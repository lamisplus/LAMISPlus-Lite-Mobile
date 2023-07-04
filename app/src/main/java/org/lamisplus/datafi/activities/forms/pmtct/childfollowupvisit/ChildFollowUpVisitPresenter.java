package org.lamisplus.datafi.activities.forms.pmtct.childfollowupvisit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.ChildFollowupVisit;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class ChildFollowUpVisitPresenter extends LamisBasePresenter implements ChildFollowUpVisitContract.Presenter {

    private final ChildFollowUpVisitContract.View childFollowupInfoView;
    private String patientId;

    public ChildFollowUpVisitPresenter(ChildFollowUpVisitContract.View childFollowupInfoView, String patientId) {
        this.childFollowupInfoView = childFollowupInfoView;
        this.patientId = patientId;
        this.childFollowupInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public ChildFollowupVisit patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findChildFollowUpVisitFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(ChildFollowupVisit childFollowupVisit, String packageName) {
        if (validate(childFollowupVisit)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String motherFollowupVisitEncounter = gson.toJson(childFollowupVisit);

            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.CHILD_FOLLOW_UP_VISIT_FORM);
            encounter.setPerson(String.valueOf(patientId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(motherFollowupVisitEncounter);
            encounter.save();

            childFollowupInfoView.startPMTCTServicesActivity();
        } else {
            childFollowupInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(ChildFollowupVisit childFollowupVisit, Encounter encounter) {
        if (validate(childFollowupVisit)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(childFollowupVisit);
            encounter.setDataValues(s);
            encounter.save();
            childFollowupInfoView.startPMTCTServicesActivity();
        } else {
            childFollowupInfoView.scrollToTop();
        }
    }

    public boolean validate(ChildFollowupVisit childFollowupVisit) {
        boolean edDateofVisit = false;
        boolean infantHospitalNo = false;
        boolean bodyWeight = false;

        childFollowupInfoView.setErrorsVisibility(edDateofVisit, infantHospitalNo, bodyWeight);

        if (StringUtils.isBlank(childFollowupVisit.getInfantVisitRequestDto().getVisitDate())) {
            edDateofVisit = true;
        }

        if (StringUtils.isBlank(childFollowupVisit.getInfantVisitRequestDto().getInfantHospitalNumber())) {
            infantHospitalNo = true;
        }

        if (StringUtils.isBlank(childFollowupVisit.getInfantVisitRequestDto().getBodyWeight())) {
            bodyWeight = true;
        }

        boolean result = !edDateofVisit && !infantHospitalNo && !bodyWeight;

        if (result) {
            return true;
        } else {
            childFollowupInfoView.setErrorsVisibility(edDateofVisit, infantHospitalNo, bodyWeight);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        childFollowupInfoView.startPMTCTServicesActivity();
    }

}
