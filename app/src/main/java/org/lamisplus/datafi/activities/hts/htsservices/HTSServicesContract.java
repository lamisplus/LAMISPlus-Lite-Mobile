package org.lamisplus.datafi.activities.hts.htsservices;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class HTSServicesContract {

    interface View extends LamisBaseView<Presenter> {


    }

    interface Presenter extends LamisBasePresenterContract {

        String getPatientId();

    }
}
