package org.lamisplus.datafi.activities.connectserver.nobiometricspatients;

import androidx.annotation.NonNull;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;
import org.lamisplus.datafi.models.Person;

import java.util.List;

public class NoBiometricsPatientsServerContract {

    interface View extends LamisBaseView<Presenter>{

        void updateAdapter(List<Person> patientList);

        void updateListVisibility(boolean isVisible);

        void updateListVisibility(boolean isVisible, @NonNull String replacementWord);

        void updateView();

    }

    interface Presenter extends LamisBasePresenterContract{

        void setQuery(String query);

        void updateLocalPatientsList();

    }
}
