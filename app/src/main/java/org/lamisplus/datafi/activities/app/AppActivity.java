package org.lamisplus.datafi.activities.app;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class AppActivity extends LamisBaseActivity {

    public AppContract.Presenter mPresenter;
    public AppFragment appFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.lamis_plus_logo);


        appFragment = (AppFragment) getSupportFragmentManager().findFragmentById(R.id.appContentFrame);
        if (appFragment == null) {
            appFragment = AppFragment.newInstance();
        }
        if (!appFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), appFragment, R.id.appContentFrame);
        }

        mPresenter = new AppPresenter(appFragment);
    }


}
