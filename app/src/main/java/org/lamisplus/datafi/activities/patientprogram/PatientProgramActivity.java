package org.lamisplus.datafi.activities.patientprogram;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PatientProgramActivity extends LamisBaseActivity {

    public PatientProgramContract.Presenter mPresenter;
    public PatientProgramFragment appFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_program);


        appFragment = (PatientProgramFragment) getSupportFragmentManager().findFragmentById(R.id.patientProgramContentFrame);
        if (appFragment == null) {
            appFragment = PatientProgramFragment.newInstance();
        }
        if (!appFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), appFragment, R.id.patientProgramContentFrame);
        }

        Bundle patientBundle = savedInstanceState;
        if (patientBundle != null) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }
        String patientID = "";
        if (patientBundle != null) {
            patientID = patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        }

        mPresenter = new PatientProgramPresenter(appFragment, patientID);
    }


}
