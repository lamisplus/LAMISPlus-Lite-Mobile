package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;

public class LabourDeliveryContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTEnrollmentActivity();

        void setErrorsVisibility(boolean dateOfBirth, boolean entryPoint, boolean settings, boolean modality, boolean visitDate, boolean targetGroup, boolean autolastHivTestBasedOnRequest);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        LabourDelivery patientToUpdate(String formName, String patientId);

        void confirmCreate(LabourDelivery labourDelivery, String packageName);

        void confirmUpdate(LabourDelivery labourDelivery, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
