package org.lamisplus.datafi.activities.patientdashboard;

import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Patient;
import org.lamisplus.datafi.models.Person;

public abstract class PatientDashboardPresenter extends LamisBasePresenter implements PatientDashboardContract.PatientDashboardMainPresenter {

    protected Person mPerson;

    @Override
    public void deletePatient() {
        new PersonDAO().deletePatient(mPerson.getId());
    }

    @Override
    public long getPatientId() {
        return mPerson.getId();
    }

}
