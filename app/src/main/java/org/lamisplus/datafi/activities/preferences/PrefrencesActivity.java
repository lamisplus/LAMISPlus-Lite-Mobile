package org.lamisplus.datafi.activities.preferences;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class PrefrencesActivity extends LamisBaseActivity {

    public PrefrencesContract.Presenter mPresenter;
    public PrefrencesFragment prefrencesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrences);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + " Configurations");


        prefrencesFragment = (PrefrencesFragment) getSupportFragmentManager().findFragmentById(R.id.prefrencesContentFrame);
        if (prefrencesFragment == null) {
            prefrencesFragment = prefrencesFragment.newInstance();
        }
        if (!prefrencesFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), prefrencesFragment, R.id.prefrencesContentFrame);
        }
        mPresenter = new PrefrencesPresenter(prefrencesFragment);
    }

}
