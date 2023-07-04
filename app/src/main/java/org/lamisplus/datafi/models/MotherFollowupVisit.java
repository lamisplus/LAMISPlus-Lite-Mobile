package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MotherFollowupVisit implements Serializable {
    @SerializedName("ancNo")
    @Expose
    private String ancNo;

    @SerializedName("dateOfViralLoad")
    @Expose
    private String dateOfViralLoad;

    @SerializedName("dateOfVisit")
    @Expose
    private String dateOfVisit;

    @SerializedName("dateOfmeternalOutcome")
    @Expose
    private String dateOfmeternalOutcome;

    @SerializedName("dsd")
    @Expose
    private String dsd;

    @SerializedName("dsdModel")
    @Expose
    private String dsdModel;

    @SerializedName("dsdOption")
    @Expose
    private String dsdOption;

    @SerializedName("enteryPoint")
    @Expose
    private String enteryPoint;

    @SerializedName("fpCounseling")
    @Expose
    private String fpCounseling;

    @SerializedName("fpMethod")
    @Expose
    private String fpMethod;

    @SerializedName("gaOfViralLoad")
    @Expose
    private String gaOfViralLoad;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("maternalOutcome")
    @Expose
    private String maternalOutcome;

    @SerializedName("nextAppointmentDate")
    @Expose
    private String nextAppointmentDate;

    @SerializedName("personUuid")
    @Expose
    private String personUuid;

    @SerializedName("resultOfViralLoad")
    @Expose
    private String resultOfViralLoad;

    @SerializedName("transferTo")
    @Expose
    private String transferTo;

    @SerializedName("visitStatus")
    @Expose
    private String visitStatus;

    @SerializedName("timeOfViralLoad")
    @Expose
    private String timeOfViralLoad;

    public String getAncNo() {
        return ancNo;
    }

    public void setAncNo(String ancNo) {
        this.ancNo = ancNo;
    }

    public String getDateOfViralLoad() {
        return dateOfViralLoad;
    }

    public void setDateOfViralLoad(String dateOfViralLoad) {
        this.dateOfViralLoad = dateOfViralLoad;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getDateOfmeternalOutcome() {
        return dateOfmeternalOutcome;
    }

    public void setDateOfmeternalOutcome(String dateOfmeternalOutcome) {
        this.dateOfmeternalOutcome = dateOfmeternalOutcome;
    }

    public String getDsd() {
        return dsd;
    }

    public void setDsd(String dsd) {
        this.dsd = dsd;
    }

    public String getDsdModel() {
        return dsdModel;
    }

    public void setDsdModel(String dsdModel) {
        this.dsdModel = dsdModel;
    }

    public String getDsdOption() {
        return dsdOption;
    }

    public void setDsdOption(String dsdOption) {
        this.dsdOption = dsdOption;
    }

    public String getEnteryPoint() {
        return enteryPoint;
    }

    public void setEnteryPoint(String enteryPoint) {
        this.enteryPoint = enteryPoint;
    }

    public String getFpCounseling() {
        return fpCounseling;
    }

    public void setFpCounseling(String fpCounseling) {
        this.fpCounseling = fpCounseling;
    }

    public String getFpMethod() {
        return fpMethod;
    }

    public void setFpMethod(String fpMethod) {
        this.fpMethod = fpMethod;
    }

    public String getGaOfViralLoad() {
        return gaOfViralLoad;
    }

    public void setGaOfViralLoad(String gaOfViralLoad) {
        this.gaOfViralLoad = gaOfViralLoad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaternalOutcome() {
        return maternalOutcome;
    }

    public void setMaternalOutcome(String maternalOutcome) {
        this.maternalOutcome = maternalOutcome;
    }

    public String getNextAppointmentDate() {
        return nextAppointmentDate;
    }

    public void setNextAppointmentDate(String nextAppointmentDate) {
        this.nextAppointmentDate = nextAppointmentDate;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public String getResultOfViralLoad() {
        return resultOfViralLoad;
    }

    public void setResultOfViralLoad(String resultOfViralLoad) {
        this.resultOfViralLoad = resultOfViralLoad;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }

    public String getTimeOfViralLoad() {
        return timeOfViralLoad;
    }

    public void setTimeOfViralLoad(String timeOfViralLoad) {
        this.timeOfViralLoad = timeOfViralLoad;
    }
}
