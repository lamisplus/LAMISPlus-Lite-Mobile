package org.lamisplus.datafi.activities.forms.pmtct.pmtctenrollment;

import android.util.Log;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;

public class PMTCTEnrollmentPresenter extends LamisBasePresenter implements PMTCTEnrollmentContract.Presenter {

    private final PMTCTEnrollmentContract.View pmtctInfoView;
    private String patientId;

    public PMTCTEnrollmentPresenter(PMTCTEnrollmentContract.View pmtctInfoView, String patientId) {
        this.pmtctInfoView = pmtctInfoView;
        this.patientId = patientId;
        this.pmtctInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public PMTCTEnrollment patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findPMTCTEnrollmentFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(PMTCTEnrollment pmtctEnrollment, String packageName) {
        if (validate(pmtctEnrollment)) {
            String pmtctEncounter = new Gson().toJson(pmtctEnrollment);
            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.PMTCT_ENROLLMENT_FORM);
            encounter.setPerson(String.valueOf(patientId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(pmtctEncounter);
            encounter.save();

            pmtctInfoView.startPMTCTServicesActivity();
        } else {
            pmtctInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(PMTCTEnrollment pmtctEnrollment, Encounter encounter) {
        if (validate(pmtctEnrollment)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(pmtctEnrollment);
            encounter.setDataValues(s);
            encounter.save();
            pmtctInfoView.startPMTCTServicesActivity();
        } else {
            pmtctInfoView.scrollToTop();
        }
    }

    public boolean validate(PMTCTEnrollment pmtctEnrollment) {
        boolean dateEnrollment = false;
        boolean entryPoint = false;
        boolean artStartDate = false;
        boolean timingARTInitiation = false;
        boolean tBStatus = false;

        pmtctInfoView.setErrorsVisibility(dateEnrollment, entryPoint, artStartDate, timingARTInitiation, tBStatus);

        if (StringUtils.isBlank(pmtctEnrollment.getPmtctEnrollmentDate())) {
            dateEnrollment = true;
        }

        if (StringUtils.isBlank(pmtctEnrollment.getEntryPoint())) {
            entryPoint = true;
        }

        if (StringUtils.isBlank(pmtctEnrollment.getArtStartDate())) {
            artStartDate = true;
        }

        if (StringUtils.isBlank(pmtctEnrollment.getArtStartTime())) {
            timingARTInitiation = true;
        }

        if (StringUtils.isBlank(pmtctEnrollment.getTbStatus())) {
            tBStatus = true;
        }

        boolean result = !dateEnrollment && !entryPoint && !artStartDate && !timingARTInitiation && !tBStatus;

        if (result) {
            return true;
        } else {
            pmtctInfoView.setErrorsVisibility(dateEnrollment, entryPoint, artStartDate, timingARTInitiation, tBStatus);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        //EncounterDAO.deleteEncounter(formName, patientId);
        pmtctInfoView.startPMTCTServicesActivity();
    }

}
