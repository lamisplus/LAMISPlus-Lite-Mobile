package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RstRiskAssessment implements Serializable {

    @SerializedName("diagnosedWithTb")
    @Expose
    private String diagnosedWithTb;

    @SerializedName("lastHivTestBasedOnRequest")
    @Expose
    private String lastHivTestBasedOnRequest;

    @SerializedName("lastHivTestBloodTransfusion")
    @Expose
    private String lastHivTestBloodTransfusion;

    @SerializedName("lastHivTestDone")
    @Expose
    private String lastHivTestDone;

    @SerializedName("lastHivTestForceToHaveSex")
    @Expose
    private String lastHivTestForceToHaveSex;

    @SerializedName("lastHivTestHadAnal")
    @Expose
    private String lastHivTestHadAnal;

    @SerializedName("lastHivTestInjectedDrugs")
    @Expose
    private String lastHivTestInjectedDrugs;

    @SerializedName("lastHivTestPainfulUrination")
    @Expose
    private String lastHivTestPainfulUrination;

    @SerializedName("lastHivTestVaginalOral")
    @Expose
    private String lastHivTestVaginalOral;

    @SerializedName("whatWasTheResult")
    @Expose
    private String whatWasTheResult;

    public String getDiagnosedWithTb() {
        return diagnosedWithTb;
    }

    public void setDiagnosedWithTb(String diagnosedWithTb) {
        this.diagnosedWithTb = diagnosedWithTb;
    }

    public String getLastHivTestBasedOnRequest() {
        return lastHivTestBasedOnRequest;
    }

    public void setLastHivTestBasedOnRequest(String lastHivTestBasedOnRequest) {
        this.lastHivTestBasedOnRequest = lastHivTestBasedOnRequest;
    }

    public String getLastHivTestBloodTransfusion() {
        return lastHivTestBloodTransfusion;
    }

    public void setLastHivTestBloodTransfusion(String lastHivTestBloodTransfusion) {
        this.lastHivTestBloodTransfusion = lastHivTestBloodTransfusion;
    }

    public String getLastHivTestDone() {
        return lastHivTestDone;
    }

    public void setLastHivTestDone(String lastHivTestDone) {
        this.lastHivTestDone = lastHivTestDone;
    }

    public String getLastHivTestForceToHaveSex() {
        return lastHivTestForceToHaveSex;
    }

    public void setLastHivTestForceToHaveSex(String lastHivTestForceToHaveSex) {
        this.lastHivTestForceToHaveSex = lastHivTestForceToHaveSex;
    }

    public String getLastHivTestHadAnal() {
        return lastHivTestHadAnal;
    }

    public void setLastHivTestHadAnal(String lastHivTestHadAnal) {
        this.lastHivTestHadAnal = lastHivTestHadAnal;
    }

    public String getLastHivTestInjectedDrugs() {
        return lastHivTestInjectedDrugs;
    }

    public void setLastHivTestInjectedDrugs(String lastHivTestInjectedDrugs) {
        this.lastHivTestInjectedDrugs = lastHivTestInjectedDrugs;
    }

    public String getLastHivTestPainfulUrination() {
        return lastHivTestPainfulUrination;
    }

    public void setLastHivTestPainfulUrination(String lastHivTestPainfulUrination) {
        this.lastHivTestPainfulUrination = lastHivTestPainfulUrination;
    }

    public String getLastHivTestVaginalOral() {
        return lastHivTestVaginalOral;
    }

    public void setLastHivTestVaginalOral(String lastHivTestVaginalOral) {
        this.lastHivTestVaginalOral = lastHivTestVaginalOral;
    }

    public String getWhatWasTheResult() {
        return whatWasTheResult;
    }

    public void setWhatWasTheResult(String whatWasTheResult) {
        this.whatWasTheResult = whatWasTheResult;
    }
}
