package org.lamisplus.datafi.activities.forms.hts.recency;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.forms.hts.pretest.PreTestContract;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PreTest;
import org.lamisplus.datafi.models.Recency;
import org.lamisplus.datafi.models.RiskStratification;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class RecencyPresenter extends LamisBasePresenter implements RecencyContract.Presenter {

    private final RecencyContract.View recencyInfoView;
    private String patientId;

    public RecencyPresenter(RecencyContract.View recencyInfoView, String patientId) {
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
        if (validate(recency)) {
            String recencyEncounter = new Gson().toJson(recency);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.HIV_RECENCY_FORM);
            encounter.setPerson(patientId);
            encounter.setPackageName(packageName);
            encounter.setDataValues(recencyEncounter);
            encounter.save();

            recencyInfoView.startActivityForElicitation();
        } else {
            recencyInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(Recency recency, Encounter encounter) {
        if (validate(recency)) {
            String s = new Gson().toJson(recency);
            encounter.setDataValues(s);
            encounter.save();
            recencyInfoView.startActivityForElicitation();
        } else {
            recencyInfoView.scrollToTop();
        }
    }

    public boolean validate(Recency recency) {
        boolean optOutRTRI = false;
        boolean testName = false;
        boolean testDate = false;
        boolean rencencyId = false;
        boolean controlLine = false;
        boolean verififcationLine = false;
        boolean longTermLine = false;
        boolean hasViralLoad = false;
        boolean sampleReferanceNumber = false;
        boolean sampleType = false;

        recencyInfoView.setErrorsVisibility(optOutRTRI, testName, testDate, rencencyId, controlLine, verififcationLine, longTermLine, hasViralLoad, sampleReferanceNumber, sampleType);

        if (StringUtils.isBlank(recency.getRecencyDetails().getOptOutRTRI())) {
            optOutRTRI = true;
        } else {

            if (recency.getRecencyDetails().getOptOutRTRI().equals("false")) {

                if (StringUtils.isBlank(recency.getRecencyDetails().getOptOutRTRITestName())) {
                    testName = true;
                }

                if (StringUtils.isBlank(recency.getRecencyDetails().getOptOutRTRITestDate())) {
                    testDate = true;
                }

                if (StringUtils.isBlank(recency.getRecencyDetails().getRencencyId())) {
                    rencencyId = true;
                }

                if (StringUtils.isBlank(recency.getRecencyDetails().getControlLine())) {
                    controlLine = true;
                }

                if (StringUtils.isBlank(recency.getRecencyDetails().getVerififcationLine())) {
                    verififcationLine = true;
                }

                if (StringUtils.isBlank(recency.getRecencyDetails().getLongTermLine())) {
                    longTermLine = true;
                }

                if (recency.getRecencyDetails().getRencencyInterpretation() != null) {
                    if (recency.getRecencyDetails().getRencencyInterpretation().equals("recent")) {
                        if (StringUtils.isBlank(recency.getRecencyDetails().getHasViralLoad())) {
                            hasViralLoad = true;
                        }
                    }
                }

                if (recency.getRecencyDetails().getHasViralLoad() != null) {
                    if (recency.getRecencyDetails().getHasViralLoad().equals("true")) {
                        if (StringUtils.isBlank(recency.getRecencyDetails().getSampleReferanceNumber())) {
                            sampleReferanceNumber = true;
                        }

                        if (StringUtils.isBlank(recency.getRecencyDetails().getSampleType())) {
                            sampleType = true;
                        }
                    }
                }

            }
        }

        boolean result = !optOutRTRI && !testName && !testDate && !rencencyId && !controlLine && !verififcationLine && !longTermLine && !hasViralLoad && !sampleReferanceNumber && !sampleType;

        if (result) {
            return true;
        } else {
            recencyInfoView.setErrorsVisibility(optOutRTRI, testName, testDate, rencencyId, controlLine, verififcationLine, longTermLine, hasViralLoad, sampleReferanceNumber, sampleType);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounterRecency(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        recencyInfoView.startDashboardActivity();
    }
}
