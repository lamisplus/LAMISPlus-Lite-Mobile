package org.lamisplus.datafi.activities.login;

import android.util.Log;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.LoginRequest;
import org.lamisplus.datafi.classes.TokenRequest;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.NetworkUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;

public class LoginPresenter extends LamisBasePresenter implements LoginContract.Presenter {

    private final LoginContract.View loginInfoView;
    private LamisPlus lamisPlus;

    public LoginPresenter(LoginContract.View loginInfoView, LamisPlus lamisPlus) {
        this.loginInfoView = loginInfoView;
        this.lamisPlus = lamisPlus;
        this.loginInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void handleUserLogin(String url, String username, String password, boolean rememberMe) {
        if (NetworkUtils.isOnline()) {
            String token = new BearerApi(username, password, rememberMe).getToken();
            RestApi restApi = RestServiceBuilder.createService(RestApi.class, token);
            //Call<Object> call = restApi.get();

//            RestServiceBuilder.createNewBearer(username, password, rememberMe);
//            RestServiceBuilder.createService(RestApi.class);


//            Call<Object> call = restApi.getToken(objectPay);
//            call.enqueue(new Callback<Object>() {
//                @Override
//                public void onResponse(Call<Object> call, Response<Object> response) {
//                    LamisCustomHandler.showJson(response);
//                    Log.v("Baron", "The status code is:" + response.code() + "  " + response.raw());
//                    if (response.isSuccessful()) {
//                         response.body();
//                        lamisPlus.setSessionToken(url);
//                        lamisPlus.setPasswordAndHashedPassword(password);
//                        lamisPlus.setSystemId(password);
////                        if (token.getId_token() != null) {
//                            Log.v("Baron", "Token is not empty");
////                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Object> call, Throwable t) {
//                    Toast.makeText(LamisPlus.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.v("Baron", t.getMessage());
//                }
//            });
        } else {

        }
        lamisPlus.setSessionToken(url);
        lamisPlus.setPasswordAndHashedPassword(password);
        lamisPlus.setSystemId(password);
    }
}
