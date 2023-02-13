package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RiskStratification implements Serializable {

    @SerializedName("personId")
    @Expose
    private long personId;

    @SerializedName("entryPoint")
    @Expose
    private String entryPoint;

    @SerializedName("communityEntryPoint")
    @Expose
    private String communityEntryPoint;

    @SerializedName("age")
    @Expose
    private int age;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("modality")
    @Expose
    private String modality;

    @SerializedName("riskAssessment")
    @Expose
    private RstRiskAssessment riskAssessment;

    @SerializedName("targetGroup")
    @Expose
    private String targetGroup;

    @SerializedName("testingSetting")
    @Expose
    private String testingSetting;

    @SerializedName("visitDate")
    @Expose
    private String visitDate;

    //This variable should not be exposed as it is used to enable if a patient should go further with the other HTS forms
    @SerializedName("eligible")
    private boolean eligible = true;

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public RstRiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RstRiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public String getTestingSetting() {
        return testingSetting;
    }

    public void setTestingSetting(String testingSetting) {
        this.testingSetting = testingSetting;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getCommunityEntryPoint() {
        return communityEntryPoint;
    }

    public void setCommunityEntryPoint(String communityEntryPoint) {
        this.communityEntryPoint = communityEntryPoint;
    }
}
