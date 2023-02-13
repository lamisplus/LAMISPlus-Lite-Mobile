package org.lamisplus.datafi.activities.login;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class LoginActivity extends LamisBaseActivity {

    public LoginContract.Presenter mPresenter;
    public LoginFragment loginFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
