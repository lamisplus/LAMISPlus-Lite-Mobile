package org.lamisplus.datafi.activities.hepatitis;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class HepatitisContract {

    interface View extends LamisBaseView<Presenter>{

        void bindDrawableResources();
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleClicks();

    }
}
