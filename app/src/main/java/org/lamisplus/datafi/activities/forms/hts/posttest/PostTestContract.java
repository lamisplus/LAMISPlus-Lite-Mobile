package org.lamisplus.datafi.activities.forms.hts.posttest;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PostTest;

public class PostTestContract {

    interface View extends LamisBaseView<Presenter>{

        void startActivityForRecencyForm();
        void startDashboardActivity();
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        PostTest patientToUpdate(String formName, String patientId);

        void confirmCreate(PostTest postTest, String packageName);

        void confirmUpdate(PostTest postTest, Encounter encounter);

        void confirmDeleteEncounterPostTest(String formName, String patientId);

    }
}
