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
    private String personId;

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

    @SerializedName("riskAssessmentList")
    @Expose
    private List<RiskAssessment> riskAssessmentList = new ArrayList<>();

    private String riskAssessment;

    @SerializedName("targetGroup")
    @Expose
    private String targetGroup;

    @SerializedName("testingSetting")
    @Expose
    private String testingSetting;

    @SerializedName("visitDate")
    @Expose
    private String visitDate;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
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

    public List<RiskAssessment> getRiskAssessmentList() {
        return riskAssessmentList;
    }

    public void setRiskAssessmentList(List<RiskAssessment> riskAssessmentList) {
        this.riskAssessmentList = riskAssessmentList;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
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

    public RiskAssessment getRiskAssessments(){
        if(this.getRiskAssessmentList() != null){
            return getRiskAssessmentList().get(0);
        }
        return null;
    }
}
