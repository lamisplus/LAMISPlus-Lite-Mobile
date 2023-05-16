package org.lamisplus.datafi.activities.patientdashboard;

import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.dao.BiometricsDAO;
import org.lamisplus.datafi.dao.EncounterDAO;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Patient;
import org.lamisplus.datafi.models.Person;

public abstract class PatientDashboardPresenter extends LamisBasePresenter implements PatientDashboardContract.PatientDashboardMainPresenter {

    protected Person mPerson;

    @Override
    public void deletePatient() {
        long patientIDLong = mPerson.getId();
        int patientIDInteger = (int) patientIDLong;
        //Delete the Patient
        PersonDAO.deletePatient(patientIDLong);
        //Delete the Encounters
        EncounterDAO.deleteAllEncounter(patientIDLong);
        //Delete the Biometrics
        BiometricsDAO.deletePrint(patientIDInteger);
    }

    @Override
    public long getPatientId() {
        if (mPerson != null) {
            return mPerson.getId();
        }
        return 0;
    }

}
