package org.lamisplus.datafi.activities.forms.hts.rst;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class RSTPresenter extends LamisBasePresenter implements RSTContract.Presenter {

    private final RSTContract.View rstInfoView;
    private String patientId;

    public RSTPresenter(RSTContract.View rstInfoView, String patientId) {
        this.rstInfoView = rstInfoView;
        this.patientId = patientId;
        this.rstInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public RiskStratification patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findRstFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(RiskStratification riskStratification, String packageName) {
        if (validate(riskStratification)) {
            String rstEncounter = new Gson().toJson(riskStratification);
            rstInfoView.startActivityForClientIntakeForm(rstEncounter, packageName);
        } else {
            rstInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(RiskStratification riskStratification, Encounter encounter) {
        if (validate(riskStratification)) {
            Person person = PersonDAO.findPersonById(patientId);
            if(person != null && person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(riskStratification);
            encounter.setDataValues(s);
            encounter.save();
            rstInfoView.startHTSActivity();
        } else {
            rstInfoView.scrollToTop();
        }
    }

    public boolean validate(RiskStratification riskStratification) {
        boolean dateOfBirth = false;
        boolean entryPointError = false;
        boolean settingsError = false;
        boolean modalityError = false;
        boolean visitDateError = false;
        boolean targetGroupError = false;
        boolean autolastHivTestBasedOnRequest = false;

        rstInfoView.setErrorsVisibility(dateOfBirth, entryPointError, settingsError, modalityError, visitDateError, targetGroupError, autolastHivTestBasedOnRequest);

        if (StringUtils.isBlank(riskStratification.getDob())) {
            dateOfBirth = true;
        }

        if (StringUtils.isBlank(riskStratification.getEntryPoint())) {
            entryPointError = true;
        }

        if (StringUtils.isBlank(riskStratification.getTestingSetting())) {
            settingsError = true;
        }

        if (StringUtils.isBlank(riskStratification.getModality())) {
            modalityError = true;
        }

        if (StringUtils.isBlank(riskStratification.getVisitDate())) {
            visitDateError = true;
        }

        if (StringUtils.isBlank(riskStratification.getTargetGroup())) {
            targetGroupError = true;
        }

        if (StringUtils.isBlank(riskStratification.getRiskAssessment().getLastHivTestBasedOnRequest())) {
            autolastHivTestBasedOnRequest = true;
        }

        boolean result = !dateOfBirth && !entryPointError && !settingsError && !modalityError && !visitDateError && !targetGroupError && !autolastHivTestBasedOnRequest;

        if (result) {
            return true;
        } else {
            rstInfoView.setErrorsVisibility(dateOfBirth, entryPointError, settingsError, modalityError, visitDateError, targetGroupError, autolastHivTestBasedOnRequest);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        rstInfoView.startHTSActivity();
    }

}
