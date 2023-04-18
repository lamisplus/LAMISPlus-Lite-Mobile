package org.lamisplus.datafi.activities.forms.pmtct.infantregistration;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;

public class InfantRegistrationContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTEnrollmentActivity();

        void setErrorsVisibility(boolean dateOfBirth, boolean entryPoint, boolean settings, boolean modality, boolean visitDate, boolean targetGroup, boolean autolastHivTestBasedOnRequest);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        PMTCTEnrollment patientToUpdate(String formName, String patientId);

        void confirmCreate(PMTCTEnrollment pmtctEnrollment, String packageName);

        void confirmUpdate(PMTCTEnrollment pmtctEnrollment, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
