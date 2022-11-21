package org.lamisplus.datafi.activities.patientprogram;

import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class PatientProgramPresenter extends LamisBasePresenter implements PatientProgramContract.Presenter {

    private final PatientProgramContract.View appInfoView;

    public String patientId;

    public PatientProgramPresenter(PatientProgramContract.View appInfoView, String patientId){
        this.appInfoView = appInfoView;
        this.patientId = patientId;
        this.appInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        appInfoView.bindDrawableResources();
    }


    @Override
    public String getPatientId() {
        return this.patientId;
    }
}
