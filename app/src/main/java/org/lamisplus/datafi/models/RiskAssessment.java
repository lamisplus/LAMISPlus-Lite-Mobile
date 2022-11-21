package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RiskAssessment implements Serializable {

    @SerializedName("previousTestedHIVNegative")
    @Expose
    private boolean previousTestedHIVNegative;

    @SerializedName("timeLastHIVNegativeTestResult")
    @Expose
    private boolean timeLastHIVNegativeTestResult;

    @SerializedName("clientPregnant")
    @Expose
    private boolean clientPregnant;

    @SerializedName("clientInformHivTransRoutes")
    @Expose
    private boolean clientInformHivTransRoutes;

    @SerializedName("clientInformRiskHivTrans")
    @Expose
    private boolean clientInformRiskHivTrans;

    @SerializedName("clientInformPreventingsHivTrans")
    @Expose
    private boolean clientInformPreventingsHivTrans;

    @SerializedName("clientInformPossibleTestResult")
    @Expose
    private boolean clientInformPossibleTestResult;

    @SerializedName("informConsentHivTest")
    @Expose
    private boolean informConsentHivTest;


}

