package org.lamisplus.datafi.activities.forms.hts.pretest;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class PreTestPresenter extends LamisBasePresenter implements PreTestContract.Presenter {

    private final PreTestContract.View clientIntakeInfoView;

    public PreTestPresenter(PreTestContract.View clientIntakeInfoView){
        this.clientIntakeInfoView = clientIntakeInfoView;
        this.clientIntakeInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        clientIntakeInfoView.bindDrawableResources();
    }

    @Override
    public void handleClicks() {

    }
}
