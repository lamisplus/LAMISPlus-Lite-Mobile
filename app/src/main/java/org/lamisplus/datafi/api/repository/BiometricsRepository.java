package org.lamisplus.datafi.api.repository;

import static java.lang.reflect.Modifier.TRANSIENT;

import static SecuGen.FDxSDKPro.SGFDxDeviceName.SG_DEV_AUTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Biometrics;
import org.lamisplus.datafi.models.BiometricsRecapture;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;

import java.text.NumberFormat;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiometricsRepository extends RetrofitRepository {

    private RestApi restApi;

    public BiometricsRepository() {
        this.restApi = RestServiceBuilder.createService(RestApi.class);
    }

    public void syncBiometrics(Biometrics biometrics, final DefaultCallbackListener callbackListener) {
        final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .excludeFieldsWithModifiers(TRANSIENT).disableHtmlEscaping() // STATIC|TRANSIENT in the default configuration
                .create();
        String biometricsJson = gson.toJson(biometrics);
        final String payload = biometricsJson.replaceAll("\\\\", "").replaceAll("\"\\[\\{", "[{").replaceAll("\\}\\]\"", "}]");
        LamisCustomHandler.showJson(payload.replaceAll("\\/", ""));
        //LamisCustomFileHandler.writeLogToFile(payload.replaceAll("\\/", ""));
        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if(biometrics.getSyncStatus() == 0) {
                Call<Object> call = restApi.createBiometrics(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            LamisCustomFileHandler.writeLogToFile("Patient Biometrics Synced successfully");

                            biometrics.setSyncStatus(1);
                            biometrics.save();
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the Patient Biometrics, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Patient Biometrics Failure, Message: " + t.getMessage());
                        if (callbackListener != null) {
                            callbackListener.onErrorResponse(t.getMessage());
                        }
                    }
                });
            }
        }
    }

    public void syncBiometricsRecapture(BiometricsRecapture biometricsRecapture, final DefaultCallbackListener callbackListener) {
        final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .excludeFieldsWithModifiers(TRANSIENT).disableHtmlEscaping() // STATIC|TRANSIENT in the default configuration
                .create();
        String biometricsJson = gson.toJson(biometricsRecapture);
        final String payload = biometricsJson.replaceAll("\\\\", "").replaceAll("\"\\[\\{", "[{").replaceAll("\\}\\]\"", "}]");
        LamisCustomHandler.showJson(payload.replaceAll("\\/", ""));
        //LamisCustomFileHandler.writeLogToFile(payload.replaceAll("\\/", ""));
        JsonObject jsonObject = new JsonParser().parse(payload).getAsJsonObject();
        if (NetworkUtils.isOnline()) {
            if(biometricsRecapture.getSyncStatus() == 0) {
                Call<Object> call = restApi.createBiometrics(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            LamisCustomFileHandler.writeLogToFile("Patient Biometrics Recapture Synced successfully");

                            biometricsRecapture.setSyncStatus(1);
                            biometricsRecapture.save();
                        } else {
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the Patient Biometrics Recapture, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Patient Biometrics Recapture Failure, Message: " + t.getMessage());
                        if (callbackListener != null) {
                            callbackListener.onErrorResponse(t.getMessage());
                        }
                    }
                });
            }
        }
    }
}
