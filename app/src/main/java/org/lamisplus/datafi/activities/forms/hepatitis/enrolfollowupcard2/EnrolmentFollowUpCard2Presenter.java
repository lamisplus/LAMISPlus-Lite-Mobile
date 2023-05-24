package org.lamisplus.datafi.activities.forms.hepatitis.enrolfollowupcard2;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;

public class EnrolmentFollowUpCard2Presenter extends LamisBasePresenter implements EnrolmentFollowUpCard2Contract.Presenter {

    private final EnrolmentFollowUpCard2Contract.View pmtctInfoView;
    private String patientId;

    public EnrolmentFollowUpCard2Presenter(EnrolmentFollowUpCard2Contract.View pmtctInfoView, String patientId) {
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
//        if (validate(anc)) {
//            String pmtctEncounter = new Gson().toJson(anc);
//            pmtctInfoView.startActivityForClientIntakeForm(pmtctEncounter, packageName);
//        } else {
//            pmtctInfoView.scrollToTop();
//        }
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
            pmtctInfoView.startPMTCTEnrollmentActivity();
        } else {
            pmtctInfoView.scrollToTop();
        }
    }

    public boolean validate(PMTCTEnrollment pmtctEnrollment) {

        return false;
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        //EncounterDAO.deleteEncounter(formName, patientId);
        pmtctInfoView.startPMTCTEnrollmentActivity();
    }

}
