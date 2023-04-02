package org.lamisplus.datafi.activities.forms.hts.pretest;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.forms.hts.clientintake.ClientIntakeContract;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.ClientIntake;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;
import org.lamisplus.datafi.utilities.ToastUtil;

public class PreTestPresenter extends LamisBasePresenter implements PreTestContract.Presenter {

    private final PreTestContract.View preTestInfoView;
    private String patientId;

    public PreTestPresenter(PreTestContract.View preTestInfoView, String patientId) {
        this.preTestInfoView = preTestInfoView;
        this.patientId = patientId;
        this.preTestInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        preTestInfoView.hideFieldsDefault(patientId);
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
        if (validate(preTest)) {
            String clientIntakeEncounter = new Gson().toJson(preTest);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.PRE_TEST_COUNSELING_FORM);
            encounter.setPerson(patientId);
            encounter.setPackageName(packageName);
            encounter.setDataValues(clientIntakeEncounter);
            encounter.save();

            preTestInfoView.startActivityForRequestResultForm();
        } else {
            preTestInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(PreTest preTest, Encounter encounter) {
        if (validate(preTest)) {
            String s = new Gson().toJson(preTest);
            encounter.setDataValues(s);
            encounter.save();
            preTestInfoView.startActivityForRequestResultForm();
        } else {
            preTestInfoView.scrollToTop();
        }
    }

    public boolean validate(PreTest preTest) {
        Gson gson = new Gson();
        String s = gson.toJson(preTest);

        ClientIntake clientIntake = EncounterDAO.findClientIntakeFromForm(ApplicationConstants.Forms.CLIENT_INTAKE_FORM, patientId);

        if (CodesetsDAO.findCodesetsDisplayByCode(clientIntake.getTargetGroup()).equals("Gen Pop")) {
            boolean everHadSexualIntercourse = false;
            boolean bloodtransInlastThreeMonths = false;
            boolean uprotectedSexWithCasualLastThreeMonths = false;
            boolean uprotectedSexWithRegularPartnerLastThreeMonths = false;
            boolean autounprotectedVaginalSex = false;
            boolean uprotectedAnalSexHivRiskAssess = false;
            boolean stiLastThreeMonths = false;
            boolean sexUnderInfluence = false;
            boolean moreThanOneSexPartnerLastThreeMonths = false;

            preTestInfoView.setErrorsVisibilityGenPop(everHadSexualIntercourse, bloodtransInlastThreeMonths, uprotectedSexWithCasualLastThreeMonths, uprotectedSexWithRegularPartnerLastThreeMonths,
                    autounprotectedVaginalSex, uprotectedAnalSexHivRiskAssess, stiLastThreeMonths, sexUnderInfluence, moreThanOneSexPartnerLastThreeMonths);

            if (StringUtils.isBlank(preTest.getRiskAssessment().isEverHadSexualIntercourse())) {
                everHadSexualIntercourse = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isBloodtransInlastThreeMonths())) {
                bloodtransInlastThreeMonths = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isUprotectedSexWithCasualLastThreeMonths())) {
                uprotectedSexWithCasualLastThreeMonths = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isUprotectedSexWithRegularPartnerLastThreeMonths())) {
                uprotectedSexWithRegularPartnerLastThreeMonths = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isUnprotectedVaginalSex())) {
                autounprotectedVaginalSex = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isUprotectedAnalSex())) {
                uprotectedAnalSexHivRiskAssess = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isStiLastThreeMonths())) {
                stiLastThreeMonths = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isSexUnderInfluence())) {
                sexUnderInfluence = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isMoreThanOneSexPartnerLastThreeMonths())) {
                moreThanOneSexPartnerLastThreeMonths = true;
            }

            boolean result = !everHadSexualIntercourse && !bloodtransInlastThreeMonths && !uprotectedSexWithCasualLastThreeMonths && !uprotectedSexWithRegularPartnerLastThreeMonths && !autounprotectedVaginalSex
                    && !uprotectedAnalSexHivRiskAssess && !stiLastThreeMonths && !sexUnderInfluence && !moreThanOneSexPartnerLastThreeMonths;

            if (result) {
                return true;
            } else {
                preTestInfoView.setErrorsVisibilityGenPop(everHadSexualIntercourse, bloodtransInlastThreeMonths, uprotectedSexWithCasualLastThreeMonths, uprotectedSexWithRegularPartnerLastThreeMonths,
                        autounprotectedVaginalSex, uprotectedAnalSexHivRiskAssess, stiLastThreeMonths, sexUnderInfluence, moreThanOneSexPartnerLastThreeMonths);
                return false;
            }
        } else {
            boolean experiencePain = false;
            boolean haveSexWithoutCondom = false;
            boolean haveCondomBurst = false;
            boolean abuseDrug = false;
            boolean bloodTransfusion = false;
            boolean consistentWeightFeverNightCough = false;
            boolean soldPaidVaginalSex = false;

            preTestInfoView.setErrorsVisibilityOthers(experiencePain, haveSexWithoutCondom, haveCondomBurst, abuseDrug, bloodTransfusion, consistentWeightFeverNightCough, soldPaidVaginalSex);

            if (StringUtils.isBlank(preTest.getRiskAssessment().isExperiencePain())) {
                experiencePain = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isHaveSexWithoutCondom())) {
                haveSexWithoutCondom = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isHaveCondomBurst())) {
                haveCondomBurst = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isAbuseDrug())) {
                abuseDrug = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isBloodTransfusion())) {
                bloodTransfusion = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isConsistentWeightFeverNightCough())) {
                consistentWeightFeverNightCough = true;
            }

            if (StringUtils.isBlank(preTest.getRiskAssessment().isSoldPaidVaginalSex())) {
                soldPaidVaginalSex = true;
            }

            boolean result = !experiencePain && !haveSexWithoutCondom && !haveCondomBurst && !abuseDrug && !bloodTransfusion && !consistentWeightFeverNightCough && !soldPaidVaginalSex;

            if (result) {
                return true;
            } else {
                preTestInfoView.setErrorsVisibilityOthers(experiencePain, haveSexWithoutCondom, haveCondomBurst, abuseDrug, bloodTransfusion, consistentWeightFeverNightCough, soldPaidVaginalSex);
                return false;
            }
        }

    }


    @Override
    public void confirmDeleteEncounterPreTest(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        preTestInfoView.startDashboardActivity();
    }
}
