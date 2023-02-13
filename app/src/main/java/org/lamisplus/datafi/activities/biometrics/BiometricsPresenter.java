package org.lamisplus.datafi.activities.biometrics;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.application.LamisPlus;

public class BiometricsPresenter extends LamisBasePresenter implements BiometricsContract.Presenter {

    private final BiometricsContract.View biometricsInfoView;
    private LamisPlus lamisPlus;

    public BiometricsPresenter(BiometricsContract.View biometricsInfoView, LamisPlus lamisPlus) {
        this.biometricsInfoView = biometricsInfoView;
        this.lamisPlus = lamisPlus;
        this.biometricsInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

}
