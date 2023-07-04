package org.lamisplus.datafi.activities.forms.hts.posttest;

import android.util.Log;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeContract;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.models.PostTest;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class PostTestPresenter extends LamisBasePresenter implements PostTestContract.Presenter {

    private final PostTestContract.View postTestInfoView;
    private String patientId;

    public PostTestPresenter(PostTestContract.View postTestInfoView, String patientId){
        this.postTestInfoView = postTestInfoView;
        this.patientId = patientId;
        this.postTestInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public PostTest patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findPostTestFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(PostTest postTest, String packageName) {
        if(validate(postTest)) {
            String postTestEncounter = new Gson().toJson(postTest);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.POST_TEST_COUNSELING_FORM);
            encounter.setPerson(patientId);
            Person person = PersonDAO.findPersonById(patientId);
            if(person != null && person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            encounter.setPackageName(packageName);
            encounter.setDataValues(postTestEncounter);
            encounter.save();
            if(postTest.getPostTestCounselingKnowledgeAssessment().getHivTestResult().equals("true")) {
                postTestInfoView.startActivityForRecencyForm();
            }else{
                postTestInfoView.startDashboardActivity();
            }
        }else {
            postTestInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(PostTest postTest, Encounter encounter) {
        if(validate(postTest)) {
            String s = new Gson().toJson(postTest);
            encounter.setDataValues(s);
            encounter.save();
            if(postTest.getPostTestCounselingKnowledgeAssessment().getHivTestResult().equals("true")) {
                postTestInfoView.startActivityForRecencyForm();
            }else{
                postTestInfoView.startDashboardActivity();
            }
        }else{
            postTestInfoView.scrollToTop();
        }
    }

    public boolean validate(PostTest postTest) {
        boolean hivTestResultError = false;

        postTestInfoView.setErrorsVisibility(hivTestResultError);

        if (StringUtils.isBlank(postTest.getPostTestCounselingKnowledgeAssessment().getHivTestResult())) {
            hivTestResultError = true;
        }

        boolean result = !hivTestResultError;

        if (result) {
            return true;
        } else {
            postTestInfoView.setErrorsVisibility(hivTestResultError);
            return false;
        }

    }

    @Override
    public void confirmDeleteEncounterPostTest(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        postTestInfoView.startDashboardActivity();
    }
}
