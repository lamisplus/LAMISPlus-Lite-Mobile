package org.lamisplus.datafi.api.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PostTestCounseling;
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

public class HTSRepository extends RetrofitRepository{

    private RestApi restApi;
    public HTSRepository(){
        String token = new BearerApi("guest@lamisplus.org", "12345", true).getToken();
        this.restApi = RestServiceBuilder.createService(RestApi.class, token);
    }

    public void syncRst(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener) {
        if (NetworkUtils.isOnline()) {
            RiskStratification riskStratification = new Gson().fromJson(encounter.getDataValues(), RiskStratification.class);
            riskStratification.setPersonId(encounter.getPersonId());

            //String formValues = new Gson().toJson(riskStratification);
            //JsonObject jsonObject = new JsonParser().parse(formValues).getAsJsonObject();
            Call<RiskStratification> call = restApi.createRiskStratification(riskStratification);
            call.enqueue(new Callback<RiskStratification>() {
                @Override
                public void onResponse(Call<RiskStratification> call, Response<RiskStratification> response) {
                    if (response.isSuccessful()) {
                        RiskStratification rst = response.body();
                        riskStratification.setCode(rst.getCode());
                        encounter.setDataValues(new Gson().toJson(riskStratification));
                        encounter.save();
                        Log.v("Baron", "RST Synced");

                        callbackListener.onResponse();
                    }else{
                        LamisCustomHandler.showJson(response.errorBody().toString());
                        Log.v("Baron", "Couldn't sync the RST");
                        callbackListener.onErrorResponse(response.errorBody().toString() + " - " + response.message() + " - " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RiskStratification> call, Throwable t) {
                    Log.v("Baron", "Error message is " + t.getMessage());
                }
            });
        }
    }

    public void syncClientIntake(Encounter encounter,  @Nullable final DefaultCallbackListener callbackListener){
        if (NetworkUtils.isOnline()) {
            ClientIntake clientIntake = new Gson().fromJson(encounter.getDataValues(), ClientIntake.class);
            clientIntake.setPersonId(encounter.getPersonId());

            RiskStratification riskStratification = EncounterDAO.findRstFromForm(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM, encounter.getPerson());
            clientIntake.setRiskStratificationCode(riskStratification.getCode());

            JsonObject jsonObject = new JsonParser().parse(new Gson().toJson(clientIntake)).getAsJsonObject();
            Call<Object> call = restApi.createClientIntakeHTS(jsonObject);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {

                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            String serverId =  jsonObject.getString("id");
                            //The Id from the server is a string so we had to convert it using the Number format to int
                            NumberFormat defForm = NumberFormat.getInstance();
                            Number d = defForm.parse(serverId);
                            int id = d.intValue();

                            clientIntake.setHtsClientId(id);
                            encounter.setDataValues(new Gson().toJson(clientIntake));
                            encounter.setSynced(true);
                            encounter.save();
                            Log.v("Baron", "The hts id is now " + id);
                        } catch (JSONException | ParseException e) {
                            throw new RuntimeException(e);
                        }
                        LamisCustomHandler.showJson(response.body());
                        Log.v("Baron", "Client Intake has synced");
                        callbackListener.onResponse();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.v("Baron", "Client Intake " + t.getMessage());
                }
            });
        }
    }

    public void syncPreTest(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if (NetworkUtils.isOnline()) {
            PreTest preTest = new Gson().fromJson(encounter.getDataValues(), PreTest.class);
            preTest.setPersonId(encounter.getPersonId());

            ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
            preTest.setHtsClientId(clientIntake.getHtsClientId());

            Log.v("Baron", "Pretest Values are " + new Gson().toJson(preTest));
            JsonObject jsonObject = new JsonParser().parse(new Gson().toJson(preTest)).getAsJsonObject();

            Call<Object> preTestCounselingCall = restApi.updatePreTestCounseling(clientIntake.getHtsClientId(), jsonObject);
            preTestCounselingCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if(response.isSuccessful()){
                        Log.v("Baron", "Pre Test synced");
                        LamisCustomHandler.showJson(response.body());
                    }else{
                        Log.v("Baron", "Pre Test not synced");
                        LamisCustomHandler.showJson(response.errorBody() + " " + response.message() + " " + response.code());
                    }
                    callbackListener.onResponse();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.v("Baron", "Pre Test " + t.getMessage());
                }
            });
        }
    }

    public void syncRequestResult(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if (NetworkUtils.isOnline()) {
            RequestResult requestResult = new Gson().fromJson(encounter.getDataValues(), RequestResult.class);
            requestResult.setPersonId(encounter.getPersonId());

            ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
            requestResult.setHtsClientId(clientIntake.getHtsClientId());

            JsonObject jsonObject = new JsonParser().parse(new Gson().toJson(requestResult)).getAsJsonObject();

            Call<Object> preTestCounselingCall = restApi.updateRequestResult(clientIntake.getHtsClientId(), jsonObject);
            preTestCounselingCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if(response.isSuccessful()){
                        Log.v("Baron", "Request Result synced");
                        LamisCustomHandler.showJson(response.body());
                    }else{
                        Log.v("Baron", "Request Result not synced");
                        LamisCustomHandler.showJson(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.v("Baron", "Request Result " + t.getMessage());
                }
            });
        }
    }

    public void syncPostTest(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if (NetworkUtils.isOnline()) {
            PostTestCounseling postTestCounseling = new Gson().fromJson(encounter.getDataValues(), PostTestCounseling.class);
            postTestCounseling.setPersonId(encounter.getPersonId());

            ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
            postTestCounseling.setHtsClientId(clientIntake.getHtsClientId());

            JsonObject jsonObject = new JsonParser().parse(new Gson().toJson(postTestCounseling)).getAsJsonObject();

            Call<Object> preTestCounselingCall = restApi.updatePostTestCounselingKnowledgeAssessment(clientIntake.getHtsClientId(), jsonObject);
            preTestCounselingCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if(response.isSuccessful()){
                        Log.v("Baron", "Post Test synced");
                        LamisCustomHandler.showJson(response.body());
                    }else{
                        Log.v("Baron", "Post Test not synced");
                        LamisCustomHandler.showJson(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.v("Baron", "Post Test " + t.getMessage());
                }
            });
        }
    }

    public void syncRecency(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if (NetworkUtils.isOnline()) {
            Recency recency = new Gson().fromJson(encounter.getDataValues(), Recency.class);
            recency.setPersonId(encounter.getPersonId());

            ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, encounter.getPerson());
            recency.setHtsClientId(Integer.parseInt(clientIntake.getClientCode()));

            JsonObject jsonObject = new JsonParser().parse(new Gson().toJson(recency)).getAsJsonObject();

            Call<Object> recencyCall = restApi.updateRecency(clientIntake.getHtsClientId(), jsonObject);
            recencyCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if(response.isSuccessful()){
                        Log.v("Baron", "Recency synced");
                        LamisCustomHandler.showJson(response.body());
                    }else{
                        Log.v("Baron", "Recency not synced");
                        LamisCustomHandler.showJson(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.v("Baron", "Recency " + t.getMessage());
                }
            });
        }
    }

}
