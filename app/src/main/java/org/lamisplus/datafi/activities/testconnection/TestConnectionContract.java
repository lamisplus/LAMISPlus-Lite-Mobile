package org.lamisplus.datafi.activities.testconnection;

import org.lamisplus.datafi.activities.LamisBasePresenterContract;
import org.lamisplus.datafi.activities.LamisBaseView;

public class TestConnectionContract {

    interface View extends LamisBaseView<Presenter>{
        void loadIpAddress();
    }

    interface Presenter extends LamisBasePresenterContract{

    }
}
