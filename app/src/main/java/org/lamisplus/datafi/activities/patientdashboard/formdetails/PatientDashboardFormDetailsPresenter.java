package org.lamisplus.datafi.activities.patientdashboard.formdetails;

import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardContract;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardPresenter;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;

import java.util.List;

public class PatientDashboardFormDetailsPresenter extends PatientDashboardPresenter implements PatientDashboardContract.PatientFormDetailsPresenter {

    private PatientDashboardContract.ViewPatientDetails mPatientDetailsView;
    private Person mPersonId;
    private PersonDAO mPersonDAO;
    private String personId;

    public PatientDashboardFormDetailsPresenter(String id, PatientDashboardContract.ViewPatientDetails mPatientDetailsView) {
        this.mPatientDetailsView = mPatientDetailsView;
        this.mPersonDAO = new PersonDAO();
        this.mPerson = PersonDAO.findPersonById(id);
        this.personId = id;
        this.mPatientDetailsView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mPatientDetailsView.resolveFormDetailsDisplay(EncounterDAO.findAllEncounterForPatient(personId));
    }

}
