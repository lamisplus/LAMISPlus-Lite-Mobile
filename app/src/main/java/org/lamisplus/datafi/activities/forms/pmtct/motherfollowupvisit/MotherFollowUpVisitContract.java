package org.lamisplus.datafi.activities.forms.pmtct.motherfollowupvisit;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.MotherFollowupVisit;
import org.lamisplus.datafi.models.PMTCTEnrollment;

public class MotherFollowUpVisitContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTServicesActivity();

        void setErrorsVisibility(boolean edDateofVisit, boolean entryPoint, boolean fpCounselling, boolean dsd, boolean maternalOutcome, boolean dateOutcome, boolean clientStatusVisit);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        MotherFollowupVisit patientToUpdate(String formName, String patientId);

        void confirmCreate(MotherFollowupVisit motherFollowupVisit, String packageName);

        void confirmUpdate(MotherFollowupVisit motherFollowupVisit, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
