package org.lamisplus.datafi.activities.forms.hts.requestresult;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.models.RiskStratification;

public class RequestResultContract {

    interface View extends LamisBaseView<Presenter>{

        void startActivityForPostTestForm();

        void startDashboardActivity();
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        RequestResult patientToUpdate(String formName, String patientId);

        void confirmCreate(RequestResult requestResult, String packageName);

        void confirmUpdate(RequestResult requestResult, Encounter encounter);

        void confirmDeleteEncounterRequestResult(String formName, String patientId);
    }
}
