package org.lamisplus.datafi.activities.forms.pmtct.pmtctenrollment;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PMTCTEnrollmentActivity extends LamisBaseActivity {

    public PMTCTEnrollmentContract.Presenter mPresenter;
    public PMTCTEnrollmentFragment PMTCTEnrollmentFragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmtct_enrollment);

        PMTCTEnrollmentFragment = (PMTCTEnrollmentFragment) getSupportFragmentManager().findFragmentById(R.id.pmtctContentFrame);
        if (PMTCTEnrollmentFragment == null) {
            PMTCTEnrollmentFragment = PMTCTEnrollmentFragment.newInstance();
        }
        if (!PMTCTEnrollmentFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), PMTCTEnrollmentFragment, R.id.pmtctContentFrame);
        }

        Bundle patientBundle = savedInstanceState;
        if (patientBundle != null) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }

        if (patientBundle != null) {
            patientID = patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        }

        mPresenter = new PMTCTEnrollmentPresenter(PMTCTEnrollmentFragment, patientID);
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, patientID);
        onSaveInstanceState(bundle);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
