package org.lamisplus.datafi.activities.forms.hts.rst;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RSTPresenter extends LamisBasePresenter implements RSTContract.Presenter {

    private final RSTContract.View rstInfoView;
    private String patientId;

    public RSTPresenter(RSTContract.View rstInfoView, String patientId){
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
    public void confirmCreate(RiskStratification riskStratification) {
        String rstEncounter = new Gson().toJson(riskStratification);
        Encounter encounter = new Encounter();
        encounter.setName(ApplicationConstants.Forms.RISK_STRATIFICATION_FORM);
        encounter.setPersonId(patientId);
        encounter.setDataValues(rstEncounter);
        encounter.save();

        rstInfoView.startActivityForClientIntakeForm();
    }

    @Override
    public void confirmUpdate(RiskStratification riskStratification, Encounter encounter) {
        String s = new Gson().toJson(riskStratification);
        encounter.setDataValues(s);
        encounter.save();
        rstInfoView.startActivityForClientIntakeForm();
    }

}
