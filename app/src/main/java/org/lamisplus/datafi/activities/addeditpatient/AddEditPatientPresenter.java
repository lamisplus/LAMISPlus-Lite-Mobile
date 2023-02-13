package org.lamisplus.datafi.activities.addeditpatient;

import android.content.Intent;
import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

public class AddEditPatientPresenter extends LamisBasePresenter implements AddEditPatientContract.Presenter {

    private boolean registeringPatient = false;
    private Person mPerson;
    public AddEditPatientContract.View mAddEditPatientInfoView;
    private String patientToUpdateId;

    public AddEditPatientPresenter(AddEditPatientContract.View mAddEditPatientInfoView, String patientId) {
        this.mAddEditPatientInfoView = mAddEditPatientInfoView;
        this.patientToUpdateId = patientId;
        this.mAddEditPatientInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public Long getPatientId(){
        return Long.parseLong(patientToUpdateId);
    }

    @Override
    public Person patientToUpdate() {
        return PersonDAO.findPersonById(patientToUpdateId);
    }

    @Override
    public void confirmRegister(Person person) {
        if (!registeringPatient && validate(person)) {
            person.save();
            mAddEditPatientInfoView.startPatientProfileActivity(person);
        }else{
            mAddEditPatientInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(Person person) {
        if (!registeringPatient && validate(person)) {
            LamisCustomHandler.showJson(person);
            mAddEditPatientInfoView.scrollToTop();
            mAddEditPatientInfoView.setProgressBarVisibility(true);
            person.save();
            mAddEditPatientInfoView.startPatientProfileActivity(person);
            registeringPatient = true;
        }else{
            mAddEditPatientInfoView.scrollToTop();
        }
    }

    private boolean validate(Person person) {
        boolean firstNameError = false;
        boolean lastNameError = false;
        boolean middleNameError = false;
        boolean dateOfBirthError = false;
        boolean dateOfRegisterError = false;
        boolean hospitalError = false;
        boolean genderError = false;
        boolean employmentNull = false;
        boolean maritalNull = false;
        boolean educationNull = false;
        boolean phoneNull = false;
        boolean stateError = false;
        boolean provinceError = false;

        mAddEditPatientInfoView.setErrorsVisibility(firstNameError, lastNameError, middleNameError, dateOfBirthError, dateOfRegisterError, hospitalError, genderError, employmentNull, maritalNull, educationNull, phoneNull, stateError, provinceError);

        if (StringUtils.isBlank(person.getFirstName())
                || !ViewUtils.validateText(person.getFirstName(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            firstNameError = true;
        }

        if (StringUtils.isBlank(person.getSurname())
                || !ViewUtils.validateText(person.getSurname(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            lastNameError = true;
        }

        if(!StringUtils.isBlank(person.getOtherName()) && !ViewUtils.validateText(person.getOtherName(), ViewUtils.ILLEGAL_NAME_CHARACTERS)){
            middleNameError = true;
        }

        if (StringUtils.isBlank(person.getDateOfRegistration())) {
            dateOfRegisterError = true;
        }

        if (StringUtils.isBlank(person.getDateOfBirth())) {
            dateOfBirthError = true;
        }

        if (StringUtils.isBlank(person.getIdentifiers().getValue())) {
            hospitalError = true;
        }

        if (person.getGenderId() == 0) {
            genderError = true;
        }

        if (person.getMaritalStatusId() == 0) {
            maritalNull = true;
        }


        if (person.getEmploymentStatusId() == 0) {
            employmentNull = true;
        }

        if (person.getEducationId() == 0) {
            educationNull = true;
        }

        if (StringUtils.isBlank(person.pullContactPointList().get(0).getValue())
                || !ViewUtils.validateText(person.pullContactPointList().get(0).getValue(), ViewUtils.ILLEGAL_CHARACTERS)) {
            phoneNull = true;
        }

        if (person.getAddress().get(0).getStateId() == 0) {
            stateError = true;
        }

        if (person.getAddress().get(0).getDistrict() == null) {
            provinceError = true;
        }

        boolean result = !firstNameError && !lastNameError && !middleNameError && !dateOfRegisterError && !dateOfBirthError && !hospitalError && !genderError && !maritalNull && !employmentNull && !educationNull && !phoneNull && !stateError && !provinceError;

        if (result) {
            mPerson = person;
            return true;
        } else {
            mAddEditPatientInfoView.setErrorsVisibility(firstNameError, lastNameError, middleNameError, dateOfBirthError, dateOfRegisterError, hospitalError, genderError, employmentNull, maritalNull, educationNull, phoneNull, stateError, provinceError);
            return false;
        }
    }


    @Override
    public boolean isRegisteringPatient() {
        return registeringPatient;
    }
}
