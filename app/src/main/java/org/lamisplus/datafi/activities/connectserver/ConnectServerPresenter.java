package org.lamisplus.datafi.activities.connectserver;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class ConnectServerPresenter extends LamisBasePresenter implements ConnectServerContract.Presenter {

    private final ConnectServerContract.View dashboardInfoView;

    public ConnectServerPresenter(ConnectServerContract.View dashboardInfoView){
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
