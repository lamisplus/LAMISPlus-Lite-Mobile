package org.lamisplus.datafi.activities.forms.pmtct.labourdelivery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.CodesetsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.LabourDelivery;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;
import org.lamisplus.datafi.utilities.StringUtils;

public class LabourDeliveryPresenter extends LamisBasePresenter implements LabourDeliveryContract.Presenter {

    private final LabourDeliveryContract.View labourDeliveryInfoView;
    private String patientId;

    public LabourDeliveryPresenter(LabourDeliveryContract.View labourDeliveryInfoView, String patientId) {
        this.labourDeliveryInfoView = labourDeliveryInfoView;
        this.patientId = patientId;
        this.labourDeliveryInfoView.setPresenter(this);
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
        if (validate(labourDelivery)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String labourDeliveryEncounter = gson.toJson(labourDelivery);

            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.LABOUR_DELIVERY_FORM);
            encounter.setPerson(String.valueOf(patientId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(labourDeliveryEncounter);
            encounter.save();

            labourDeliveryInfoView.startPMTCTServicesActivity();
        } else {
            labourDeliveryInfoView.scrollToTop();
        }
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
            labourDeliveryInfoView.startPMTCTServicesActivity();
        } else {
            labourDeliveryInfoView.scrollToTop();
        }
    }

    public boolean validate(LabourDelivery labourDelivery) {

        boolean bookingStatus = false;
        boolean dateOfDelivery = false;
        boolean romDelivery = false;
        boolean modeOfDelivery = false;
        boolean episiotomy = false;
        boolean vaginalTear = false;
        boolean feedingDecision = false;
        boolean childGivenARVwithin72hrs = false;
        boolean onArt = false;
        boolean hivExposedInfant24hrs = false;
        boolean timeDelivery = false;
        boolean artStartedLDWard = false;
        boolean hbStatus = false;
        boolean hcStatus = false;
        boolean maternalOutcome = false;
        boolean childStatus = false;
        boolean noChildAlive = false;
        boolean noChildDead = false;

        labourDeliveryInfoView.setErrorsVisibility(bookingStatus, dateOfDelivery, romDelivery, modeOfDelivery, episiotomy, vaginalTear, feedingDecision, childGivenARVwithin72hrs, onArt, hivExposedInfant24hrs, timeDelivery, artStartedLDWard, hbStatus, hcStatus, maternalOutcome, childStatus, noChildAlive, noChildDead);

        if (StringUtils.isBlank(labourDelivery.getBookingStatus())) {
            bookingStatus = true;
        }

        if (StringUtils.isBlank(labourDelivery.getDateOfDelivery())) {
            dateOfDelivery = true;
        }

        if (StringUtils.isBlank(labourDelivery.getRomDeliveryInterval())) {
            romDelivery = true;
        }

        if (StringUtils.isBlank(labourDelivery.getModeOfDelivery())) {
            modeOfDelivery = true;
        }

        if (StringUtils.isBlank(labourDelivery.getEpisiotomy())) {
            episiotomy = true;
        }

        if (StringUtils.isBlank(labourDelivery.getVaginalTear())) {
            vaginalTear = true;
        }

        if (StringUtils.isBlank(labourDelivery.getFeedingDecision())) {
            feedingDecision = true;
        }

        if (StringUtils.isBlank(labourDelivery.getChildGivenArvWithin72())) {
            childGivenARVwithin72hrs = true;
        }

        if (StringUtils.isBlank(labourDelivery.getOnArt())) {
            onArt = true;
        }

        if (StringUtils.isBlank(labourDelivery.getHivExposedInfantGivenHbWithin24hrs())) {
            hivExposedInfant24hrs = true;
        }

        if (StringUtils.isBlank(labourDelivery.getDeliveryTime())) {
            timeDelivery = true;
        }

        if (StringUtils.isBlank(labourDelivery.getArtStartedLdWard())) {
            artStartedLDWard = true;
        }

        if (StringUtils.isBlank(labourDelivery.getHbstatus())) {
            hbStatus = true;
        }

        if (StringUtils.isBlank(labourDelivery.getHcstatus())) {
            hcStatus = true;
        }

        if (StringUtils.isBlank(labourDelivery.getMaternalOutcome())) {
            maternalOutcome = true;
        }

        if (StringUtils.isBlank(labourDelivery.getChildStatus())) {
            childStatus = true;
        }

        if (labourDelivery.getChildStatus() != null && CodesetsDAO.findCodesetsDisplayByCode(labourDelivery.getChildStatus()).equals("Alive")) {
            if (labourDelivery.getNumberOfInfantsAlive() == null) {
                noChildAlive = true;
            }

            if (labourDelivery.getNumberOfInfantsDead() == null) {
                noChildDead = true;
            }
        }

        boolean result = !bookingStatus && !dateOfDelivery && !romDelivery && !modeOfDelivery && !episiotomy && !vaginalTear && !feedingDecision && !childGivenARVwithin72hrs && !onArt && !hivExposedInfant24hrs && !timeDelivery && !artStartedLDWard && !hbStatus && !hcStatus && !maternalOutcome && !childStatus && !noChildAlive && !noChildDead;
        if (result) {
            return true;
        } else {
            labourDeliveryInfoView.setErrorsVisibility(bookingStatus, dateOfDelivery, romDelivery, modeOfDelivery, episiotomy, vaginalTear, feedingDecision, childGivenARVwithin72hrs, onArt, hivExposedInfant24hrs, timeDelivery, artStartedLDWard, hbStatus, hcStatus, maternalOutcome, childStatus, noChildAlive, noChildDead);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        labourDeliveryInfoView.startPMTCTServicesActivity();
    }

}
