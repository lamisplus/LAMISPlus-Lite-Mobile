package org.lamisplus.datafi.activities.forms.hts.pretest;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PreTestActivity extends LamisBaseActivity {

    public PreTestContract.Presenter mPresenter;
    public PreTestFragment preTestFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_test);

        preTestFragment = (PreTestFragment) getSupportFragmentManager().findFragmentById(R.id.pretestContentFrame);
        if (preTestFragment == null) {
            preTestFragment = PreTestFragment.newInstance();
        }
        if (!preTestFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), preTestFragment, R.id.pretestContentFrame);
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

        mPresenter = new PreTestPresenter(preTestFragment, patientID);
    }

}
