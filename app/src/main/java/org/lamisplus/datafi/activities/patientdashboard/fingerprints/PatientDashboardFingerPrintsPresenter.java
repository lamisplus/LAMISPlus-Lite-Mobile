package org.lamisplus.datafi.activities.patientdashboard.fingerprints;

import org.lamisplus.datafi.R;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardContract;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardPresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.NetworkUtils;

public class PatientDashboardFingerPrintsPresenter extends PatientDashboardPresenter implements PatientDashboardContract.PatientFingerprintsPresenter {

    private PatientDashboardContract.ViewPatientDetails mPatientDetailsView;
    private Person mPersonId;
    private PersonDAO mPersonDAO;

    public PatientDashboardFingerPrintsPresenter(String id, PatientDashboardContract.ViewPatientDetails mPatientDetailsView) {
        this.mPatientDetailsView = mPatientDetailsView;
        this.mPersonDAO = new PersonDAO();
        this.mPerson = PersonDAO.findPersonById(id);
        this.mPatientDetailsView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }
}
