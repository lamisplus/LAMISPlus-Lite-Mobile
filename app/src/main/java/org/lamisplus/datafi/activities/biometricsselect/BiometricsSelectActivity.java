package org.lamisplus.datafi.activities.biometricsselect;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.ToastUtil;

public class BiometricsSelectActivity extends LamisBaseActivity {

    public BiometricsSelectContract.Presenter mPresenter;
    public BiometricsSelectFragment biometricsSelectFragment;

    private String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrics_select);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        biometricsSelectFragment = (BiometricsSelectFragment) getSupportFragmentManager().findFragmentById(R.id.biometricsSelectContentFrame);
        if (biometricsSelectFragment == null) {
            biometricsSelectFragment = BiometricsSelectFragment.newInstance();
        }
        if (!biometricsSelectFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), biometricsSelectFragment, R.id.biometricsSelectContentFrame);
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

        mPresenter = new BiometricsSelectPresenter(biometricsSelectFragment, patientID);
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
