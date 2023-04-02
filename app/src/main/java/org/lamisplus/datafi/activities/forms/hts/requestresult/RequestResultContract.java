package org.lamisplus.datafi.activities.forms.hts.requestresult;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.models.RiskStratification;

public class RequestResultContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();
        void startActivityForPostTestForm();

        void startDashboardActivity();

        void setErrorsVisibility(boolean edDateTest1, boolean autoResultTest1, boolean edConfirmDate1, boolean autoConfirmResult1, boolean edTieBreaker1, boolean autoResultTieBreaker1, boolean edDateTest2, boolean autoResultTest2, boolean edConfirmDate2, boolean autoConfirmResult2, boolean edDateTieBreaker2, boolean autoTieBreakerResult2);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        RequestResult patientToUpdate(String formName, String patientId);

        void confirmCreate(RequestResult requestResult, String packageName);

        void confirmUpdate(RequestResult requestResult, Encounter encounter);

        void confirmDeleteEncounterRequestResult(String formName, String patientId);
    }
}
