package org.lamisplus.datafi.activities.forms.pmtct.infantregistration;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class InfantRegistrationActivity extends LamisBaseActivity {

    public InfantRegistrationContract.Presenter mPresenter;
    public InfantRegistrationFragment InfantRegistrationFragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmtct_enrollment);

        InfantRegistrationFragment = (InfantRegistrationFragment) getSupportFragmentManager().findFragmentById(R.id.pmtctContentFrame);
        if (InfantRegistrationFragment == null) {
            InfantRegistrationFragment = InfantRegistrationFragment.newInstance();
        }
        if (!InfantRegistrationFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), InfantRegistrationFragment, R.id.pmtctContentFrame);
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

        mPresenter = new InfantRegistrationPresenter(InfantRegistrationFragment, patientID);
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
