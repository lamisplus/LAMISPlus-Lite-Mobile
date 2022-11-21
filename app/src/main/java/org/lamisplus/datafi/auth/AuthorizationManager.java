package org.lamisplus.datafi.auth;

import android.content.Intent;

import org.lamisplus.datafi.activities.login.LoginActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class AuthorizationManager {

    protected LamisPlus mLamisPlus = LamisPlus.getInstance();

    public boolean isUserNameOrServerEmpty() {
        boolean result = false;
        if (mLamisPlus.getUsername().equals(ApplicationConstants.EMPTY_STRING) ||
                (mLamisPlus.getServerUrl().equals(ApplicationConstants.EMPTY_STRING))) {
            result = true;
        }
        return result;
    }

    public boolean isUserLoggedIn() {
        return !ApplicationConstants.EMPTY_STRING.equals(mLamisPlus.getSessionToken());
    }

    public void moveToLoginActivity() {
        Intent intent = new Intent(mLamisPlus.getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mLamisPlus.getApplicationContext().startActivity(intent);
    }

}
