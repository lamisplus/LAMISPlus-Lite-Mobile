package org.lamisplus.datafi.api.repository;

import static java.lang.reflect.Modifier.TRANSIENT;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.activeandroid.query.Update;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRepository extends RetrofitRepository {

    private LamisPlus lamisPlus = LamisPlus.getInstance();
    private LamisPlusLogger lamisPlusLogger;
    private RestApi restApi;
    private List<Long> syncedIds = new ArrayList<>();

    public PatientRepository() {
        this.lamisPlusLogger = new LamisPlusLogger();
        //String token = new BearerApi("guest@lamisplus.org", "12345", true).getToken();
        this.restApi = RestServiceBuilder.createService(RestApi.class);
    }

    public synchronized void syncPatient(Person person, @Nullable final DefaultCallbackListener callbackListener) {
        final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .excludeFieldsWithModifiers(TRANSIENT) // STATIC|TRANSIENT in the default configuration
                .create();
        String ss = gson.toJson(person);
        final String payload = ss.replaceAll("\\\\", "").replaceAll("\"\\[\\{", "[{").replaceAll("\\}\\]\"", "}]");

        JsonObject jsonObject = new JsonParser().parse(payload.toString()).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if(!person.isSynced()) {
                Call<Object> call = restApi.createPatient(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            LamisCustomFileHandler.writeLogToFile("Patient Details Synced successfully");

                            try {
                                syncedIds.add(person.getId());
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                String serverId = jsonObject.getString("id");
                                String serverUuid = jsonObject.getString("uuid");
                                NumberFormat defForm = NumberFormat.getInstance();
                                Number d = defForm.parse(serverId);
                                int id = d.intValue();

                                person.setPersonId(id);
                                person.setPersonUuId(serverUuid);
                                person.setSynced(true);
                                person.save();

                                EncounterDAO.updateAllEncountersWithPatientId(id, person.getId().toString());
                                BiometricsDAO.updateAllBiometricsWithPatiendId(id, person.getId().toString());
                            } catch (JSONException | ParseException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the Patient Details, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Patient Details Failure, Message: " + t.getMessage());
                        if (callbackListener != null) {
                            callbackListener.onErrorResponse(t.getMessage());
                        }
                    }
                });
            }
        }
    }


}
