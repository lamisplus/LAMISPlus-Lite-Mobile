package org.lamisplus.datafi.activities.forms.hts.recency;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestContract;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RecencyPresenter extends LamisBasePresenter implements RecencyContract.Presenter {

    private final RecencyContract.View recencyInfoView;
    private String patientId;

    public RecencyPresenter(RecencyContract.View recencyInfoView, String patientId){
        this.recencyInfoView = recencyInfoView;
        this.patientId = patientId;
        this.recencyInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public Recency patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findRecencyFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(Recency recency, String packageName) {
        String recencyEncounter = new Gson().toJson(recency);
        Encounter encounter = new Encounter();
        encounter.setName(ApplicationConstants.Forms.HIV_RECENCY_FORM);
        encounter.setPerson(patientId);
        encounter.setPackageName(packageName);
        encounter.setDataValues(recencyEncounter);
        encounter.save();

        recencyInfoView.startActivityForElicitation();
    }

    @Override
    public void confirmUpdate(Recency recency, Encounter encounter) {
        String s = new Gson().toJson(recency);
        encounter.setDataValues(s);
        encounter.save();
        recencyInfoView.startActivityForElicitation();
    }

    @Override
    public void confirmDeleteEncounterRecency(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        recencyInfoView.startDashboardActivity();
    }
}
