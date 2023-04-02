package org.lamisplus.datafi.activities.biometrics;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.application.LamisPlus;

public class BiometricsPresenter extends LamisBasePresenter implements BiometricsContract.Presenter {

    private final BiometricsContract.View biometricsInfoView;
    private String patientId;

    public BiometricsPresenter(BiometricsContract.View biometricsInfoView, String patientId) {
        this.biometricsInfoView = biometricsInfoView;
        this.patientId = patientId;
        this.biometricsInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public Integer getPatientId() {
        return Integer.parseInt(patientId);
    }
}
