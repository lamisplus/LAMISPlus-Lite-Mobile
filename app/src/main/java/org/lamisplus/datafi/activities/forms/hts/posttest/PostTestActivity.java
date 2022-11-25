package org.lamisplus.datafi.activities.forms.hts.posttest;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PostTestActivity extends LamisBaseActivity {

    public PostTestContract.Presenter mPresenter;
    public PostTestFragment postTestFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_test);

        postTestFragment = (PostTestFragment) getSupportFragmentManager().findFragmentById(R.id.posttestContentFrame);
        if (postTestFragment == null) {
            postTestFragment = PostTestFragment.newInstance();
        }
        if (!postTestFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), postTestFragment, R.id.posttestContentFrame);
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

        mPresenter = new PostTestPresenter(postTestFragment, patientID);
    }

}
