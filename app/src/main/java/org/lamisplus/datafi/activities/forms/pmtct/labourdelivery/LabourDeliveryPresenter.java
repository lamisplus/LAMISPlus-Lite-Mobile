package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

import com.google.gson.Gson;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;

public class LabourDeliveryPresenter extends LamisBasePresenter implements LabourDeliveryContract.Presenter {

    private final LabourDeliveryContract.View pmtctInfoView;
    private String patientId;

    public LabourDeliveryPresenter(LabourDeliveryContract.View pmtctInfoView, String patientId) {
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
    public LabourDelivery patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findLabourDeliveryFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(LabourDelivery labourDelivery, String packageName) {
//        if (validate(anc)) {
//            String pmtctEncounter = new Gson().toJson(anc);
//            pmtctInfoView.startActivityForClientIntakeForm(pmtctEncounter, packageName);
//        } else {
//            pmtctInfoView.scrollToTop();
//        }
    }

    @Override
    public void confirmUpdate(LabourDelivery labourDelivery, Encounter encounter) {
        if (validate(labourDelivery)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(labourDelivery);
            encounter.setDataValues(s);
            encounter.save();
            pmtctInfoView.startPMTCTEnrollmentActivity();
        } else {
            pmtctInfoView.scrollToTop();
        }
    }

    public boolean validate(LabourDelivery labourDelivery) {

        return false;
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        //EncounterDAO.deleteEncounter(formName, patientId);
        pmtctInfoView.startPMTCTEnrollmentActivity();
    }

}
