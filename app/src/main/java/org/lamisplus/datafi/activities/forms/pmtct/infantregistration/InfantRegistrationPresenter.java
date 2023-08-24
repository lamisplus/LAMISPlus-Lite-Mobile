package org.lamisplus.datafi.activities.forms.pmtct.infantregistration;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.InfantRegistration;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.StringUtils;

public class InfantRegistrationPresenter extends LamisBasePresenter implements InfantRegistrationContract.Presenter {

    private final InfantRegistrationContract.View infantRegistrationInfoView;
    private String patientId;

    public InfantRegistrationPresenter(InfantRegistrationContract.View infantRegistrationInfoView, String patientId) {
        this.infantRegistrationInfoView = infantRegistrationInfoView;
        this.patientId = patientId;
        this.infantRegistrationInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public InfantRegistration patientToUpdate(String formName, String patientId) {
        return EncounterDAO.findInfantRegistrationFromForm(formName, patientId);
    }

    @Override
    public void confirmCreate(InfantRegistration infantRegistration, String packageName) {
        if (validate(infantRegistration)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String infantRegistrationEncounter = gson.toJson(infantRegistration);

            Encounter encounter = new Encounter();
            encounter.setName(ApplicationConstants.Forms.INFANT_INFORMATION_FORM);
            encounter.setPerson(String.valueOf(patientId));
            //Get the person id and save along
            Person personInfant = PersonDAO.findPersonById(patientId);
            if(personInfant != null && personInfant.getPersonId() != null) {
                encounter.setPersonId(personInfant.getPersonId());
            }
            encounter.setPackageName(packageName);
            encounter.setDataValues(infantRegistrationEncounter);
            encounter.save();
            infantRegistrationInfoView.startPMTCTServicesActivity();
        } else {
            infantRegistrationInfoView.scrollToTop();
        }
    }

    @Override
    public void confirmUpdate(InfantRegistration infantRegistration, Encounter encounter) {
        if (validate(infantRegistration)) {
            Person person = PersonDAO.findPersonById(patientId);
            if (person.getPersonId() != null) {
                encounter.setPersonId(person.getPersonId());
            }
            String s = new Gson().toJson(infantRegistration);
            encounter.setDataValues(s);
            encounter.save();
            infantRegistrationInfoView.startPMTCTServicesActivity();
        } else {
            infantRegistrationInfoView.scrollToTop();
        }
    }

    public boolean validate(InfantRegistration infantRegistration) {
        boolean infantGivenName = false;
        boolean hospitalNumber = false;
        boolean sex = false;

        infantRegistrationInfoView.setErrorsVisibility(infantGivenName, sex, hospitalNumber);

        if (StringUtils.isBlank(infantRegistration.getFirstName())) {
            infantGivenName = true;
        }

        if (StringUtils.isBlank(infantRegistration.getSex())) {
            sex = true;
        }

        if (StringUtils.isBlank(infantRegistration.getHospitalNumber())) {
            hospitalNumber = true;
        }

        boolean result = !infantGivenName && !sex && !hospitalNumber;

        if (result) {
            return true;
        } else {
            infantRegistrationInfoView.setErrorsVisibility(infantGivenName, sex, hospitalNumber);
            return false;
        }
    }

    @Override
    public void confirmDeleteEncounter(String formName, String patientId) {
        EncounterDAO.deleteEncounter(formName, patientId);
        infantRegistrationInfoView.startPMTCTServicesActivity();
    }

}
