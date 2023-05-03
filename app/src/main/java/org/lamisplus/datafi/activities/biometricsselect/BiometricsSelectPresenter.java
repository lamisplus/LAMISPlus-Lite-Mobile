package org.lamisplus.datafi.activities.biometricsselect;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class BiometricsSelectPresenter extends LamisBasePresenter implements BiometricsSelectContract.Presenter {

    private final BiometricsSelectContract.View dashboardInfoView;

    private String patientId;

    public BiometricsSelectPresenter(BiometricsSelectContract.View dashboardInfoView, String patientId){
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
