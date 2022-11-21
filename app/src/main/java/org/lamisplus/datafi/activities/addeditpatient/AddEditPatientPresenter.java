package org.lamisplus.datafi.activities.addeditpatient;

import android.content.Intent;
import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.patientdashboard.PatientDashboardActivity;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.dao.PersonDAO;
import org.lamisplus.datafi.models.Person;
import org.lamisplus.datafi.utilities.ApplicationConstants;
import org.lamisplus.datafi.utilities.LamisCustomHandler;

public class AddEditPatientPresenter extends LamisBasePresenter implements AddEditPatientContract.Presenter {

    public AddEditPatientContract.View mAddEditPatientInfoView;
    private String patientToUpdateId;

    public AddEditPatientPresenter(AddEditPatientContract.View mAddEditPatientInfoView, String patientId) {
        this.mAddEditPatientInfoView = mAddEditPatientInfoView;
        this.patientToUpdateId = patientId;
        this.mAddEditPatientInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public Long getPatientId(){
        return Long.parseLong(patientToUpdateId);
    }

    @Override
    public Person patientToUpdate() {
        return new PersonDAO().findPersonById(patientToUpdateId);
    }

    @Override
    public void confirmRegister(Person person) {
        LamisCustomHandler.showJson(person);
        person.save();
        mAddEditPatientInfoView.startPatientDashbordActivity(person);
    }

    @Override
    public void confirmUpdate(Person person) {
        mAddEditPatientInfoView.scrollToTop();
        mAddEditPatientInfoView.setProgressBarVisibility(true);
        LamisCustomHandler.showJson(person);
        person.save();
        mAddEditPatientInfoView.startPatientDashbordActivity(person);
    }

    @Override
    public void registerPatient() {

    }
}
