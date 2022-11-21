package org.lamisplus.datafi.activities.forms.hts.clientintake;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;

public class ClientIntakeContract {

    interface View extends LamisBaseView<Presenter>{

        void startActivityForPreTestForm();

    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        ClientIntake patientToUpdate(String formName, String patientId);

        void confirmCreate(ClientIntake clientIntake);

        void confirmUpdate(ClientIntake clientIntake, Encounter encounter);

    }
}
