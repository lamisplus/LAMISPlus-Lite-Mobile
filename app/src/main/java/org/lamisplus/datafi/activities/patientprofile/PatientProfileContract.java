package org.lamisplus.datafi.activities.patientprofile;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class PatientProfileContract {

    interface View extends LamisBaseView<Presenter>{

    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();
    }
}
