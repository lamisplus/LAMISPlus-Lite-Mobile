package org.lamisplus.datafi.activities.addeditpatient;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class AddEditPatientActivity extends LamisBaseActivity {

    public AddEditPatientContract.Presenter mPresenter;
    public AddEditPatientFragment patientFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_patient);

        patientFragment = (AddEditPatientFragment) getSupportFragmentManager().findFragmentById(R.id.addEditPatientContentFrame);
        if(patientFragment == null){
            patientFragment = AddEditPatientFragment.newinstance();
        }

        if(!patientFragment.isActive()){
            addFragmentToActivity(getSupportFragmentManager(), patientFragment, R.id.addEditPatientContentFrame);
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

        mPresenter = new AddEditPatientPresenter(patientFragment, patientID);
    }
}
