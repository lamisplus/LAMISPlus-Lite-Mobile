package org.lamisplus.datafi.activities.biometrics;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.activities.dashboard.DashboardActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class BiometricsActivity extends LamisBaseActivity {

    public BiometricsContract.Presenter mPresenter;
    public BiometricsFragment biometricsFragment;
    String patientID = "";
    Boolean recapture = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrics);

        biometricsFragment = (BiometricsFragment) getSupportFragmentManager().findFragmentById(R.id.biometricsContentFrame);
        if (biometricsFragment == null) {
            biometricsFragment = BiometricsFragment.newInstance();
        }
        if (!biometricsFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), biometricsFragment, R.id.biometricsContentFrame);
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

        Bundle recaptureBundle = savedInstanceState;
        if (recaptureBundle != null) {
            recaptureBundle.getString(ApplicationConstants.BundleKeys.BIOMETRICS_RECAPTURE);
        } else {
            recaptureBundle = getIntent().getExtras();
        }

        if (recaptureBundle != null) {
            recapture = recaptureBundle.getBoolean(ApplicationConstants.BundleKeys.BIOMETRICS_RECAPTURE);
        }


        mPresenter = new BiometricsPresenter(biometricsFragment, patientID, recapture);

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
