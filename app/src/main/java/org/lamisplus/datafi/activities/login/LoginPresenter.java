package org.lamisplus.datafi.activities.login;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.api.BearerApi;
import org.lamisplus.datafi.api.RestApi;
import org.lamisplus.datafi.api.RestServiceBuilder;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.classes.LoginRequest;
import org.lamisplus.datafi.classes.TokenRequest;
import org.lamisplus.datafi.dao.AccountDAO;
import org.lamisplus.datafi.models.Account;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.NetworkUtils;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (!isFirstTimeLogin()) {
            loginInfoView.hideURLInputField();
        }
    }

    private boolean isFirstTimeLogin() {
        int accounts = AccountDAO.countUsers();
        if (accounts > 0) {
            return false;
        }
        return true;
    }

    @Override
    public void handleUserLogin(String url, String username, String password, boolean rememberMe) {
        loginInfoView.hideSoftKeys();
        loginInfoView.showLocationLoadingAnimation();
        //If the account username is empty then this user never logged in
        Account account = new Account();
        if (!username.isEmpty() && !password.isEmpty() && !url.isEmpty()) {
            Account accountLogin = AccountDAO.checkUserExists(username, password);
            if (AccountDAO.countUsers() <= 0) {
                if (NetworkUtils.isOnline() || NetworkUtils.isURLReachable(url)) {
                    String token = new BearerApi(url, username, password, rememberMe).getToken();
                    if (StringUtils.notEmpty(token) && StringUtils.notNull(token)) {
                        RestApi restApi = RestServiceBuilder.createService(RestApi.class, token);
                        Call<Object> call = restApi.getAccount();
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                        String serverCurrentOrganisationUnitId = jsonObject.getString("currentOrganisationUnitId");
                                        String currentOrganisationUnitName = jsonObject.getString("currentOrganisationUnitName");

                                        NumberFormat defForm = NumberFormat.getInstance();
                                        Number d = defForm.parse(serverCurrentOrganisationUnitId);
                                        int currentOrganisationUnitId = d.intValue();

                                        lamisPlus.setPassword(password);
                                        lamisPlus.setUsername(username);
                                        lamisPlus.setServerUrl(url);

                                        if (AccountDAO.countUsers() <= 0) {
                                            account.setPassword(password);
                                            account.setUsername(username);
                                            account.setServerUrl(url);

                                            account.setCurrentOrganisationUnitId(currentOrganisationUnitId);
                                            account.setCurrentOrganisationUnitName(currentOrganisationUnitName);
                                            account.setSelected(1);
                                            account.save();
                                        }

                                        //Run this function to select all Organizations in the Database and skip the one that was already selected
                                        JSONArray jsonArray = jsonObject.getJSONArray("applicationUserOrganisationUnits");
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject objSections = jsonArray.getJSONObject(j);

                                            NumberFormat defFormUnitId = NumberFormat.getInstance();
                                            Number dOther = defFormUnitId.parse(objSections.getString("organisationUnitId"));
                                            int organisationUnitId = dOther.intValue();
                                            String organisationUnitName = objSections.getString("organisationUnitName");

                                            if (currentOrganisationUnitId != organisationUnitId) {
                                                Account accountOther = new Account();
                                                accountOther.setPassword(password);
                                                accountOther.setUsername(username);
                                                accountOther.setServerUrl(url);

                                                accountOther.setCurrentOrganisationUnitId(organisationUnitId);
                                                accountOther.setCurrentOrganisationUnitName(organisationUnitName);
                                                accountOther.save();
                                            }
                                        }

                                        lamisPlus.setSessionToken(url);
                                        lamisPlus.setPasswordAndHashedPassword(password);
                                        lamisPlus.setSystemId(password);

                                        loginInfoView.startActivityForDashboard();
                                        loginInfoView.finishLoginActivity();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    loginInfoView.showInvalidURLSnackbar("Failed to fetch response from the server");
                                }
                                loginInfoView.hideLoadingAnimation();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                loginInfoView.showInvalidURLSnackbar(t.getMessage());
                                loginInfoView.hideLoadingAnimation();
                            }
                        });
                    } else {
                        loginInfoView.showInvalidURLSnackbar("Login failed. Please check your server connection string");
                        loginInfoView.hideLoadingAnimation();
                    }
                } else {
                    loginInfoView.showInvalidURLSnackbar("Connection is needed for a first time login");
                    loginInfoView.hideLoadingAnimation();
                }
            } else {
                if (accountLogin == null) {
                    loginInfoView.showInvalidURLSnackbar("The login details you supplied is invalid.");
                    loginInfoView.hideLoadingAnimation();
                } else {
//                    Account.load(Account.class, account.getId());
//                    account.setServerUrl(url);
//                    account.save();

                    lamisPlus.setSessionToken(url);
                    lamisPlus.setPasswordAndHashedPassword(password);
                    lamisPlus.setSystemId(password);

                    loginInfoView.startActivityForDashboard();
                    loginInfoView.finishLoginActivity();
                }
            }
        } else {
            loginInfoView.showInvalidURLSnackbar("Please enter all fields before submitting");
            loginInfoView.hideLoadingAnimation();
        }
    }

    public void getLocations(JSONObject jsonObject) {

    }

}
