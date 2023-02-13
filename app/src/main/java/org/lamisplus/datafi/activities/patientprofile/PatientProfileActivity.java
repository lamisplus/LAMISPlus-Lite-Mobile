package org.lamisplus.datafi.activities.patientprofile;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ToastUtil;

public class PatientProfileActivity extends LamisBaseActivity {


    public PatientProfileContract.Presenter mPresenter;
    public PatientProfileFragment patientProfileFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        patientProfileFragment = (PatientProfileFragment) getSupportFragmentManager().findFragmentById(R.id.patientProfileContentFrame);
        if(patientProfileFragment == null){
            patientProfileFragment = PatientProfileFragment.newInstance();
        }
        if(!patientProfileFragment.isActive()){
            addFragmentToActivity(getSupportFragmentManager(), patientProfileFragment, R.id.patientProfileContentFrame);
        }

        Bundle patientBundle = savedInstanceState;
        if (patientBundle != null) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }
        String patientID = "";
        if (patientBundle != null) {
            patientID = String.valueOf(patientBundle.get(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE));
        }

        mPresenter = new PatientProfilePresenter(patientProfileFragment, patientID);
    }

}
