package org.lamisplus.datafi.activities.pmtct.pmtctservices;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PMTCTServicesActivity extends LamisBaseActivity {

    public PMTCTServicesContract.Presenter mPresenter;
    public PMTCTServicesFragment PMTCTServicesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmtct_services);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        PMTCTServicesFragment = (PMTCTServicesFragment) getSupportFragmentManager().findFragmentById(R.id.pmtctServicesContentFrame);
        if (PMTCTServicesFragment == null) {
            PMTCTServicesFragment = PMTCTServicesFragment.newInstance();
        }
        if (!PMTCTServicesFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), PMTCTServicesFragment, R.id.pmtctServicesContentFrame);
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
        mPresenter = new PMTCTServicesPresenter(PMTCTServicesFragment, patientID);

    }



}
