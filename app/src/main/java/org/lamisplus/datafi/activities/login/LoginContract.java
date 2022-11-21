package org.lamisplus.datafi.activities.login;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class LoginContract {

    interface View extends LamisBaseView<LoginContract.Presenter>{
        void showLogin();
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleUserLogin(String url, String username, String password, boolean rememberMe);
    }
}
