package org.lamisplus.datafi.activities.biometricsselect;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class BiometricsSelectContract {

    interface View extends LamisBaseView<Presenter>{

        void bindDrawableResources();
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();
        void handleClicks();

    }
}
