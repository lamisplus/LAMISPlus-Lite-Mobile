package org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class EnrolmentFollowUpCard2Activity extends LamisBaseActivity {

    public EnrolmentFollowUpCard2Contract.Presenter mPresenter;
    public EnrolmentFollowUpCard2Fragment EnrolmentFollowUpCard2Fragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolmentfollowupcard2);

        EnrolmentFollowUpCard2Fragment = (EnrolmentFollowUpCard2Fragment) getSupportFragmentManager().findFragmentById(R.id.enrolFollowCard2ContentFrame);
        if (EnrolmentFollowUpCard2Fragment == null) {
            EnrolmentFollowUpCard2Fragment = EnrolmentFollowUpCard2Fragment.newInstance();
        }
        if (!EnrolmentFollowUpCard2Fragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), EnrolmentFollowUpCard2Fragment, R.id.enrolFollowCard2ContentFrame);
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

        mPresenter = new EnrolmentFollowUpCard2Presenter(EnrolmentFollowUpCard2Fragment, patientID);
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
