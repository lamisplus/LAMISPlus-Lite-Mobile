package org.lamisplus.datafi.activities.forms.pmtct.pmtctenrollment;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;

public class PMTCTEnrollmentContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTServicesActivity();

        void setErrorsVisibility(boolean dateEnrollment, boolean entryPoint, boolean artStartDate, boolean timingARTInitiation, boolean tBStatus);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        PMTCTEnrollment patientToUpdate(String formName, String patientId);

        void confirmCreate(PMTCTEnrollment pmtctEnrollment, String packageName);

        void confirmUpdate(PMTCTEnrollment pmtctEnrollment, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
