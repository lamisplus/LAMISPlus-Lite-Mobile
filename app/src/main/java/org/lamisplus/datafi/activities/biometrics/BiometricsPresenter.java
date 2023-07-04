package org.lamisplus.datafi.activities.biometrics;

import android.util.Log;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.application.LamisPlus;
import org.lamisplus.datafi.utilities.ToastUtil;

public class BiometricsPresenter extends LamisBasePresenter implements BiometricsContract.Presenter {

    private final BiometricsContract.View biometricsInfoView;
    private String patientId;
    private Boolean isClientRecapturing = false;

    public BiometricsPresenter(BiometricsContract.View biometricsInfoView, String patientId, Boolean recapture) {
        this.biometricsInfoView = biometricsInfoView;
        this.patientId = patientId;
        this.isClientRecapturing = recapture;
        this.biometricsInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public Integer getPatientId() {
        if(patientId != null) {
            return Integer.parseInt(patientId);
        }
        return null;
    }

    @Override
    public Boolean isClientRecapturing() {
        return isClientRecapturing;
    }

}
