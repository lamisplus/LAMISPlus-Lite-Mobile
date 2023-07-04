package org.lamisplus.datafi.activities.biometricsrecapture;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class BiometricsRecaptureActivity extends LamisBaseActivity {

    public BiometricsRecaptureContract.Presenter mPresenter;
    public BiometricsRecaptureFragment biometricsRecaptureFragment;

    private String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrics_recapture);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        biometricsRecaptureFragment = (BiometricsRecaptureFragment) getSupportFragmentManager().findFragmentById(R.id.biometricsRecaptureContentFrame);
        if (biometricsRecaptureFragment == null) {
            biometricsRecaptureFragment = BiometricsRecaptureFragment.newInstance();
        }
        if (!biometricsRecaptureFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), biometricsRecaptureFragment, R.id.biometricsRecaptureContentFrame);
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

        mPresenter = new BiometricsRecapturePresenter(biometricsRecaptureFragment, patientID);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
