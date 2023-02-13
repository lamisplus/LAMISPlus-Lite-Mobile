package org.lamisplus.datafi.activities.hts.htsservices;

import org.lamisplus.datafi.activities.LamisBasePresenter;

public class HTSServicesPresenter extends LamisBasePresenter implements HTSServicesContract.Presenter {

    private final HTSServicesContract.View htsServicesInfoView;
    private String mPatientId;

    public HTSServicesPresenter(HTSServicesContract.View htsServicesInfoView, String patientId){
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
