package org.lamisplus.datafi.activities.forms.hts.elicitation;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;

public class ElicitationContract {

    interface View extends LamisBaseView<Presenter>{
        void scrollToTop();
        void startDashboardActivity();

        void setErrorsVisibility(boolean offeredINSError);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        Elicitation patientToUpdate(String formName, String patientId);

        void confirmCreate(Elicitation elicitation, String packageName);

        void confirmUpdate(Elicitation elicitation, Encounter encounter);

        void confirmDeleteEncounterElicitation(String formName, String patientId);
    }
}
