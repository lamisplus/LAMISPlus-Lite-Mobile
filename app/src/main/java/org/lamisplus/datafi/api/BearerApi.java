package org.lamisplus.datafi.api;

import android.util.Log;

import com.google.gson.Gson;

import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.LoginRequest;
import org.lamisplus.datafi.classes.TokenRequest;
import org.lamisplus.datafi.utilities.ApplicationConstants;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BearerApi {

    private String username;
    private String password;
    private boolean rememberMe;
    private String url;

    private LamisPlus lamisPlus = LamisPlus.getInstance();

    public BearerApi(String url, String username, String password, boolean rememberMe) {
        Log.v("Baron", "The URL is " + url + ApplicationConstants.API.REST_ENDPOINT + "authenticate");
        this.url = url;
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getToken() {
        String token = "";
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> result = es.submit(new Callable<String>() {
            public String call() throws Exception {
                LoginRequest loginRequest = new LoginRequest(username, password, rememberMe);
                String loginPayload = new Gson().toJson(loginRequest);

                OkHttpClient client = new OkHttpClient().newBuilder()

                        .build();

                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body = RequestBody.create(mediaType, loginPayload);

                Request request = new Request.Builder()

                        .url(url + ApplicationConstants.API.REST_ENDPOINT + "authenticate")

                        .method("POST", body)

                        .addHeader("Content-Type", "application/json")

                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    TokenRequest tokenRequest = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), TokenRequest.class);
                    lamisPlus.setServerUrl(url);
                    return tokenRequest.getId_token();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            token = result.get();
            return token;
        } catch (Exception e) {
            // failed
        }
        es.shutdown();
        return token;
    }

}
