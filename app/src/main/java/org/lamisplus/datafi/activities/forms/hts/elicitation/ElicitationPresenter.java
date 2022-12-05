package org.lamisplus.datafi.activities.forms.hts.elicitation;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Elicitation;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class ElicitationPresenter extends LamisBasePresenter implements ElicitationContract.Presenter {

    private final ElicitationContract.View elicitationInfoView;
    private String patientId;

    public ElicitationPresenter(ElicitationContract.View elicitationInfoView, String patientId){
        this.elicitationInfoView = elicitationInfoView;
        this.patientId = patientId;
        this.elicitationInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public Elicitation patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findElicitationFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(Elicitation elicitation, String packageName) {
        String clientIntakeEncounter = new Gson().toJson(elicitation);
        Encounter encounter = new Encounter();
        encounter.setName(ApplicationConstants.Forms.ELICITATION);
        encounter.setPerson(patientId);
        encounter.setPackageName(packageName);
        encounter.setDataValues(clientIntakeEncounter);
        encounter.save();

        elicitationInfoView.startDashboardActivity();
    }

    @Override
    public void confirmUpdate(Elicitation elicitation, Encounter encounter) {
        String s = new Gson().toJson(elicitation);
        encounter.setDataValues(s);
        encounter.save();
        elicitationInfoView.startDashboardActivity();
    }

    @Override
    public void confirmDeleteEncounterElicitation(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        elicitationInfoView.startDashboardActivity();
    }
}
