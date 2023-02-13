package org.lamisplus.datafi.activities.forms.hts.recency;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.Recency;

public class RecencyContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();
        void startActivityForElicitation();
        void startDashboardActivity();
        void setErrorsVisibility(boolean optOutRTRI, boolean testName, boolean testDate, boolean rencencyId, boolean controlLine, boolean verififcationLine, boolean longTermLine, boolean hasViralLoad, boolean sampleReferanceNumber, boolean sampleType);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        Recency patientToUpdate(String formName, String patientId);

        void confirmCreate(Recency recency, String packageName);

        void confirmUpdate(Recency recency, Encounter encounter);

        void confirmDeleteEncounterRecency(String formName, String patientId);

    }
}
