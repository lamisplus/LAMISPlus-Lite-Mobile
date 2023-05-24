package org.lamisplus.datafi.activities.hepatitis;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class HepatitisActivity extends LamisBaseActivity {

    public HepatitisContract.Presenter mPresenter;
    public HepatitisFragment hepatitisFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hepatitis);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        hepatitisFragment = (HepatitisFragment) getSupportFragmentManager().findFragmentById(R.id.hepatitisContentFrame);
        if (hepatitisFragment == null) {
            hepatitisFragment = HepatitisFragment.newInstance();
        }
        if (!hepatitisFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), hepatitisFragment, R.id.hepatitisContentFrame);
        }
        mPresenter = new HepatitisPresenter(hepatitisFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
