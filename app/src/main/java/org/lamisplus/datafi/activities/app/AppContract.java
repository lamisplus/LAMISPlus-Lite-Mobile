package org.lamisplus.datafi.activities.app;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class AppContract {

    interface View extends LamisBaseView<Presenter>{

        void bindDrawableResources();
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleClicks();

    }
}
