package org.lamisplus.datafi.activities.forms.pmtct.childfollowupvisit;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class ChildFollowUpVisitActivity extends LamisBaseActivity {

    public ChildFollowUpVisitContract.Presenter mPresenter;
    public ChildFollowUpVisitFragment ChildFollowUpVisitFragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_followup_visit);

        ChildFollowUpVisitFragment = (ChildFollowUpVisitFragment) getSupportFragmentManager().findFragmentById(R.id.childfollowupContentFrame);
        if (ChildFollowUpVisitFragment == null) {
            ChildFollowUpVisitFragment = ChildFollowUpVisitFragment.newInstance();
        }
        if (!ChildFollowUpVisitFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), ChildFollowUpVisitFragment, R.id.childfollowupContentFrame);
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

        mPresenter = new ChildFollowUpVisitPresenter(ChildFollowUpVisitFragment, patientID);
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
