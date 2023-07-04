package org.lamisplus.datafi.activities.forms.pmtct.partners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.PMTCTEnrollment;
import org.lamisplus.datafi.models.PartnerRegistration;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class PartnersPresenter extends LamisBasePresenter implements PartnersContract.Presenter {

    private final PartnersContract.View partnersInfoView;
    private String patientId;

    public PartnersPresenter(PartnersContract.View partnersInfoView, String patientId) {
        this.partnersInfoView = partnersInfoView;
        this.patientId = patientId;
        this.partnersInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public PartnerRegistration patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findPartnerRegistrationFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(PartnerRegistration partnerRegistration, String packageName) {
        if (validate(partnerRegistration)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String partnersRegistrationEncounter = gson.toJson(partnerRegistration);

            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.PARTNERS_FORM);
            encounter.setPerson(String.valueOf(patientId));
            encounter.setPackageName(packageName);
            encounter.setDataValues(partnersRegistrationEncounter);
            encounter.save();

            partnersInfoView.startPMTCTServicesActivity();
        } else {
            partnersInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(PartnerRegistration partnerRegistration, Encounter encounter) {
        if (validate(partnerRegistration)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(partnerRegistration);
            encounter.setDataValues(s);
            encounter.save();
            partnersInfoView.startPMTCTServicesActivity();
        } else {
            partnersInfoView.scrollToTop();
        }
    }

    public boolean validate(PartnerRegistration partnerRegistration) {
        boolean fullName = false;
        boolean age = false;
        boolean pretestCounseled = false;

        partnersInfoView.setErrorsVisibility(fullName, age, pretestCounseled);

        if (StringUtils.isBlank(partnerRegistration.getFullName())) {
            fullName = true;
        }

        if (StringUtils.isBlank(partnerRegistration.getAge())) {
            age = true;
        }

        if (StringUtils.isBlank(partnerRegistration.getPreTestCounseled())) {
            pretestCounseled = true;
        }

        boolean result = !fullName && !age && !pretestCounseled;

        if (result) {
            return true;
        } else {
            partnersInfoView.setErrorsVisibility(fullName, age, pretestCounseled);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        partnersInfoView.startPMTCTServicesActivity();
    }

}
