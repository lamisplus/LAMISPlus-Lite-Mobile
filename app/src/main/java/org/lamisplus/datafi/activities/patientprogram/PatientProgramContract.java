package org.lamisplus.datafi.activities.patientprogram;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class PatientProgramContract {

    interface View extends LamisBaseView<Presenter>{

        void bindDrawableResources();
        void setPatientId(String id);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

    }
}
