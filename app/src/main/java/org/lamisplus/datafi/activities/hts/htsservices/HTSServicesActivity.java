package org.lamisplus.datafi.activities.hts.htsservices;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.hts.htsprogram.HTSProgramPresenter;
import org.lamisplus.datafi.activities.patientprofile.PatientProfilePresenter;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ToastUtil;

public class HTSServicesActivity extends LamisBaseActivity {

    public HTSServicesContract.Presenter mPresenter;
    public HTSServicesFragment HTSServicesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hts_services);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        HTSServicesFragment = (HTSServicesFragment) getSupportFragmentManager().findFragmentById(R.id.htsServicesContentFrame);
        if (HTSServicesFragment == null) {
            HTSServicesFragment = HTSServicesFragment.newInstance();
        }
        if (!HTSServicesFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), HTSServicesFragment, R.id.htsServicesContentFrame);
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
        mPresenter = new HTSServicesPresenter(HTSServicesFragment, patientID);

    }



}
