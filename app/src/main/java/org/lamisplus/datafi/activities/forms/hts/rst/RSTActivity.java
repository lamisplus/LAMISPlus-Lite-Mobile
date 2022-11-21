package org.lamisplus.datafi.activities.forms.hts.rst;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RSTActivity extends LamisBaseActivity {

    public RSTContract.Presenter mPresenter;
    public RSTFragment rstFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rst);

        rstFragment = (RSTFragment) getSupportFragmentManager().findFragmentById(R.id.rstContentFrame);
        if (rstFragment == null) {
            rstFragment = RSTFragment.newInstance();
        }
        if (!rstFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), rstFragment, R.id.rstContentFrame);
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

        mPresenter = new RSTPresenter(rstFragment, patientID);
    }

}
