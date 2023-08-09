package org.lamisplus.datafi.api.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisCustomFileHandler;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.InfantRegistration;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PMTCTRepository extends RetrofitRepository{

    private RestApi restApi;

    public PMTCTRepository() {
        this.restApi = RestServiceBuilder.createService(RestApi.class);
    }

    public void syncANC(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if(encounter != null){
            ANC anc = new Gson().fromJson(encounter.getDataValues(), ANC.class);
            String personUuid = PersonDAO.getPersonUuid(encounter.getPersonId());
            if(personUuid != null) {
                anc.setPerson_uuid(personUuid);
            }

            String formValues = new Gson().toJson(anc);
            JsonObject jsonObject = new JsonParser().parse(formValues).getAsJsonObject();
            if (NetworkUtils.isOnline()) {
                Call<Object> call = restApi.createANCEnrollement(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("ANC Synced");

                            assert callbackListener != null;
                            callbackListener.onResponse();
                        } else {
                            assert response.errorBody() != null;
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the ANC, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(formValues);
                            assert callbackListener != null;
                            callbackListener.onErrorResponse(response.errorBody().toString() + " - " + response.message() + " - " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("ANC Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }


    public void syncPMTCTEnrollment(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if(encounter != null){
            PMTCTEnrollment pmtctEnrollment = new Gson().fromJson(encounter.getDataValues(), PMTCTEnrollment.class);

            String formValues = new Gson().toJson(pmtctEnrollment);
            Log.v("Baron PMTCT", formValues);
            JsonObject jsonObject = new JsonParser().parse(formValues).getAsJsonObject();
            if (NetworkUtils.isOnline()) {
                Call<Object> call = restApi.createPmtctEnrollment(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("PMTCT Enrollment Synced");

                            assert callbackListener != null;
                            callbackListener.onResponse();
                        } else {
                            assert response.errorBody() != null;
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the PMTCT Enrollment, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(formValues);
                            assert callbackListener != null;
                            callbackListener.onErrorResponse(response.errorBody().toString() + " - " + response.message() + " - " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("PMTCT Enrollment Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }

    public void syncLabourDelivery(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if(encounter != null){
            LabourDelivery labourDelivery = new Gson().fromJson(encounter.getDataValues(), LabourDelivery.class);

            String formValues = new Gson().toJson(labourDelivery);
            Log.v("Baron Labour & Delivery", formValues);
            JsonObject jsonObject = new JsonParser().parse(formValues).getAsJsonObject();
            if (NetworkUtils.isOnline()) {
                Call<Object> call = restApi.createPmtctDelivery(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Labour & Delivery Synced");

                            assert callbackListener != null;
                            callbackListener.onResponse();
                        } else {
                            assert response.errorBody() != null;
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the Labour & Delivery, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(formValues);
                            assert callbackListener != null;
                            callbackListener.onErrorResponse(response.errorBody().toString() + " - " + response.message() + " - " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Labour & Delivery Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }

    public void syncInfantRegistration(Encounter encounter, @Nullable final DefaultCallbackListener callbackListener){
        if(encounter != null){
            InfantRegistration labourDelivery = new Gson().fromJson(encounter.getDataValues(), InfantRegistration.class);

            String formValues = new Gson().toJson(labourDelivery);
            Log.v("Baron Infant Reg", formValues);
            JsonObject jsonObject = new JsonParser().parse(formValues).getAsJsonObject();
            if (NetworkUtils.isOnline()) {
                Call<Object> call = restApi.createInfants(jsonObject);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                        if (response.isSuccessful()) {
                            encounter.setSynced(true);
                            encounter.save();
                            LamisCustomFileHandler.writeLogToFile("Infant Registration Synced");

                            assert callbackListener != null;
                            callbackListener.onResponse();
                        } else {
                            assert response.errorBody() != null;
                            LamisCustomFileHandler.writeLogToFile("Couldn't sync the Infant Registration, Reason: " + "Error Body: " + response.errorBody().toString() + " - Error Message: " + response.message() + " - Error Code: " + response.code());
                            LamisCustomFileHandler.writeLogToFile(formValues);
                            assert callbackListener != null;
                            callbackListener.onErrorResponse(response.errorBody().toString() + " - " + response.message() + " - " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                        LamisCustomFileHandler.writeLogToFile("Infant Registration Failure, Message: " + t.getMessage());
                    }
                });
            }
        }
    }




}
