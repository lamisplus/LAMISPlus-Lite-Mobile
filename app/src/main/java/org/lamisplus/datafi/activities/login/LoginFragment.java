package org.lamisplus.datafi.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseFragment;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;
import org.lamisplus.datafi.utilities.URLValidator;

public class LoginFragment extends LamisBaseFragment<LoginContract.Presenter> implements LoginContract.View{

    private View root;
    private EditText mServerUrl;
    private EditText mUsername;
    private EditText mPassword;
    private Button loginBtn;

    final private String initialUrl = LamisPlus.getInstance().getServerUrl();
    protected LamisPlus lamisPlus = LamisPlus.getInstance();

    private ProgressBar mLocationLoadingProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_login, container, false);
        if (root != null) {
            initiateFragmentViews(root);
            setHasOptionsMenu(true);
            setListeners();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mServerUrl.getText().toString().trim();
                    String user = mUsername.getText().toString().trim();
                    String pass = mPassword.getText().toString().trim();
                    mPresenter.handleUserLogin(url, user, pass, true);
                }
            });
        }
        return root;
    }

    @Override
    public void startActivityForDashboard() {
        Intent intent = new Intent(lamisPlus.getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        lamisPlus.getApplicationContext().startActivity(intent);
    }

    @Override
    public void finishLoginActivity() {
        getActivity().finish();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void setListeners(){
        mServerUrl.setOnFocusChangeListener((view, hasFocus) -> {
            if (StringUtils.notEmpty(mServerUrl.getText().toString())
                    && !view.isFocused()) {
                ((LoginFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.loginContentFrame))
                        .setUrl(mServerUrl.getText().toString());
            }

            if (hasFocus) {
                mServerUrl.setHint("");
                //mUrlInput.setHint(Html.fromHtml(getString(R.string.login_url_hint)));
            } else if (mServerUrl.getText().toString().equals("")) {
                //mUrl.setHint(Html.fromHtml(getString(R.string.login_url_hint) + getString(R.string.req_star)));
                //mUrlInput.setHint("");
            }
        });
    }

    @Override
    public void hideLoadingAnimation() {
        mLocationLoadingProgressBar.setVisibility(View.INVISIBLE);
        loginBtn.setEnabled(true);
    }

    @Override
    public void showLocationLoadingAnimation() {
        mLocationLoadingProgressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
    }

    public void initiateFragmentViews(View root){
        mServerUrl = root.findViewById(R.id.loginServerUrl);
        mUsername = root.findViewById(R.id.loginUsername);
        mPassword = root.findViewById(R.id.loginPassword);
        loginBtn = root.findViewById(R.id.loginButton);

        mLocationLoadingProgressBar = root.findViewById(R.id.locationLoadingProgressBar);
    }

    @Override
    public void hideSoftKeys() {
        View view = this.getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(this.getActivity());
        }
        InputMethodManager inputMethodManager = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void hideURLInputField() {
        mServerUrl.setVisibility(View.GONE);
    }

    private Snackbar createSnackbar(String message) {
        return Snackbar
                .make(root, message, Snackbar.LENGTH_LONG);
    }

    @Override
    public void showInvalidURLSnackbar(String message) {
        createSnackbar(message)
                .show();
    }

    public void setUrl(String url) {
        URLValidator.ValidationResult result = URLValidator.validate(url);

        if (result.isURLValid()) {
           Log.v(ApplicationConstants.LAMIS_TAG, "URL is valid");
        } else {
            showInvalidURLSnackbar("Invalid URL");
        }
    }

}
