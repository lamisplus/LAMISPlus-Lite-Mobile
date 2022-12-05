package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KnowledgeAssessment implements Serializable {

    @SerializedName("clientPregnant")
    private String clientPregnant;

    @SerializedName("clientInformHivTransRoutes") //
    private String clientInformHivTransRoutes;

    @SerializedName("clientInformPossibleTestResult") //
    private String clientInformPossibleTestResult;

    @SerializedName("clientInformPreventingsHivTrans")  //
    private String clientInformPreventingsHivTrans;

    @SerializedName("clientInformRiskkHivTrans") //
    private String clientInformRiskkHivTrans;

    @SerializedName("informConsentHivTest") //
    private String informConsentHivTest;

    @SerializedName("previousTestedHIVNegative") //
    private String previousTestedHIVNegative;

    @SerializedName("timeLastHIVNegativeTestResult")
    private String timeLastHIVNegativeTestResult;

    public String isClientInformHivTransRoutes() {
        return clientInformHivTransRoutes;
    }

    public void setClientInformHivTransRoutes(String clientInformHivTransRoutes) {
        this.clientInformHivTransRoutes = clientInformHivTransRoutes;
    }

    public String isClientInformPossibleTestResult() {
        return clientInformPossibleTestResult;
    }

    public void setClientInformPossibleTestResult(String clientInformPossibleTestResult) {
        this.clientInformPossibleTestResult = clientInformPossibleTestResult;
    }

    public String isClientInformPreventingsHivTrans() {
        return clientInformPreventingsHivTrans;
    }

    public void setClientInformPreventingsHivTrans(String clientInformPreventingsHivTrans) {
        this.clientInformPreventingsHivTrans = clientInformPreventingsHivTrans;
    }

    public String isClientInformRiskkHivTrans() {
        return clientInformRiskkHivTrans;
    }

    public void setClientInformRiskkHivTrans(String clientInformRiskkHivTrans) {
        this.clientInformRiskkHivTrans = clientInformRiskkHivTrans;
    }

    public String isInformConsentHivTest() {
        return informConsentHivTest;
    }

    public void setInformConsentHivTest(String informConsentHivTest) {
        this.informConsentHivTest = informConsentHivTest;
    }

    public String isPreviousTestedHIVNegative() {
        return previousTestedHIVNegative;
    }

    public void setPreviousTestedHIVNegative(String previousTestedHIVNegative) {
        this.previousTestedHIVNegative = previousTestedHIVNegative;
    }

    public String getTimeLastHIVNegativeTestResult() {
        return timeLastHIVNegativeTestResult;
    }

    public void setTimeLastHIVNegativeTestResult(String timeLastHIVNegativeTestResult) {
        this.timeLastHIVNegativeTestResult = timeLastHIVNegativeTestResult;
    }

    public String isClientPregnant() {
        return clientPregnant;
    }

    public void setClientPregnant(String clientPregnant) {
        this.clientPregnant = clientPregnant;
    }
}
