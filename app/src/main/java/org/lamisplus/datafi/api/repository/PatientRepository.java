package org.lamisplus.datafi.api.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.application.LamisPlusLogger;
import org.lamisplus.datafi.listeners.retrofit.DefaultCallbackListener;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRepository extends RetrofitRepository {

    private LamisPlus lamisPlus = LamisPlus.getInstance();
    private LamisPlusLogger lamisPlusLogger;
    private RestApi restApi;

    public PatientRepository() {
        this.lamisPlusLogger = new LamisPlusLogger();
        String token = new BearerApi("guest@lamisplus.org", "12345", true).getToken();
        this.restApi = RestServiceBuilder.createService(RestApi.class, token);
    }

    public void syncPatient(Person person, @Nullable final DefaultCallbackListener callbackListener) {
        if(NetworkUtils.isOnline()){
            Call<Person> call = restApi.createPatient(person);
            call.enqueue(new Callback<Person>() {
                @Override
                public void onResponse(Call<Person> call, Response<Person> response) {
                    if(response.isSuccessful()){
                        LamisCustomHandler.showJson(response.body());
                    }
                    Log.v("Baron", "Message success is " + response.code());
                }

                @Override
                public void onFailure(Call<Person> call, Throwable t) {
                    Log.v("Baron", "Message is " + t.getMessage());
                    if(callbackListener != null){
                        callbackListener.onErrorResponse(t.getMessage());
                    }
                }
            });
        }
    }


}
