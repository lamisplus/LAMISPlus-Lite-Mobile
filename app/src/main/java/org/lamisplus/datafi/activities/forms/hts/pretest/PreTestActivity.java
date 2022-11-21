package org.lamisplus.datafi.activities.forms.hts.pretest;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;

public class PreTestActivity extends LamisBaseActivity {

    public PreTestContract.Presenter mPresenter;
    public PreTestFragment preTestFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_intake);

        preTestFragment = (PreTestFragment) getSupportFragmentManager().findFragmentById(R.id.clientIntakeContentFrame);
        if (preTestFragment == null) {
            preTestFragment = PreTestFragment.newInstance();
        }
        if (!preTestFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), preTestFragment, R.id.dashboardContentFrame);
        }
        mPresenter = new PreTestPresenter(preTestFragment);
    }

}
