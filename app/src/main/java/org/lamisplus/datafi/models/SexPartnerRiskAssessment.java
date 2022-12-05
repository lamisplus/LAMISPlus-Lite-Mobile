package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SexPartnerRiskAssessment implements Serializable {

    @SerializedName("currentlyArvForPmtct")
    private String currentlyArvForPmtct;

    @SerializedName("knowHivPositiveAfterLostToFollowUp")
    private String knowHivPositiveAfterLostToFollowUp;

    @SerializedName("knowHivPositiveOnArv")
    private String knowHivPositiveOnArv;

    @SerializedName("newDiagnosedHivlastThreeMonths")
    private String newDiagnosedHivlastThreeMonths;

    @SerializedName("sexPartnerHivPositive")
    private String sexPartnerHivPositive;

    @SerializedName("uprotectedAnalSex")
    private String uprotectedAnalSex;

    public String isCurrentlyArvForPmtct() {
        return currentlyArvForPmtct;
    }

    public void setCurrentlyArvForPmtct(String currentlyArvForPmtct) {
        this.currentlyArvForPmtct = currentlyArvForPmtct;
    }

    public String isKnowHivPositiveAfterLostToFollowUp() {
        return knowHivPositiveAfterLostToFollowUp;
    }

    public void setKnowHivPositiveAfterLostToFollowUp(String knowHivPositiveAfterLostToFollowUp) {
        this.knowHivPositiveAfterLostToFollowUp = knowHivPositiveAfterLostToFollowUp;
    }

    public String isKnowHivPositiveOnArv() {
        return knowHivPositiveOnArv;
    }

    public void setKnowHivPositiveOnArv(String knowHivPositiveOnArv) {
        this.knowHivPositiveOnArv = knowHivPositiveOnArv;
    }

    public String isNewDiagnosedHivlastThreeMonths() {
        return newDiagnosedHivlastThreeMonths;
    }

    public void setNewDiagnosedHivlastThreeMonths(String newDiagnosedHivlastThreeMonths) {
        this.newDiagnosedHivlastThreeMonths = newDiagnosedHivlastThreeMonths;
    }

    public String isSexPartnerHivPositive() {
        return sexPartnerHivPositive;
    }

    public void setSexPartnerHivPositive(String sexPartnerHivPositive) {
        this.sexPartnerHivPositive = sexPartnerHivPositive;
    }

    public String isUprotectedAnalSex() {
        return uprotectedAnalSex;
    }

    public void setUprotectedAnalSex(String uprotectedAnalSex) {
        this.uprotectedAnalSex = uprotectedAnalSex;
    }

}
