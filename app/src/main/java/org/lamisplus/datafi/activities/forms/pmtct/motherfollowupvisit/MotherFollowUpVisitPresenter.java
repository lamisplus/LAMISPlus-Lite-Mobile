package org.lamisplus.datafi.activities.forms.pmtct.motherfollowupvisit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.MotherFollowupVisit;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class MotherFollowUpVisitPresenter extends LamisBasePresenter implements MotherFollowUpVisitContract.Presenter {

    private final MotherFollowUpVisitContract.View motherFollowupInfoView;
    private String patientId;

    public MotherFollowUpVisitPresenter(MotherFollowUpVisitContract.View motherFollowupInfoView, String patientId) {
        this.motherFollowupInfoView = motherFollowupInfoView;
        this.patientId = patientId;
        this.motherFollowupInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public MotherFollowupVisit patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findMotherFollowUpVisitFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(MotherFollowupVisit motherFollowupVisit, String packageName) {
        if (validate(motherFollowupVisit)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String motherFollowupVisitEncounter = gson.toJson(motherFollowupVisit);

            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.MOTHER_FOLLOW_UP_VISIT_FORM);
            encounter.setPerson(String.valueOf(patientId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(motherFollowupVisitEncounter);
            encounter.save();

            motherFollowupInfoView.startPMTCTServicesActivity();
        } else {
            motherFollowupInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(MotherFollowupVisit motherFollowupVisit, Encounter encounter) {
        if (validate(motherFollowupVisit)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(motherFollowupVisit);
            encounter.setDataValues(s);
            encounter.save();
            motherFollowupInfoView.startPMTCTServicesActivity();
        } else {
            motherFollowupInfoView.scrollToTop();
        }
    }

    public boolean validate(MotherFollowupVisit motherFollowupVisit) {
        boolean edDateofVisit = false;
        boolean entryPoint = false;
        boolean fpCounselling = false;
        boolean dsd = false;
        boolean maternalOutcome = false;
        boolean dateOutcome = false;
        boolean clientStatusVisit = false;

        motherFollowupInfoView.setErrorsVisibility(edDateofVisit, entryPoint, fpCounselling, dsd, maternalOutcome, dateOutcome, clientStatusVisit);

        if (StringUtils.isBlank(motherFollowupVisit.getDateOfVisit())) {
            edDateofVisit = true;
        }

        if (StringUtils.isBlank(motherFollowupVisit.getEnteryPoint())) {
            entryPoint = true;
        }

        if (StringUtils.isBlank(motherFollowupVisit.getFpCounseling())) {
            fpCounselling = true;
        }

        if (StringUtils.isBlank(motherFollowupVisit.getDsd())) {
            dsd = true;
        }

        if (StringUtils.isBlank(motherFollowupVisit.getMaternalOutcome())) {
            maternalOutcome = true;
        }

        if (StringUtils.isBlank(motherFollowupVisit.getDateOfmeternalOutcome())) {
            dateOutcome = true;
        }

        if (StringUtils.isBlank(motherFollowupVisit.getVisitStatus())) {
            clientStatusVisit = true;
        }

        boolean result = !edDateofVisit && !entryPoint && !fpCounselling && !dsd & !maternalOutcome && !dateOutcome && !clientStatusVisit;

        if (result) {
            return true;
        } else {
            motherFollowupInfoView.setErrorsVisibility(edDateofVisit, entryPoint, fpCounselling, dsd, maternalOutcome, dateOutcome, clientStatusVisit);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        motherFollowupInfoView.startPMTCTServicesActivity();
    }

}
