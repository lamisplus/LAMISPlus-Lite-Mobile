package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClientIntake implements Serializable {

    @SerializedName("htsClientId")
    @Expose
    private Integer htsClientId = null;

    @SerializedName("breastFeeding")
    @Expose
    private String breastFeeding;

    @SerializedName("clientCode")
    @Expose
    private String clientCode;

    @SerializedName("dateVisit")
    @Expose
    private String dateVisit;

    @SerializedName("firstTimeVisit")
    @Expose
    private String firstTimeVisit;

    @SerializedName("indexClient")
    @Expose
    private String indexClient;

    @SerializedName("indexClientCode")
    @Expose
    private String indexClientCode;

    @SerializedName("numChildren")
    @Expose
    private Integer numChildren = 0;

    @SerializedName("numWives")
    @Expose
    private Integer numWives = 0;

    @SerializedName("personId")
    @Expose
    private Integer personId = null;

    @SerializedName("pregnant")
    @Expose
    private Integer pregnant = null;

    @SerializedName("previouslyTested")
    @Expose
    private String previouslyTested;

    @SerializedName("referredFrom")
    @Expose
    private Integer referredFrom = null;

    @SerializedName("relationWithIndexClient")
    @Expose
    private Integer relationWithIndexClient = null;

    @SerializedName("targetGroup")
    @Expose
    private String targetGroup;

    @SerializedName("testingSetting")
    @Expose
    private String testingSetting;

    @SerializedName("typeCounseling")
    @Expose
    private Integer typeCounseling = null;

    @SerializedName("extra")
    @Expose
    private String extra;

    @SerializedName("riskStratificationCode")
    @Expose
    private String riskStratificationCode;


    public String isBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(String breastFeeding) {
        this.breastFeeding = breastFeeding;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(String dateVisit) {
        this.dateVisit = dateVisit;
    }

    public String isFirstTimeVisit() {
        return firstTimeVisit;
    }

    public void setFirstTimeVisit(String firstTimeVisit) {
        this.firstTimeVisit = firstTimeVisit;
    }

    public String getIndexClient() {
        return indexClient;
    }

    public void setIndexClient(String indexClient) {
        this.indexClient = indexClient;
    }

    public String getIndexClientCode() {
        return indexClientCode;
    }

    public void setIndexClientCode(String indexClientCode) {
        this.indexClientCode = indexClientCode;
    }

    public Integer getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(Integer numChildren) {
        this.numChildren = numChildren;
    }

    public Integer getNumWives() {
        return numWives;
    }

    public void setNumWives(Integer numWives) {
        this.numWives = numWives;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getPregnant() {
        return pregnant;
    }

    public void setPregnant(Integer pregnant) {
        this.pregnant = pregnant;
    }

    public String getPreviouslyTested() {
        return previouslyTested;
    }

    public void setPreviouslyTested(String previouslyTested) {
        this.previouslyTested = previouslyTested;
    }

    public Integer getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(Integer referredFrom) {
        this.referredFrom = referredFrom;
    }

    public Integer getRelationWithIndexClient() {
        return relationWithIndexClient;
    }

    public void setRelationWithIndexClient(Integer relationWithIndexClient) {
        this.relationWithIndexClient = relationWithIndexClient;
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

    public Integer getTypeCounseling() {
        return typeCounseling;
    }

    public void setTypeCounseling(Integer typeCounseling) {
        this.typeCounseling = typeCounseling;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getRiskStratificationCode() {
        return riskStratificationCode;
    }

    public void setRiskStratificationCode(String riskStratificationCode) {
        this.riskStratificationCode = riskStratificationCode;
    }

    public Integer getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(Integer htsClientId) {
        this.htsClientId = htsClientId;
    }
}
