package org.lamisplus.datafi.activities.forms.hts.recency;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RecencyActivity extends LamisBaseActivity {

    public RecencyContract.Presenter mPresenter;
    public RecencyFragment recencyFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recency);

        recencyFragment = (RecencyFragment) getSupportFragmentManager().findFragmentById(R.id.recencyContentFrame);
        if (recencyFragment == null) {
            recencyFragment = RecencyFragment.newInstance();
        }
        if (!recencyFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), recencyFragment, R.id.recencyContentFrame);
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

        mPresenter = new RecencyPresenter(recencyFragment, patientID);
    }

}
