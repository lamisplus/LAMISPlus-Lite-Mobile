package org.lamisplus.datafi.activities.forms.pmtct.anc;

import android.util.Log;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.ANC;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

public class ANCPresenter extends LamisBasePresenter implements ANCContract.Presenter {

    private final ANCContract.View ancInfoView;
    private String patientId;

    public ANCPresenter(ANCContract.View ancInfoView, String patientId) {
        this.ancInfoView = ancInfoView;
        this.patientId = patientId;
        this.ancInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public ANC patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findAncFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(ANC anc, Person person, String packageName) {
        if (validate(anc) && validatePerson(person)) {
            //save the person
            long personId = person.save();
            //save the ANC form
            String ancEncounter = new Gson().toJson(anc);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.ANC_FORM);
            encounter.setPerson(String.valueOf(personId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(ancEncounter);
            encounter.save();

            ancInfoView.startPMTCTEnrollmentActivity();
        } else {
            ancInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(ANC anc, Encounter encounter) {
        if (validate(anc)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(anc);
            encounter.setDataValues(s);
            encounter.save();
            ancInfoView.startPMTCTEnrollmentActivity();
        } else {
            ancInfoView.scrollToTop();
        }
    }

    public boolean validate(ANC anc) {
        boolean ancNo = false;
        boolean dateOfEnrollment = false;
        boolean parity = false;
        boolean gravida = false;
        boolean lmp = false;
        boolean gaweeks = false;
        boolean sourceRef = false;
        boolean testedSyphilis = false;
        boolean hivStatus = false;


        ancInfoView.setErrorsVisibility(ancNo, dateOfEnrollment, parity, gravida, lmp, gaweeks, sourceRef, testedSyphilis, hivStatus);

        if (StringUtils.isBlank(anc.getAncNo())) {
            ancNo = true;
        }

        if (StringUtils.isBlank(anc.getDateOfEnrollment())) {
            dateOfEnrollment = true;
        }

        if (StringUtils.isBlank(anc.getParity())) {
            parity = true;
        }

        if (StringUtils.isBlank(anc.getGravida())) {
            gravida = true;
        }

        if (StringUtils.isBlank(anc.getLmp())) {
            lmp = true;
        }

        if (StringUtils.isBlank(anc.getGaweeks())) {
            gaweeks = true;
        }

        if (StringUtils.isBlank(anc.getSourceOfReferral())) {
            sourceRef = true;
        }

        if (StringUtils.isBlank(anc.getTestedSyphilis())) {
            testedSyphilis = true;
        }

        if (StringUtils.isBlank(anc.getStaticHivStatus())) {
            hivStatus = true;
        }

        boolean result = !ancNo && !dateOfEnrollment && !parity && !gravida && !lmp && !gaweeks && !sourceRef && !testedSyphilis
                && !hivStatus;


        if (result) {
            return true;
        } else {
            ancInfoView.setErrorsVisibility(ancNo, dateOfEnrollment, parity, gravida, lmp, gaweeks, sourceRef, testedSyphilis, hivStatus);
            return false;
        }
    }

    private boolean validatePerson(Person person) {
        boolean firstNameError = false;
        boolean lastNameError = false;
        boolean middleNameError = false;
        boolean dateOfBirthError = false;
        boolean genderError = false;
        boolean maritalNull = false;
        boolean educationNull = false;
        boolean phoneNull = false;
        boolean stateError = false;
        boolean provinceError = false;
        boolean addressError = false;
        boolean nokFirstNameError = false;
        boolean nokMiddleNameError = false;
        boolean nokLastNameError = false;
        boolean dateOfRegisterError = false;
        boolean hospitalError = false;

        ancInfoView.setErrorsVisibilityPatient(dateOfRegisterError, hospitalError, firstNameError, lastNameError, middleNameError, dateOfBirthError, genderError, maritalNull, educationNull, phoneNull, stateError, provinceError, addressError,
                nokFirstNameError, nokMiddleNameError, nokLastNameError);

        if (StringUtils.isBlank(person.getFirstName())
                || !ViewUtils.validateText(person.getFirstName(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            firstNameError = true;
        }

        if (StringUtils.isBlank(person.getSurname())
                || !ViewUtils.validateText(person.getSurname(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            lastNameError = true;
        }

        if (!StringUtils.isBlank(person.getOtherName()) && !ViewUtils.validateText(person.getOtherName(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
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

        if (person.getGenderId() == null) {
            genderError = true;
        }

        if (person.getMaritalStatusId() == null) {
            maritalNull = true;
        }


        if (person.getEducationId() == null) {
            educationNull = true;
        }

        if (StringUtils.isBlank(person.pullContactPointList().get(0).getValue())
                || !ViewUtils.validateText(person.pullContactPointList().get(0).getValue(), ViewUtils.ILLEGAL_CHARACTERS)) {
            phoneNull = true;
        }

        if (person.getAddresses().getStateId() == null) {
            stateError = true;
        }

        if (person.getAddresses().getDistrict() == null) {
            provinceError = true;
        }

        if (person.getAddresses().getCity() == null) {
            addressError = true;
        }

        if (!StringUtils.isBlank(person.getContacts().getFirstName()) && !ViewUtils.validateText(person.getContacts().getFirstName(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            nokFirstNameError = true;
        }

        if (!StringUtils.isBlank(person.getContacts().getOtherName()) && !ViewUtils.validateText(person.getContacts().getOtherName(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            nokMiddleNameError = true;
        }

        if (!StringUtils.isBlank(person.getContacts().getSurname()) && !ViewUtils.validateText(person.getContacts().getSurname(), ViewUtils.ILLEGAL_NAME_CHARACTERS)) {
            nokLastNameError = true;
        }

        boolean result = !firstNameError && !lastNameError && !middleNameError && !dateOfRegisterError && !dateOfBirthError && !hospitalError && !genderError && !maritalNull
                && !educationNull && !phoneNull && !stateError && !provinceError && !addressError && !nokFirstNameError && !nokMiddleNameError && !nokLastNameError;

        if (result) {
            return true;
        } else {
            ancInfoView.setErrorsVisibilityPatient(dateOfRegisterError, hospitalError, firstNameError, lastNameError, middleNameError, dateOfBirthError, genderError, maritalNull, educationNull, phoneNull, stateError, provinceError, addressError,
                    nokFirstNameError, nokMiddleNameError, nokLastNameError);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        //EncounterDAO.deleteEncounter(formName, patientId);
        ancInfoView.startPMTCTEnrollmentActivity();
    }

}
