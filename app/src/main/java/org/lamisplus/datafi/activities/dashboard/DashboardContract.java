package org.lamisplus.datafi.activities.dashboard;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class DashboardContract {

    interface View extends LamisBaseView<DashboardContract.Presenter>{

        void bindDrawableResources();
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleClicks();

    }
}
