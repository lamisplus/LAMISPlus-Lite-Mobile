package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class  RiskAssessment implements Serializable {

    @SerializedName("abuseDrug")
    @Expose
    private String abuseDrug;

    @SerializedName("bloodTransfusion")
    @Expose
    private String bloodTransfusion;

    @SerializedName("bloodtransInlastThreeMonths")
    @Expose
    private String bloodtransInlastThreeMonths;

    @SerializedName("consistentWeightFeverNightCough")
    @Expose
    private String consistentWeightFeverNightCough;

    @SerializedName("everHadSexualIntercourse")
    @Expose
    private String everHadSexualIntercourse;

    @SerializedName("experiencePain")
    @Expose
    private String experiencePain;

    @SerializedName("haveCondomBurst")
    @Expose
    private String haveCondomBurst;

    @SerializedName("haveSexWithoutCondom")
    @Expose
    private String haveSexWithoutCondom;

    @SerializedName("moreThanOneSexPartnerLastThreeMonths")
    @Expose
    private String moreThanOneSexPartnerLastThreeMonths;

    @SerializedName("sexUnderInfluence")
    @Expose
    private String sexUnderInfluence;

    @SerializedName("soldPaidVaginalSex")
    @Expose
    private String soldPaidVaginalSex;

    @SerializedName("stiLastThreeMonths")
    @Expose
    private String stiLastThreeMonths;

    @SerializedName("unprotectedVaginalSex")
    @Expose
    private String unprotectedVaginalSex;

    @SerializedName("uprotectedAnalSex")
    @Expose
    private String uprotectedAnalSex;

    @SerializedName("uprotectedSexWithCasualLastThreeMonths")
    @Expose
    private String uprotectedSexWithCasualLastThreeMonths;

    @SerializedName("uprotectedSexWithRegularPartnerLastThreeMonths")
    @Expose
    private String uprotectedSexWithRegularPartnerLastThreeMonths;

    public String isAbuseDrug() {
        return abuseDrug;
    }

    public void setAbuseDrug(String abuseDrug) {
        this.abuseDrug = abuseDrug;
    }

    public String isBloodTransfusion() {
        return bloodTransfusion;
    }

    public void setBloodTransfusion(String bloodTransfusion) {
        this.bloodTransfusion = bloodTransfusion;
    }

    public String isBloodtransInlastThreeMonths() {
        return bloodtransInlastThreeMonths;
    }

    public void setBloodtransInlastThreeMonths(String bloodtransInlastThreeMonths) {
        this.bloodtransInlastThreeMonths = bloodtransInlastThreeMonths;
    }

    public String isConsistentWeightFeverNightCough() {
        return consistentWeightFeverNightCough;
    }

    public void setConsistentWeightFeverNightCough(String consistentWeightFeverNightCough) {
        this.consistentWeightFeverNightCough = consistentWeightFeverNightCough;
    }

    public String isEverHadSexualIntercourse() {
        return everHadSexualIntercourse;
    }

    public void setEverHadSexualIntercourse(String everHadSexualIntercourse) {
        this.everHadSexualIntercourse = everHadSexualIntercourse;
    }

    public String isExperiencePain() {
        return experiencePain;
    }

    public void setExperiencePain(String experiencePain) {
        this.experiencePain = experiencePain;
    }

    public String isHaveCondomBurst() {
        return haveCondomBurst;
    }

    public void setHaveCondomBurst(String haveCondomBurst) {
        this.haveCondomBurst = haveCondomBurst;
    }

    public String isHaveSexWithoutCondom() {
        return haveSexWithoutCondom;
    }

    public void setHaveSexWithoutCondom(String haveSexWithoutCondom) {
        this.haveSexWithoutCondom = haveSexWithoutCondom;
    }

    public String isMoreThanOneSexPartnerLastThreeMonths() {
        return moreThanOneSexPartnerLastThreeMonths;
    }

    public void setMoreThanOneSexPartnerLastThreeMonths(String moreThanOneSexPartnerLastThreeMonths) {
        this.moreThanOneSexPartnerLastThreeMonths = moreThanOneSexPartnerLastThreeMonths;
    }

    public String isSexUnderInfluence() {
        return sexUnderInfluence;
    }

    public void setSexUnderInfluence(String sexUnderInfluence) {
        this.sexUnderInfluence = sexUnderInfluence;
    }

    public String isSoldPaidVaginalSex() {
        return soldPaidVaginalSex;
    }

    public void setSoldPaidVaginalSex(String soldPaidVaginalSex) {
        this.soldPaidVaginalSex = soldPaidVaginalSex;
    }

    public String isStiLastThreeMonths() {
        return stiLastThreeMonths;
    }

    public void setStiLastThreeMonths(String stiLastThreeMonths) {
        this.stiLastThreeMonths = stiLastThreeMonths;
    }

    public String isUnprotectedVaginalSex() {
        return unprotectedVaginalSex;
    }

    public void setUnprotectedVaginalSex(String unprotectedVaginalSex) {
        this.unprotectedVaginalSex = unprotectedVaginalSex;
    }

    public String isUprotectedAnalSex() {
        return uprotectedAnalSex;
    }

    public void setUprotectedAnalSex(String uprotectedAnalSex) {
        this.uprotectedAnalSex = uprotectedAnalSex;
    }

    public String isUprotectedSexWithCasualLastThreeMonths() {
        return uprotectedSexWithCasualLastThreeMonths;
    }

    public void setUprotectedSexWithCasualLastThreeMonths(String uprotectedSexWithCasualLastThreeMonths) {
        this.uprotectedSexWithCasualLastThreeMonths = uprotectedSexWithCasualLastThreeMonths;
    }

    public String isUprotectedSexWithRegularPartnerLastThreeMonths() {
        return uprotectedSexWithRegularPartnerLastThreeMonths;
    }

    public void setUprotectedSexWithRegularPartnerLastThreeMonths(String uprotectedSexWithRegularPartnerLastThreeMonths) {
        this.uprotectedSexWithRegularPartnerLastThreeMonths = uprotectedSexWithRegularPartnerLastThreeMonths;
    }
}

