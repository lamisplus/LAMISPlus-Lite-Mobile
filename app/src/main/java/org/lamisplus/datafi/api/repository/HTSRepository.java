package org.lamisplus.datafi.api.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PostTest;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;

import java.text.NumberFormat;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HTSRepository extends RetrofitRepository {

    private RestApi restApi;

    public HTSRepository() {
        //String token = new BearerApi("guest@lamisplus.org", "12345", true).getToken();
        this.restApi = RestServiceBuilder.createService(RestApi.class);
    }

    public void syncRst(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener) {
        RiskStratification riskStratification = new Gson().fromJson(encounter.getDataValues(), RiskStratification.class);
        riskStratification.setPersonId(encounter.getPersonId());

        String formValues = new Gson().toJson(riskStratification);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");
        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            Call<RiskStratification> call = restApi.createRiskStratification(jsonObject);
            call.enqueue(new Callback<RiskStratification>() {
                @Override
                public void onResponse(Call<RiskStratification> call, Response<RiskStratification> response) {
                    if (response.isSuccessful()) {
                        RiskStratification rst = response.body();
                        riskStratification.setCode(rst.getCode());
                        encounter.setDataValues(new Gson().toJson(riskStratification));
                        encounter.setSynced(true);
                        encounter.save();
                        LamisCustomFileHandler.writeLogToFile("RST Synced");

                        callbackListener.onResponse();
                    } else {
                        LamisCustomFileHandler.writeLogToFile("Couldn't sync the RST, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                        LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        callbackListener.onErrorResponse(response.errorBody().toString() + " - " + response.message() + " - " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RiskStratification> call, Throwable t) {
                    LamisCustomFileHandler.writeLogToFile("RST Failure, Message: " + t.getMessage());
                }
            });
        }
    }

    public void syncClientIntake(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener) {
        ClientIntake clientIntake = new Gson().fromJson(encounter.getDataValues(), ClientIntake.class);
        clientIntake.setPersonId(encounter.getPersonId());

        RiskStratification riskStratification = EncounterDAO.findRstFromForm(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, encounter.getPerson());
        clientIntake.setRiskStratificationCode(riskStratification.getCode());

        String formValues = new Gson().toJson(clientIntake);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");
        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if (!encounter.isSynced()) {
                //LamisCustomHandler.showJson(clientIntake);
                Call<Object> call = restApi.createClientIntakeHTS(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                String serverId = jsonObject.getString("id");
                                //The Id from the server is a string so we had to convert it using the Number format to int
                                NumberFormat defForm = NumberFormat.getInstance();
                                Number d = defForm.parse(serverId);
                                int id = d.intValue();

                                clientIntake.setHtsClientId(id);
                                encounter.setDataValues(new Gson().toJson(clientIntake));
                                encounter.setSynced(true);
                                encounter.save();

                            } catch (JSONException | ParseException e) {
                                throw new RuntimeException(e);
                            }
                            LamisCustomFileHandler.writeLogToFile("Client Intake Form Synced");
                            callbackListener.onResponse();
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't Sync the Client Intake Form, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Client Intake Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }

    public void syncPreTest(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener) {
        PreTest preTest = new Gson().fromJson(encounter.getDataValues(), PreTest.class);
        preTest.setPersonId(encounter.getPersonId());

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
        preTest.setHtsClientId(clientIntake.getHtsClientId());
        preTest.setHtsClientId(clientIntake.getHtsClientId());

        String formValues = new Gson().toJson(preTest);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");

        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if (!encounter.isSynced()) {
                Call<Object> preTestCounselingCall = restApi.updatePreTestCounseling(clientIntake.getHtsClientId(), jsonObject);
                preTestCounselingCall.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Pre Test Form Synced");
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't Sync the Pre Test Form, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        }
                        callbackListener.onResponse();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Pre Test Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }

    public void syncRequestResult(Encounter encounter,
                                  @Nullable final DefaultCallbackListener callbackListener) {
        RequestResult requestResult = new Gson().fromJson(encounter.getDataValues(), RequestResult.class);
        requestResult.setPersonId(encounter.getPersonId());

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
        requestResult.setHtsClientId(clientIntake.getHtsClientId());

        String formValues = new Gson().toJson(requestResult);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");

        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if (!encounter.isSynced()) {
                Call<Object> preTestCounselingCall = restApi.updateRequestResult(clientIntake.getHtsClientId(), jsonObject);
                preTestCounselingCall.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Request Result Form Synced");
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't Sync the Request Result Form, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Request Result Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }

    public void syncPostTest(Encounter encounter,
                             @Nullable final DefaultCallbackListener callbackListener) {
        PostTest postTestCounseling = new Gson().fromJson(encounter.getDataValues(), PostTest.class);
        postTestCounseling.setPersonId(encounter.getPersonId());

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
        postTestCounseling.setHtsClientId(clientIntake.getHtsClientId());

        String formValues = new Gson().toJson(postTestCounseling);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");

        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if (!encounter.isSynced()) {
                Call<Object> preTestCounselingCall = restApi.updatePostTestCounselingKnowledgeAssessment(clientIntake.getHtsClientId(), jsonObject);
                preTestCounselingCall.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Post Test Form Synced");
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't Sync the Post Test Form, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Post Test Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }

    public void syncRecency(Encounter encounter,
                            @Nullable final DefaultCallbackListener callbackListener) {

        Recency recency = new Gson().fromJson(encounter.getDataValues(), Recency.class);
        recency.setPersonId(encounter.getPersonId());

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
        recency.setHtsClientId(clientIntake.getHtsClientId());

        String formValues = new Gson().toJson(recency);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");

        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if (!encounter.isSynced()) {
                Call<Object> recencyCall = restApi.updateRecency(clientIntake.getHtsClientId(), jsonObject);
                recencyCall.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Recency Form Synced");
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't Sync the Recency Form, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Recency Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }


    public void syncElicitation(Encounter encounter,
                                @Nullable final DefaultCallbackListener callbackListener) {
        Elicitation elicitation = new Gson().fromJson(encounter.getDataValues(), Elicitation.class);

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
        elicitation.setHtsClientId(clientIntake.getHtsClientId());

        String formValues = new Gson().toJson(elicitation);
        final String payload = formValues.replaceAll("\u003c", "<").replaceAll("\u003d", "=").replaceAll("\u003e", ">");

        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if (!encounter.isSynced()) {
                Call<Object> indexElicitationCall = restApi.createIndexElicitation(jsonObject);
                indexElicitationCall.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Index Elicitation Form Synced");
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't Sync the Index Elicitation Form, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(encounter.getDataValues());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Index Elicitation Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }


}
