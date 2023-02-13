package org.lamisplus.datafi.activities.login;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class LoginContract {

    interface View extends LamisBaseView<Presenter>{
        void startActivityForDashboard();
        void finishLoginActivity();

        void showLocationLoadingAnimation();

        void hideLoadingAnimation();

        void hideSoftKeys();
        void hideURLInputField();

        void showInvalidURLSnackbar(String message);
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleUserLogin(String url, String username, String password, boolean rememberMe);
    }
}
