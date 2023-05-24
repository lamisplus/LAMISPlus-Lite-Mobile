package org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class EnrolmentFollowUpCard1Activity extends LamisBaseActivity {

    public EnrolmentFollowUpCard1Contract.Presenter mPresenter;
    public EnrolmentFollowUpCard1Fragment EnrolmentFollowUpCard1Fragment;

    String patientID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolmentfollowupcard1);

        EnrolmentFollowUpCard1Fragment = (EnrolmentFollowUpCard1Fragment) getSupportFragmentManager().findFragmentById(R.id.enrolFollowCard1ContentFrame);
        if (EnrolmentFollowUpCard1Fragment == null) {
            EnrolmentFollowUpCard1Fragment = EnrolmentFollowUpCard1Fragment.newInstance();
        }
        if (!EnrolmentFollowUpCard1Fragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), EnrolmentFollowUpCard1Fragment, R.id.enrolFollowCard1ContentFrame);
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

        mPresenter = new EnrolmentFollowUpCard1Presenter(EnrolmentFollowUpCard1Fragment, patientID);
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
