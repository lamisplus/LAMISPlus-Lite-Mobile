package org.lamisplus.datafi.activities.forms.hts.pretest;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeContract;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.utilities.ApplicationConstants;

public class PreTestPresenter extends LamisBasePresenter implements PreTestContract.Presenter {

    private final PreTestContract.View preTestInfoView;
    private String patientId;

    public PreTestPresenter(PreTestContract.View preTestInfoView, String patientId){
        this.preTestInfoView = preTestInfoView;
        this.patientId = patientId;
        this.preTestInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public PreTest patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findPreTestFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(PreTest preTest, String packageName) {
        String clientIntakeEncounter = new Gson().toJson(preTest);
        Encounter encounter = new Encounter();
        encounter.setName(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM);
        encounter.setPerson(patientId);
        encounter.setPackageName(packageName);
        encounter.setDataValues(clientIntakeEncounter);
        encounter.save();

        preTestInfoView.startActivityForRequestResultForm();
    }

    @Override
    public void confirmUpdate(PreTest preTest, Encounter encounter) {
        String s = new Gson().toJson(preTest);
        encounter.setDataValues(s);
        encounter.save();
        preTestInfoView.startActivityForRequestResultForm();
    }

    @Override
    public void confirmDeleteEncounterPreTest(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        preTestInfoView.startDashboardActivity();
    }
}
