package org.lamisplus.datafi.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;

public class LoginFragment extends LamisBaseFragment<LoginContract.Presenter> implements LoginContract.View{

    private EditText mServerUrl;
    private EditText mUsername;
    private EditText mPassword;
    private Button loginBtn;

    final private String initialUrl = LamisPlus.getInstance().getServerUrl();
    protected LamisPlus lamisPlus = LamisPlus.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mServerUrl.getText().toString();
                    String user = mUsername.getText().toString();
                    String pass = mPassword.getText().toString();
                    mPresenter.handleUserLogin(url, "guest@lamisplus.org", "12345", true);
                    Intent i = new Intent(getActivity(), DashboardActivity.class);
                    startActivity(i);
                }
            });
        }
        return root;
    }

    @Override
    public void showLogin() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void setListeners(){

    }

    public void initiateFragmentViews(View root){
        mServerUrl = root.findViewById(R.id.loginServerUrl);
        mUsername = root.findViewById(R.id.loginUsername);
        mPassword = root.findViewById(R.id.loginPassword);
        loginBtn = root.findViewById(R.id.loginButton);
    }


}
