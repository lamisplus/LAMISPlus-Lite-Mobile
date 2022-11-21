package org.lamisplus.datafi.activities.forms.hts.clientintake;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class ClientIntakePresenter extends LamisBasePresenter implements ClientIntakeContract.Presenter {

    private final ClientIntakeContract.View clientIntakeInfoView;
    private String patientId;

    public ClientIntakePresenter(ClientIntakeContract.View clientIntakeInfoView, String patientId){
        this.clientIntakeInfoView = clientIntakeInfoView;
        this.patientId = patientId;
        this.clientIntakeInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public ClientIntake patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findClientIntakeFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(ClientIntake clientIntake) {
        String clientIntakeEncounter = new Gson().toJson(clientIntake);
        Encounter encounter = new Encounter();
        encounter.setName(ApplicationConstants.Forms.CLIENT_INTAKE_FORM);
        encounter.setPersonId(patientId);
        encounter.setDataValues(clientIntakeEncounter);
        encounter.save();

        clientIntakeInfoView.startActivityForPreTestForm();
    }

    @Override
    public void confirmUpdate(ClientIntake clientIntake, Encounter encounter) {
        String s = new Gson().toJson(clientIntake);
        encounter.setDataValues(s);
        encounter.save();
        clientIntakeInfoView.startActivityForPreTestForm();
    }

}
