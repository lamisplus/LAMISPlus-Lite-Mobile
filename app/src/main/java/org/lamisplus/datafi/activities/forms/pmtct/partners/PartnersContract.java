package org.lamisplus.datafi.activities.forms.pmtct.partners;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PartnerRegistration;

public class PartnersContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTServicesActivity();

        void setErrorsVisibility(boolean fullName, boolean age, boolean pretestCounseled);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        PartnerRegistration patientToUpdate(String formName, String patientId);

        void confirmCreate(PartnerRegistration partnerRegistration, String packageName);

        void confirmUpdate(PartnerRegistration partnerRegistration, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
