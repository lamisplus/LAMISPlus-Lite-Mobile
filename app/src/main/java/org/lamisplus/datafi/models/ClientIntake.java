package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClientIntake implements Serializable {

    @SerializedName("htsClientId")
    @Expose
    private int htsClientId;

    @SerializedName("breastFeeding")
    @Expose
    private boolean breastFeeding;

    @SerializedName("clientCode")
    @Expose
    private String clientCode;

    @SerializedName("dateVisit")
    @Expose
    private String dateVisit;

    @SerializedName("firstTimeVisit")
    @Expose
    private boolean firstTimeVisit;

    @SerializedName("indexClient")
    @Expose
    private String indexClient;

    @SerializedName("indexClientCode")
    @Expose
    private String indexClientCode;

    @SerializedName("numChildren")
    @Expose
    private int numChildren;

    @SerializedName("numWives")
    @Expose
    private int numWives;

    @SerializedName("personId")
    @Expose
    private int personId;

    @SerializedName("pregnant")
    @Expose
    private int pregnant;

    @SerializedName("previouslyTested")
    @Expose
    private Boolean previouslyTested;

    @SerializedName("referredFrom")
    @Expose
    private int referredFrom;

    @SerializedName("relationWithIndexClient")
    @Expose
    private int relationWithIndexClient;

    @SerializedName("targetGroup")
    @Expose
    private String targetGroup;

    @SerializedName("testingSetting")
    @Expose
    private String testingSetting;

    @SerializedName("typeCounseling")
    @Expose
    private int typeCounseling;

    @SerializedName("extra")
    @Expose
    private String extra;

    @SerializedName("riskStratificationCode")
    @Expose
    private String riskStratificationCode;


    public boolean isBreastFeeding() {
        return breastFeeding;
    }

    public void setBreastFeeding(boolean breastFeeding) {
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

    public boolean isFirstTimeVisit() {
        return firstTimeVisit;
    }

    public void setFirstTimeVisit(boolean firstTimeVisit) {
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

    public int getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    public int getNumWives() {
        return numWives;
    }

    public void setNumWives(int numWives) {
        this.numWives = numWives;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPregnant() {
        return pregnant;
    }

    public void setPregnant(int pregnant) {
        this.pregnant = pregnant;
    }

    public Boolean getPreviouslyTested() {
        return previouslyTested;
    }

    public void setPreviouslyTested(Boolean previouslyTested) {
        this.previouslyTested = previouslyTested;
    }

    public int getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(int referredFrom) {
        this.referredFrom = referredFrom;
    }

    public int getRelationWithIndexClient() {
        return relationWithIndexClient;
    }

    public void setRelationWithIndexClient(int relationWithIndexClient) {
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

    public int getTypeCounseling() {
        return typeCounseling;
    }

    public void setTypeCounseling(int typeCounseling) {
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

    public int getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(int htsClientId) {
        this.htsClientId = htsClientId;
    }
}
