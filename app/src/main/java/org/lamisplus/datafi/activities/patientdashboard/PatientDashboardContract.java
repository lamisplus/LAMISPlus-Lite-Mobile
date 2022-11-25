package org.lamisplus.datafi.activities.patientdashboard;


import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Encounter;
import org.lamisplus.datafi.models.Person;

import java.util.List;

public interface PatientDashboardContract {

    interface ViewPatientMain extends LamisBaseView<PatientDashboardMainPresenter> {

    }

    interface ViewPatientDetails extends ViewPatientMain {
        void attachSnackbarToActivity();
        void resolvePatientDataDisplay(Person person);
        void showDialog(int resId);
        void dismissDialog();
        void showToast(int stringRes, boolean error);
        void setMenuTitle(String nameString, String identifier);
        void resolveFormDetailsDisplay(List<Encounter> encounterList);
    }

    interface PatientDashboardMainPresenter extends LamisBasePresenterContract {
        void deletePatient();
        long getPatientId();
    }

    interface PatientDetailsPresenter extends PatientDashboardMainPresenter  {
        void synchronizePatient();
        void updatePatientDataFromServer();
        void reloadPatientData(Person mPerson);
    }

    interface PatientFingerprintsPresenter extends PatientDashboardMainPresenter{

    }

    interface PatientFormDetailsPresenter extends PatientDashboardMainPresenter{

    }

}
