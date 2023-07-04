package org.lamisplus.datafi.activities.forms.pmtct.infantregistration;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.InfantRegistration;

public class InfantRegistrationContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTServicesActivity();

        void setErrorsVisibility(boolean InfantGivenName, boolean sex, boolean hospitalNumber);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        InfantRegistration patientToUpdate(String formName, String patientId);

        void confirmCreate(InfantRegistration infantRegistration, String packageName);

        void confirmUpdate(InfantRegistration infantRegistration, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
