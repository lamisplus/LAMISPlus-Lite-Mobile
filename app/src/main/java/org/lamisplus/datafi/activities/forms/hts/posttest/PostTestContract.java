package org.lamisplus.datafi.activities.forms.hts.posttest;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class PostTestContract {

    interface View extends LamisBaseView<Presenter>{

        void bindDrawableResources();
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleClicks();

    }
}
