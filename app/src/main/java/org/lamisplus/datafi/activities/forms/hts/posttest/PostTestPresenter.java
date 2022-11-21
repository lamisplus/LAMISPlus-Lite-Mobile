package org.lamisplus.datafi.activities.forms.hts.posttest;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class PostTestPresenter extends LamisBasePresenter implements PostTestContract.Presenter {

    private final PostTestContract.View clientIntakeInfoView;

    public PostTestPresenter(PostTestContract.View clientIntakeInfoView){
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
