package org.lamisplus.datafi.activities.forms.hts.clientintake;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class ClientIntakeContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();
        void startActivityForPreTestForm(String s);

        void startDashboardActivity();

        void startHTSActivity();

        void retreiveRSTFormData(String rstForm);
        void setErrorsVisibility(boolean targetGroup, boolean clientCode, boolean referredFrom, boolean settings, boolean visitDate,
                boolean indexTestingError, boolean firstTimeVisitError, boolean previouslyTestedError, boolean typeCounselingError);

        void setErrorsVisibilityPatient(boolean firstNameError,
                                 boolean lastNameError,
                                 boolean middleNameError,
                                 boolean dateOfBirthError,
                                 boolean genderError,
                                 boolean maritalNull,
                                 boolean stateError,
                                 boolean provinceError, boolean addressError);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();
        String getRSTForm();

        ClientIntake patientToUpdate(String formName, String patientId);

        void confirmCreate(ClientIntake clientIntake, Person person, String packageName);
        void confirmCreate(ClientIntake clientIntake, String packageName);

        void confirmUpdate(ClientIntake clientIntake, Encounter encounter);
        void confirmDeleteEncounterClientIntake(String formName, String patientId);

    }
}
