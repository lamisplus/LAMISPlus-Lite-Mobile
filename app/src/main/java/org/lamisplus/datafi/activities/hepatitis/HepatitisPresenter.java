package org.lamisplus.datafi.activities.hepatitis;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class HepatitisPresenter extends LamisBasePresenter implements HepatitisContract.Presenter {

    private final HepatitisContract.View dashboardInfoView;

    public HepatitisPresenter(HepatitisContract.View dashboardInfoView){
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
