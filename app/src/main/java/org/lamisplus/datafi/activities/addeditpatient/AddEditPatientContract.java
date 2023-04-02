package org.lamisplus.datafi.activities.addeditpatient;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Person;

public class AddEditPatientContract {

    interface View extends LamisBaseView<Presenter>{
        void scrollToTop();
        void hideSoftKeys();
        void startPatientProfileActivity(Person person);
        boolean areFieldsNotEmpty();

        void setProgressBarVisibility(boolean b);

        void setErrorsVisibility(boolean firstNameError,
                                 boolean lastNameError,
                                 boolean middleNameError,
                                 boolean dateOfBirthError,
                                 boolean dateOfRegisterError,
                                 boolean hospitalError,
                                 boolean genderError,
                                 boolean maritalNull,
                                 boolean educationNull,
                                 boolean phoneNull,
                                 boolean stateError,
                                 boolean provinceError,
                                 boolean nokFirstNameError,
                                 boolean nokMiddleNameError,
                                 boolean nokLastNameError);
    }

    interface Presenter extends LamisBasePresenterContract{

        Person patientToUpdate();
        void confirmRegister(Person person);
        void confirmUpdate(Person person);

        Long getPatientId();

        boolean isRegisteringPatient();
    }

}
