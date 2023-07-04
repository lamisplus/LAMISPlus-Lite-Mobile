package org.lamisplus.datafi.activities.forms.pmtct.motherfollowupvisit;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class MotherFollowUpVisitActivity extends LamisBaseActivity {

    public MotherFollowUpVisitContract.Presenter mPresenter;
    public MotherFollowUpVisitFragment MotherFollowUpVisitFragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_followup_visit);

        MotherFollowUpVisitFragment = (MotherFollowUpVisitFragment) getSupportFragmentManager().findFragmentById(R.id.motherfollowupContentFrame);
        if (MotherFollowUpVisitFragment == null) {
            MotherFollowUpVisitFragment = MotherFollowUpVisitFragment.newInstance();
        }
        if (!MotherFollowUpVisitFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), MotherFollowUpVisitFragment, R.id.motherfollowupContentFrame);
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

        mPresenter = new MotherFollowUpVisitPresenter(MotherFollowUpVisitFragment, patientID);
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
