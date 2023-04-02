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
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;

public class RequestResultPresenter extends LamisBasePresenter implements RequestResultContract.Presenter {

    private final RequestResultContract.View requestResultInfoView;
    private String patientId;

    public RequestResultPresenter(RequestResultContract.View requestRequestInfoView, String patientId) {
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
        if (validate(requestResult)) {
            String requestResultEncounter = new Gson().toJson(requestResult);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.REQUEST_RESULT_FORM);
            encounter.setPerson(patientId);
            encounter.setPackageName(packageName);
            encounter.setDataValues(requestResultEncounter);
            encounter.save();

            requestResultInfoView.startActivityForPostTestForm();
        } else {
            requestResultInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(RequestResult requestResult, Encounter encounter) {
        if (validate(requestResult)) {
            String s = new Gson().toJson(requestResult);
            encounter.setDataValues(s);
            encounter.save();
            requestResultInfoView.startActivityForPostTestForm();
        } else {
            requestResultInfoView.scrollToTop();
        }
    }

    public boolean validate(RequestResult requestResult) {
        boolean edDateTest1 = false;
        boolean autoResultTest1 = false;
        boolean edConfirmDate1 = false;
        boolean autoConfirmResult1 = false;
        boolean edTieBreaker1 = false;
        boolean autoResultTieBreaker1 = false;
        boolean edDateTest2 = false;
        boolean autoResultTest2 = false;
        boolean edConfirmDate2 = false;
        boolean autoConfirmResult2 = false;
        boolean edDateTieBreaker2 = false;
        boolean autoTieBreakerResult2 = false;

        requestResultInfoView.setErrorsVisibility(edDateTest1, autoResultTest1, edConfirmDate1, autoConfirmResult1, edTieBreaker1, autoResultTieBreaker1, edDateTest2, autoResultTest2, edConfirmDate2, autoConfirmResult2, edDateTieBreaker2, autoTieBreakerResult2);

        if (StringUtils.isBlank(requestResult.getTest1().getDate())) {
            edDateTest1 = true;
        }

        if (StringUtils.isBlank(requestResult.getTest1().getResult())) {
            autoResultTest1 = true;
        }

        if (!StringUtils.isBlank(requestResult.getTest1().getResult()) && requestResult.getTest1().getResult().equals("Reactive")) {
            if (StringUtils.isBlank(requestResult.getConfirmatoryTest().getDate())) {
                edConfirmDate1 = true;
            }

            if (StringUtils.isBlank(requestResult.getConfirmatoryTest().getResult())) {
                autoConfirmResult1 = true;
            }
        }

        if (!StringUtils.isBlank(requestResult.getConfirmatoryTest().getResult()) && requestResult.getConfirmatoryTest().getResult().equals("Non Reactive")) {
            if (StringUtils.isBlank(requestResult.getTieBreakerTest().getDate())) {
                edTieBreaker1 = true;
            }

            if (StringUtils.isBlank(requestResult.getTieBreakerTest().getResult())) {
                autoResultTieBreaker1 = true;
            }
        }

        if ((!StringUtils.isBlank(requestResult.getConfirmatoryTest().getResult()) && requestResult.getConfirmatoryTest().getResult().equals("Reactive")) || (!StringUtils.isBlank(requestResult.getTieBreakerTest().getResult()) && requestResult.getTieBreakerTest().getResult().equals("Reactive"))) {
            if (StringUtils.isBlank(requestResult.getTest2().getDate2())) {
                edDateTest2 = true;
            }

            if (StringUtils.isBlank(requestResult.getTest2().getResult2())) {
                autoResultTest2 = true;
            }

            if (!StringUtils.isBlank(requestResult.getTest2().getResult2()) && requestResult.getTest2().getResult2().equals("Reactive")) {
                if (StringUtils.isBlank(requestResult.getConfirmatoryTest2().getDate2())) {
                    edConfirmDate2 = true;
                }

                if (StringUtils.isBlank(requestResult.getConfirmatoryTest2().getResult2())) {
                    autoConfirmResult2 = true;
                }
            }

            if (!StringUtils.isBlank(requestResult.getConfirmatoryTest2().getResult2()) && requestResult.getConfirmatoryTest2().getResult2().equals("Non Reactive")) {
                if (StringUtils.isBlank(requestResult.getTieBreakerTest2().getDate2())) {
                    edDateTieBreaker2 = true;
                }

                if (StringUtils.isBlank(requestResult.getTieBreakerTest2().getResult2())) {
                    autoTieBreakerResult2 = true;
                }
            }

        }

        boolean result = !edDateTest1 && !autoResultTest1 && !edConfirmDate1 && !autoConfirmResult1 && !edTieBreaker1 && !autoResultTieBreaker1 && !edDateTest2 && !autoResultTest2 && !edConfirmDate2 && !autoConfirmResult2 && !edDateTieBreaker2 && !autoTieBreakerResult2;

        if (result) {
            return true;
        } else {
            requestResultInfoView.setErrorsVisibility(edDateTest1, autoResultTest1, edConfirmDate1, autoConfirmResult1, edTieBreaker1, autoResultTieBreaker1, edDateTest2, autoResultTest2, edConfirmDate2, autoConfirmResult2, edDateTieBreaker2, autoTieBreakerResult2);
            return false;
        }

    }

    @Override
    public void confirmDeleteEncounterRequestResult(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        requestResultInfoView.startDashboardActivity();
    }
}

