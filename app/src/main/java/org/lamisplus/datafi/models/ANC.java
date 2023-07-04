package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ANC implements Serializable {

    @SerializedName("personId")
    @Expose
    private String personId;

    @SerializedName("ancNo")
    @Expose
    private String ancNo;

    @SerializedName("gaweeks")
    @Expose
    private String gaweeks;

    @SerializedName("gravida")
    @Expose
    private String gravida;

    @SerializedName("expectedDeliveryDate")
    @Expose
    private String expectedDeliveryDate;

    @SerializedName("firstAncDate")
    @Expose
    private String firstAncDate;

    @SerializedName("lmp")
    @Expose
    private String lmp;

    @SerializedName("parity")
    @Expose
    private String parity;

    @SerializedName("person_uuid")
    @Expose
    private String person_uuid;

    @SerializedName("hivDiognosicTime")
    @Expose
    private String hivDiognosicTime;

    @SerializedName("referredSyphilisTreatment")
    @Expose
    private String referredSyphilisTreatment;

    @SerializedName("testResultSyphilis")
    @Expose
    private String testResultSyphilis;

    @SerializedName("testedSyphilis")
    @Expose
    private String testedSyphilis;

    @SerializedName("treatedSyphilis")
    @Expose
    private String treatedSyphilis;

    @SerializedName("personDto")
    @Expose
    private String personDto;

    @SerializedName("pmtctHtsInfo")
    @Expose
    private String pmtctHtsInfo;

    @SerializedName("syphilisInfo")
    @Expose
    private String syphilisInfo;

    @SerializedName("partnerNotification")
    @Expose
    private String partnerNotification;

    @SerializedName("sourceOfReferral")
    @Expose
    private String sourceOfReferral;

    @SerializedName("staticHivStatus")
    @Expose
    private String staticHivStatus;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("dateOfEnrollment")
    @Expose
    private String dateOfEnrollment;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getAncNo() {
        return ancNo;
    }

    public void setAncNo(String ancNo) {
        this.ancNo = ancNo;
    }

    public String getGaweeks() {
        return gaweeks;
    }

    public void setGaweeks(String gaweeks) {
        this.gaweeks = gaweeks;
    }

    public String getGravida() {
        return gravida;
    }

    public void setGravida(String gravida) {
        this.gravida = gravida;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getFirstAncDate() {
        return firstAncDate;
    }

    public void setFirstAncDate(String firstAncDate) {
        this.firstAncDate = firstAncDate;
    }

    public String getLmp() {
        return lmp;
    }

    public void setLmp(String lmp) {
        this.lmp = lmp;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getPerson_uuid() {
        return person_uuid;
    }

    public void setPerson_uuid(String person_uuid) {
        this.person_uuid = person_uuid;
    }

    public String getHivDiognosicTime() {
        return hivDiognosicTime;
    }

    public void setHivDiognosicTime(String hivDiognosicTime) {
        this.hivDiognosicTime = hivDiognosicTime;
    }

    public String getReferredSyphilisTreatment() {
        return referredSyphilisTreatment;
    }

    public void setReferredSyphilisTreatment(String referredSyphilisTreatment) {
        this.referredSyphilisTreatment = referredSyphilisTreatment;
    }

    public String getTestResultSyphilis() {
        return testResultSyphilis;
    }

    public void setTestResultSyphilis(String testResultSyphilis) {
        this.testResultSyphilis = testResultSyphilis;
    }

    public String getTestedSyphilis() {
        return testedSyphilis;
    }

    public void setTestedSyphilis(String testedSyphilis) {
        this.testedSyphilis = testedSyphilis;
    }

    public String getTreatedSyphilis() {
        return treatedSyphilis;
    }

    public void setTreatedSyphilis(String treatedSyphilis) {
        this.treatedSyphilis = treatedSyphilis;
    }

    public String getPersonDto() {
        return personDto;
    }

    public void setPersonDto(String personDto) {
        this.personDto = personDto;
    }

    public String getPmtctHtsInfo() {
        return pmtctHtsInfo;
    }

    public void setPmtctHtsInfo(String pmtctHtsInfo) {
        this.pmtctHtsInfo = pmtctHtsInfo;
    }

    public String getSyphilisInfo() {
        return syphilisInfo;
    }

    public void setSyphilisInfo(String syphilisInfo) {
        this.syphilisInfo = syphilisInfo;
    }

    public String getPartnerNotification() {
        return partnerNotification;
    }

    public void setPartnerNotification(String partnerNotification) {
        this.partnerNotification = partnerNotification;
    }

    public String getSourceOfReferral() {
        return sourceOfReferral;
    }

    public void setSourceOfReferral(String sourceOfReferral) {
        this.sourceOfReferral = sourceOfReferral;
    }

    public String getStaticHivStatus() {
        return staticHivStatus;
    }

    public void setStaticHivStatus(String staticHivStatus) {
        this.staticHivStatus = staticHivStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(String dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }
}
