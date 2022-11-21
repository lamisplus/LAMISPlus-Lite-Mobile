package org.lamisplus.datafi.activities.forms.hts;

import org.lamisplus.datafi.activities.LamisBasePresenter;
import org.lamisplus.datafi.activities.addeditpatient.AddEditPatientContract;

public class HTSFormPresenter extends LamisBasePresenter implements HTSFormContract.Presenter {


    public HTSFormContract.View mhtsFormView;

    public HTSFormPresenter(HTSFormContract.View mhtsFormView){
        this.mhtsFormView = mhtsFormView;
        this.mhtsFormView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }
}
