package org.lamisplus.datafi.activities.forms.hts.clientintake;

import android.content.Context;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ViewUtils;

public class ClientIntakePresenter extends LamisBasePresenter implements ClientIntakeContract.Presenter {

    private final ClientIntakeContract.View clientIntakeInfoView;
    private String patientId;
    private String rstForm;

    public ClientIntakePresenter(ClientIntakeContract.View clientIntakeInfoView, String patientId, String rstForm){
        this.clientIntakeInfoView = clientIntakeInfoView;
        this.patientId = patientId;
        this.rstForm = rstForm;
        this.clientIntakeInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        clientIntakeInfoView.retreiveRSTFormData(rstForm);
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getRSTForm() {
        return rstForm;
    }

    @Override
    public ClientIntake patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findClientIntakeFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(ClientIntake clientIntake, Person person, String packageName) {
        if (validate(clientIntake) && validatePerson(person)) {
            //save the person
            long personId = person.save();
            //save the RST form
            Encounter encounterRst = new Encounter();
            encounterRst.setName(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM);
            encounterRst.setPerson(String.valueOf(personId));
            encounterRst.setPackageName(packageName);
            encounterRst.setDataValues(rstForm);
            encounterRst.save();
            //Save the Client Intake Form
            String clientIntakeEncounter = new Gson().toJson(clientIntake);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.CLIENT_INTAKE_FORM);
            encounter.setPerson(String.valueOf(personId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(clientIntakeEncounter);
            encounter.save();

            clientIntakeInfoView.startActivityForPreTestForm();
        }else{
            clientIntakeInfoView.scrollToTop();
        }
    }


    @Override
    public void confirmUpdate(ClientIntake clientIntake, Encounter encounter) {
        if (validate(clientIntake)) {
            String s = new Gson().toJson(clientIntake);
            encounter.setDataValues(s);
            encounter.save();
            clientIntakeInfoView.startActivityForPreTestForm();
        }else{
            clientIntakeInfoView.scrollToTop();
        }
    }

    public boolean validate(ClientIntake clientIntake) {
        boolean targetGroupError = false;
        boolean clientCodeError = false;
        boolean referredFromError = false;
        boolean settingsError = false;
        boolean visitDateError = false;

        clientIntakeInfoView.setErrorsVisibility(targetGroupError, clientCodeError, referredFromError, settingsError, visitDateError);

        if (StringUtils.isBlank(clientIntake.getTargetGroup())) {
            targetGroupError = true;
        }

        if (StringUtils.isBlank(clientIntake.getClientCode())) {
            clientCodeError = true;
        }

        if (clientIntake.getReferredFrom() == 0) {
            referredFromError = true;
        }

        if (StringUtils.isBlank(clientIntake.getTestingSetting())) {
            settingsError = true;
        }

        if (StringUtils.isBlank(clientIntake.getDateVisit())) {
            visitDateError = true;
        }

        boolean result = !targetGroupError && !clientCodeError && !referredFromError && !settingsError && !visitDateError;

        if (result) {
            return true;
        } else {
            clientIntakeInfoView.setErrorsVisibility(targetGroupError, clientCodeError, referredFromError, settingsError, visitDateError);
            return false;
        }

    }

    private boolean validatePerson(Person person) {
        boolean firstNameError = false;
        boolean lastNameError = false;
        boolean middleNameError = false;
        boolean dateOfBirthError = false;
        boolean genderError = false;
        boolean employmentNull = false;
        boolean maritalNull = false;
        boolean educationNull = false;
        boolean phoneNull = false;
        boolean stateError = false;
        boolean provinceError = false;

        clientIntakeInfoView.setErrorsVisibilityPatient(firstNameError, lastNameError, middleNameError, dateOfBirthError, genderError, employmentNull, maritalNull, educationNull, phoneNull, stateError, provinceError);

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

        if (StringUtils.isBlank(person.getDateOfBirth())) {
            dateOfBirthError = true;
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

        boolean result = !firstNameError && !lastNameError && !middleNameError && !dateOfBirthError && !genderError && !maritalNull && !employmentNull && !educationNull && !phoneNull && !stateError && !provinceError;

        if (result) {
            return true;
        } else {
            clientIntakeInfoView.setErrorsVisibilityPatient(firstNameError, lastNameError, middleNameError, dateOfBirthError, genderError, employmentNull, maritalNull, educationNull, phoneNull, stateError, provinceError);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounterClientIntake(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        clientIntakeInfoView.startDashboardActivity();
    }

}
