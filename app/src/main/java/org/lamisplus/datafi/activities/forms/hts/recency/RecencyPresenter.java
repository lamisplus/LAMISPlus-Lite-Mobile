package org.lamisplus.datafi.activities.forms.hts.recency;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class RecencyPresenter extends LamisBasePresenter implements RecencyContract.Presenter {

    private final RecencyContract.View clientIntakeInfoView;

    public RecencyPresenter(RecencyContract.View clientIntakeInfoView){
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
