package org.lamisplus.datafi.activities.testconnection;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.AccountDAO;
import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.StringUtils;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestConnectionPresenter extends LamisBasePresenter implements TestConnectionContract.Presenter {

    private final TestConnectionContract.View loginInfoView;
    private LamisPlus lamisPlus;

    public TestConnectionPresenter(TestConnectionContract.View loginInfoView, LamisPlus lamisPlus) {
        this.loginInfoView = loginInfoView;
        this.lamisPlus = lamisPlus;
        this.loginInfoView.setPresenter(this);
    }


    @Override
    public void subscribe() {

    }
}
