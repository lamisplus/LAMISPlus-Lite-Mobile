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

    public boolean isPreviousTestedHIVNegative() {
        return previousTestedHIVNegative;
    }

    public void setPreviousTestedHIVNegative(boolean previousTestedHIVNegative) {
        this.previousTestedHIVNegative = previousTestedHIVNegative;
    }

    public boolean isTimeLastHIVNegativeTestResult() {
        return timeLastHIVNegativeTestResult;
    }

    public void setTimeLastHIVNegativeTestResult(boolean timeLastHIVNegativeTestResult) {
        this.timeLastHIVNegativeTestResult = timeLastHIVNegativeTestResult;
    }

    public boolean isClientPregnant() {
        return clientPregnant;
    }

    public void setClientPregnant(boolean clientPregnant) {
        this.clientPregnant = clientPregnant;
    }

    public boolean isClientInformHivTransRoutes() {
        return clientInformHivTransRoutes;
    }

    public void setClientInformHivTransRoutes(boolean clientInformHivTransRoutes) {
        this.clientInformHivTransRoutes = clientInformHivTransRoutes;
    }

    public boolean isClientInformRiskHivTrans() {
        return clientInformRiskHivTrans;
    }

    public void setClientInformRiskHivTrans(boolean clientInformRiskHivTrans) {
        this.clientInformRiskHivTrans = clientInformRiskHivTrans;
    }

    public boolean isClientInformPreventingsHivTrans() {
        return clientInformPreventingsHivTrans;
    }

    public void setClientInformPreventingsHivTrans(boolean clientInformPreventingsHivTrans) {
        this.clientInformPreventingsHivTrans = clientInformPreventingsHivTrans;
    }

    public boolean isClientInformPossibleTestResult() {
        return clientInformPossibleTestResult;
    }

    public void setClientInformPossibleTestResult(boolean clientInformPossibleTestResult) {
        this.clientInformPossibleTestResult = clientInformPossibleTestResult;
    }

    public boolean isInformConsentHivTest() {
        return informConsentHivTest;
    }

    public void setInformConsentHivTest(boolean informConsentHivTest) {
        this.informConsentHivTest = informConsentHivTest;
    }
}

