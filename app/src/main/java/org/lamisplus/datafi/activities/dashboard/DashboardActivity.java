package org.lamisplus.datafi.activities.dashboard;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBaseActivity;
import org.lamisplus.datafi.models.Encounter;

public class DashboardActivity extends LamisBaseActivity {

    public DashboardContract.Presenter mPresenter;
    public DashboardFragment dashboardFragment;
    private Encounter mforms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.lamis_plus_logo);


        dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.dashboardContentFrame);
        if (dashboardFragment == null) {
            dashboardFragment = DashboardFragment.newInstance();
        }
        if (!dashboardFragment.isActive()) {
            addFragmentToActivity(getSupportFragmentManager(), dashboardFragment, R.id.dashboardContentFrame);
        }
        mPresenter = new DashboardPresenter(dashboardFragment);
    }

}
