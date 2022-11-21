package org.lamisplus.datafi.activities.forms.hts.posttest;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;

public class PostTestActivity extends LamisBaseActivity {

    public PostTestContract.Presenter mPresenter;
    public PostTestFragment postTestFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_intake);

        postTestFragment = (PostTestFragment) getSupportFragmentManager().findFragmentById(R.id.clientIntakeContentFrame);
        if (postTestFragment == null) {
            postTestFragment = PostTestFragment.newInstance();
        }
        if (!postTestFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), postTestFragment, R.id.dashboardContentFrame);
        }
        mPresenter = new PostTestPresenter(postTestFragment);
    }

}
