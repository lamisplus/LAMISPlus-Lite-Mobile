package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;

public class LabourDeliveryContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTServicesActivity();

        void setErrorsVisibility(boolean bookingStatus, boolean dateOfDelivery, boolean romDelivery, boolean modeOfDelivery, boolean episiotomy,
                                 boolean vaginalTear, boolean feedingDecision, boolean childGivenARVwithin72hrs, boolean onArt, boolean hivExposedInfant24hrs, boolean timeDelivery,
                                 boolean artStartedLDWard, boolean hbStatus, boolean hcStatus, boolean maternalOutcome, boolean childStatus, boolean noChildAlive, boolean noChildDead);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        LabourDelivery patientToUpdate(String formName, String patientId);

        void confirmCreate(LabourDelivery labourDelivery, String packageName);

        void confirmUpdate(LabourDelivery labourDelivery, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
