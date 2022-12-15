package org.lamisplus.datafi.activities.addeditpatient;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class AddEditPatientActivity extends LamisBaseActivity {

    public AddEditPatientContract.Presenter mPresenter;
    public AddEditPatientFragment patientFragment;
    private AlertDialog alertDialog;
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

    @Override
    public void onBackPressed() {
        if (!mPresenter.isRegisteringPatient()) {
            boolean createDialog = patientFragment.areFieldsNotEmpty();
            if (createDialog) {
                showInfoLostDialog();
            } else {
                if (!mPresenter.isRegisteringPatient()) {
                    super.onBackPressed();
                }
            }
        }
    }

    private void showInfoLostDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle(R.string.dialog_title_are_you_sure);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.dialog_message_data_lost)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_button_stay, (dialog, id) -> dialog.cancel())
                .setNegativeButton(R.string.dialog_button_leave, (dialog, id) -> {
                    // Finish the activity
                    super.onBackPressed();
                    finish();
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
