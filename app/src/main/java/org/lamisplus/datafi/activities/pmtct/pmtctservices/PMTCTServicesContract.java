package org.lamisplus.datafi.activities.pmtct.pmtctservices;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class PMTCTServicesContract {

    interface View extends LamisBaseView<Presenter> {


    }

    interface Presenter extends LamisBasePresenterContract {

        String getPatientId();

    }
}
