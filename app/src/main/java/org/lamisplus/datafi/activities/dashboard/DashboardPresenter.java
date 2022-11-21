package org.lamisplus.datafi.activities.dashboard;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class DashboardPresenter extends LamisBasePresenter implements DashboardContract.Presenter {

    private final DashboardContract.View dashboardInfoView;

    public DashboardPresenter(DashboardContract.View dashboardInfoView){
        this.dashboardInfoView = dashboardInfoView;
        this.dashboardInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        dashboardInfoView.bindDrawableResources();
    }

    @Override
    public void handleClicks() {

    }
}
