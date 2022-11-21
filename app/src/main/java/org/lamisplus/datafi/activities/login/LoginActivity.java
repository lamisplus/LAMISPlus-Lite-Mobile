package org.lamisplus.datafi.activities.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class LoginActivity extends LamisBaseActivity {

    public LoginContract.Presenter mPresenter;
    public LoginFragment loginFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.loginContentFrame);
        if(loginFragment == null){
            loginFragment = LoginFragment.newInstance();
        }
        if(!loginFragment.isActive()){
            addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.loginContentFrame);
        }

        mPresenter = new LoginPresenter(loginFragment, mLamisPlus);
    }

}
