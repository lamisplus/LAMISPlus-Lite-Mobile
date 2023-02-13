package org.lamisplus.datafi.activities.preferences;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class PrefrencesPresenter extends LamisBasePresenter implements PrefrencesContract.Presenter {

    private final PrefrencesContract.View preferencesInfoView;

    public PrefrencesPresenter(PrefrencesContract.View preferencesInfoView){
        this.preferencesInfoView = preferencesInfoView;
        this.preferencesInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

}
