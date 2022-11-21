package org.lamisplus.datafi.activities.findpatient;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;

public class FindPatientActivity extends LamisBaseActivity {

    public FindPatientContract.Presenter mPresenter;
    public FindPatientFragment findPatientFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_patient);

        findPatientFragment = (FindPatientFragment) getSupportFragmentManager().findFragmentById(R.id.findPatientContentFrame);
        if (findPatientFragment == null) {
            findPatientFragment = FindPatientFragment.newInstance();
        }
        if (!findPatientFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), findPatientFragment, R.id.findPatientContentFrame);
        }
        mPresenter = new FindPatientPresenter(findPatientFragment);
    }

}
