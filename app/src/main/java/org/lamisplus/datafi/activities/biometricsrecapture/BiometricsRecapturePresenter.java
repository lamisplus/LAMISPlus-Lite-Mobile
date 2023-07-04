package org.lamisplus.datafi.activities.biometricsrecapture;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class BiometricsRecapturePresenter extends LamisBasePresenter implements BiometricsRecaptureContract.Presenter {

    private final BiometricsRecaptureContract.View dashboardInfoView;

    private String patientId;

    public BiometricsRecapturePresenter(BiometricsRecaptureContract.View dashboardInfoView, String patientId){
        this.dashboardInfoView = dashboardInfoView;
        this.patientId = patientId;
        this.dashboardInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        dashboardInfoView.bindDrawableResources();
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public void handleClicks() {

    }
}
