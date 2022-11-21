package org.lamisplus.datafi.activities.app;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class AppPresenter extends LamisBasePresenter implements AppContract.Presenter {

    private final AppContract.View appInfoView;

    public AppPresenter(AppContract.View appInfoView){
        this.appInfoView = appInfoView;
        this.appInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        appInfoView.bindDrawableResources();
    }

    @Override
    public void handleClicks() {

    }
}
