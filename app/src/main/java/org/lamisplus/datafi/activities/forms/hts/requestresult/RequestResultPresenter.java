package org.lamisplus.datafi.activities.forms.hts.requestresult;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestContract;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.RequestResult;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class RequestResultPresenter extends LamisBasePresenter implements RequestResultContract.Presenter {

    private final RequestResultContract.View requestResultInfoView;
    private String patientId;

    public RequestResultPresenter(RequestResultContract.View requestRequestInfoView, String patientId){
        this.requestResultInfoView = requestRequestInfoView;
        this.patientId = patientId;
        this.requestResultInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public RequestResult patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findRequestResultFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(RequestResult requestResult, String packageName) {
        String requestResultEncounter = new Gson().toJson(requestResult);
        Encounter encounter = new Encounter();
        encounter.setName(ApplicationConstants.Forms.REQUEST_RESULT_FORM);
        encounter.setPerson(patientId);
        encounter.setPackageName(packageName);
        encounter.setDataValues(requestResultEncounter);
        encounter.save();

        requestResultInfoView.startActivityForPostTestForm();
    }

    @Override
    public void confirmUpdate(RequestResult requestResult, Encounter encounter) {
        String s = new Gson().toJson(requestResult);
        encounter.setDataValues(s);
        encounter.save();
        requestResultInfoView.startActivityForPostTestForm();
    }

    @Override
    public void confirmDeleteEncounterRequestResult(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        requestResultInfoView.startDashboardActivity();
    }
}

