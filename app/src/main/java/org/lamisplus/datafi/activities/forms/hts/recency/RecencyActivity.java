package org.lamisplus.datafi.activities.forms.hts.recency;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;

public class RecencyActivity extends LamisBaseActivity {

    public RecencyContract.Presenter mPresenter;
    public RecencyFragment recencyFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_intake);

        recencyFragment = (RecencyFragment) getSupportFragmentManager().findFragmentById(R.id.clientIntakeContentFrame);
        if (recencyFragment == null) {
            recencyFragment = RecencyFragment.newInstance();
        }
        if (!recencyFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), recencyFragment, R.id.dashboardContentFrame);
        }
        mPresenter = new RecencyPresenter(recencyFragment);
    }

}
