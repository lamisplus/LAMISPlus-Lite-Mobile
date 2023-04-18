package org.lamisplus.datafi.activities.forms.pmtct.anc;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;

public class ANCContract {

    interface View extends LamisBaseView<Presenter>{

        void scrollToTop();

        void startPMTCTEnrollmentActivity();

        void setErrorsVisibility(boolean ancNo, boolean dateOfEnrollment, boolean parity, boolean gravida, boolean lmp, boolean gaweeks, boolean sourceRef, boolean testedSyphilis, boolean hivStatus);

        void setErrorsVisibilityPatient(boolean dateOfRegisterError, boolean hospitalError, boolean firstNameError, boolean lastNameError, boolean middleNameError, boolean dateOfBirthError, boolean genderError, boolean maritalNull, boolean educationNull, boolean phoneNull, boolean stateError, boolean provinceError, boolean addressError,
                                        boolean nokFirstNameError, boolean nokMiddleNameError, boolean nokLastNameError);
    }

    interface Presenter extends LamisBasePresenterContract{

        String getPatientId();

        ANC patientToUpdate(String formName, String patientId);

        void confirmCreate(ANC anc, Person person, String packageName);

        void confirmUpdate(ANC anc, Encounter encounter);
        void confirmDeleteEncounter(String formName, String patientId);
    }
}
