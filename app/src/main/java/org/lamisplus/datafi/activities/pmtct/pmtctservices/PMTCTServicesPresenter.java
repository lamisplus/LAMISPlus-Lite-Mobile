package org.lamisplus.datafi.activities.pmtct.pmtctservices;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class PMTCTServicesPresenter extends LamisBasePresenter implements PMTCTServicesContract.Presenter {

    private final PMTCTServicesContract.View htsServicesInfoView;
    private String mPatientId;

    public PMTCTServicesPresenter(PMTCTServicesContract.View htsServicesInfoView, String patientId){
        this.htsServicesInfoView = htsServicesInfoView;
        this.mPatientId = patientId;
        this.htsServicesInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public String getPatientId() {
        return mPatientId;
    }
}
