package org.lamisplus.datafi.activities.forms.hts.requestresult;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RiskStratification;

public class RequestResultContract {

    interface View extends LamisBaseView<Presenter>{

        void startActivityForClientIntakeForm();

    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        RiskStratification patientToUpdate(String formName, String patientId);

        void confirmCreate(RiskStratification riskStratification);

        void confirmUpdate(RiskStratification riskStratification, Encounter encounter);
    }
}
