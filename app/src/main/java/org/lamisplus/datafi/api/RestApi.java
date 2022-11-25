package org.lamisplus.datafi.api;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lamisplus.datafi.models.Person;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApi {

    @POST("authenticate")
    Call<Object> getToken(@Body Object json);

    @POST("patient")
    Call<Object> createPatient(@Body Object personPayload);


}
