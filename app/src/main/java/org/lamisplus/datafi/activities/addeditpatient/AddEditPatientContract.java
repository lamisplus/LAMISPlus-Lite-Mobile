package org.lamisplus.datafi.activities.addeditpatient;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Person;

public class AddEditPatientContract {

    interface View extends LamisBaseView<AddEditPatientContract.Presenter>{
        void scrollToTop();
        void hideSoftKeys();
        void startPatientDashbordActivity(Person person);
        boolean areFieldsNotEmpty();

        void setProgressBarVisibility(boolean b);
    }

    interface Presenter extends LamisBasePresenterContract{

        Person patientToUpdate();
        void confirmRegister(Person person);
        void confirmUpdate(Person person);
        void registerPatient();
        Long getPatientId();

    }

}
