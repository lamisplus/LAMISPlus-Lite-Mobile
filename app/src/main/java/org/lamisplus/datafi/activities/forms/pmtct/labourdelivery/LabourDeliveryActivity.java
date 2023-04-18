package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class LabourDeliveryActivity extends LamisBaseActivity {

    public LabourDeliveryContract.Presenter mPresenter;
    public LabourDeliveryFragment LabourDeliveryFragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_delivery);

        LabourDeliveryFragment = (LabourDeliveryFragment) getSupportFragmentManager().findFragmentById(R.id.labourDeliveryContentFrame);
        if (LabourDeliveryFragment == null) {
            LabourDeliveryFragment = LabourDeliveryFragment.newInstance();
        }
        if (!LabourDeliveryFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), LabourDeliveryFragment, R.id.labourDeliveryContentFrame);
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

        mPresenter = new LabourDeliveryPresenter(LabourDeliveryFragment, patientID);
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
