package org.lamisplus.datafi.activities.patientprofile;

import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.application.LamisPlus;

public class PatientProfilePresenter extends LamisBasePresenter implements PatientProfileContract.Presenter {

    private final PatientProfileContract.View patientProfileInfoView;
    private LamisPlus lamisPlus;
    private String mPatientID;

    public PatientProfilePresenter(PatientProfileContract.View patientProfileInfoView, String patientID) {
        this.patientProfileInfoView = patientProfileInfoView;
        this.mPatientID = patientID;
        this.patientProfileInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return mPatientID;
    }
}
