package org.lamisplus.datafi.activities.forms.hts.elicitation;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class ElicitationActivity extends LamisBaseActivity {

    public ElicitationContract.Presenter mPresenter;
    public ElicitationFragment elicitationFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elicitation);

        elicitationFragment = (ElicitationFragment) getSupportFragmentManager().findFragmentById(R.id.elicitationContentFrame);
        if (elicitationFragment == null) {
            elicitationFragment = ElicitationFragment.newInstance();
        }
        if (!elicitationFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), elicitationFragment, R.id.elicitationContentFrame);
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

        mPresenter = new ElicitationPresenter(elicitationFragment, patientID);
    }

}
