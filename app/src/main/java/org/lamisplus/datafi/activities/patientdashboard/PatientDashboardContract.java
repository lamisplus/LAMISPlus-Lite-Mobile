package org.lamisplus.datafi.activities.patientdashboard;


import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Person;

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

}
