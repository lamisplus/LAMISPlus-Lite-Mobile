package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClientIntake implements Serializable {

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
    private boolean indexClient;

    @SerializedName("numChildren")
    @Expose
    private int numChildren;

    @SerializedName("numWives")
    @Expose
    private int numWives;

    @SerializedName("personId")
    @Expose
    private String personId;

    @SerializedName("pregnant")
    @Expose
    private String pregnant;

    @SerializedName("previouslyTested")
    @Expose
    private Boolean previouslyTested;

    @SerializedName("referredFrom")
    @Expose
    private String referredFrom;

    @SerializedName("relationWithIndexClient")
    @Expose
    private String relationWithIndexClient;

    @SerializedName("targetGroup")
    @Expose
    private String targetGroup;

    @SerializedName("testingSetting")
    @Expose
    private String testingSetting;

    @SerializedName("typeCounseling")
    @Expose
    private String typeCounseling;

    @SerializedName("extra")
    @Expose
    private String extra;

    @SerializedName("personDto")
    @Expose
    private String personDto;


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

    public boolean isIndexClient() {
        return indexClient;
    }

    public void setIndexClient(boolean indexClient) {
        this.indexClient = indexClient;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPregnant() {
        return pregnant;
    }

    public void setPregnant(String pregnant) {
        this.pregnant = pregnant;
    }

    public Boolean getPreviouslyTested() {
        return previouslyTested;
    }

    public void setPreviouslyTested(Boolean previouslyTested) {
        this.previouslyTested = previouslyTested;
    }

    public String getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(String referredFrom) {
        this.referredFrom = referredFrom;
    }

    public String getRelationWithIndexClient() {
        return relationWithIndexClient;
    }

    public void setRelationWithIndexClient(String relationWithIndexClient) {
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

    public String getTypeCounseling() {
        return typeCounseling;
    }

    public void setTypeCounseling(String typeCounseling) {
        this.typeCounseling = typeCounseling;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPersonDto() {
        return personDto;
    }

    public void setPersonDto(String personDto) {
        this.personDto = personDto;
    }
}
