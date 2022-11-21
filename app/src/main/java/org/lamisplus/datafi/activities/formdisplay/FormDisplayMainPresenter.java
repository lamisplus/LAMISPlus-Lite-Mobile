package org.lamisplus.datafi.activities.formdisplay;

import android.os.Bundle;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class FormDisplayMainPresenter extends LamisBasePresenter implements FormDisplayContract.Presenter.MainPresenter {

    private FormDisplayContract.View.MainView mFormDisplayView;

    public FormDisplayMainPresenter(FormDisplayContract.View.MainView mFormDisplayView, Bundle bundle, FormPageAdapter mPageAdapter) {
        this.mFormDisplayView = mFormDisplayView;
        mFormDisplayView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

}
