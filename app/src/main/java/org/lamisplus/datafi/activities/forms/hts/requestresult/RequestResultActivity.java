package org.lamisplus.datafi.activities.forms.hts.requestresult;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RequestResultActivity extends LamisBaseActivity {

    public RequestResultContract.Presenter mPresenter;
    public RequestResultFragment requestResultFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_result);

        requestResultFragment = (RequestResultFragment) getSupportFragmentManager().findFragmentById(R.id.requestResultContentFrame);
        if (requestResultFragment == null) {
            requestResultFragment = RequestResultFragment.newInstance();
        }
        if (!requestResultFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), requestResultFragment, R.id.requestResultContentFrame);
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

        mPresenter = new RequestResultPresenter(requestResultFragment, patientID);
    }

}
