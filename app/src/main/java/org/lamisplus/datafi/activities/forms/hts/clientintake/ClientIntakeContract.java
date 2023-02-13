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
        void startActivityForPreTestForm();

        void startDashboardActivity();

        void retreiveRSTFormData(String rstForm);
        void setErrorsVisibility(boolean targetGroup, boolean clientCode, boolean referredFrom, boolean settings, boolean visitDate);

        void setErrorsVisibilityPatient(boolean firstNameError,
                                 boolean lastNameError,
                                 boolean middleNameError,
                                 boolean dateOfBirthError,
                                 boolean genderError,
                                 boolean employmentNull,
                                 boolean maritalNull,
                                 boolean educationNull,
                                 boolean phoneNull,
                                 boolean stateError,
                                 boolean provinceError);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();
        String getRSTForm();

        ClientIntake patientToUpdate(String formName, String patientId);

        void confirmCreate(ClientIntake clientIntake, Person person, String packageName);

        void confirmUpdate(ClientIntake clientIntake, Encounter encounter);
        void confirmDeleteEncounterClientIntake(String formName, String patientId);

    }
}
