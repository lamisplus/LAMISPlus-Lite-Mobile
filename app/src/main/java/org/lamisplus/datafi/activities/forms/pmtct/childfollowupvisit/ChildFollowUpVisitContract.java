package org.lamisplus.datafi.activities.forms.pmtct.childfollowupvisit;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.ChildFollowupVisit;
import org.lamisplus.datafi.models.Encounter;

public class ChildFollowUpVisitContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTServicesActivity();

        void setErrorsVisibility(boolean edDateofVisit, boolean infantHospitalNo, boolean bodyWeight);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        ChildFollowupVisit patientToUpdate(String formName, String patientId);

        void confirmCreate(ChildFollowupVisit childFollowupVisit, String packageName);

        void confirmUpdate(ChildFollowupVisit childFollowupVisit, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
