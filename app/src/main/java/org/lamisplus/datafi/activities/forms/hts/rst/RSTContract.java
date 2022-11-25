package org.lamisplus.datafi.activities.forms.hts.rst;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RiskStratification;

public class RSTContract {

    interface View extends LamisBaseView<Presenter>{

        void startActivityForClientIntakeForm();

        void startDashboardActivity();
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        RiskStratification patientToUpdate(String formName, String patientId);

        void confirmCreate(RiskStratification riskStratification, String packageName);

        void confirmUpdate(RiskStratification riskStratification, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
