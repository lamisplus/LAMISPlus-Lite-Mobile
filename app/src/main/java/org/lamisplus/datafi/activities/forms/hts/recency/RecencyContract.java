package org.lamisplus.datafi.activities.forms.hts.recency;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class RecencyContract {

    interface View extends LamisBaseView<Presenter>{

        void bindDrawableResources();
    }

    interface Presenter extends LamisBasePresenterContract{

        void handleClicks();

    }
}
