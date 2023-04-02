package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Elicitation implements Serializable {

    @SerializedName("acceptedIns")
    @Expose
    private Integer acceptedIns = null;

    @SerializedName("offeredIns")
    @Expose
    private Integer offeredIns = null;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("middleName")
    @Expose
    private String middleName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("altPhoneNumber")
    @Expose
    private String altPhoneNumber;

    @SerializedName("sex")
    @Expose
    private Integer sex = null;

    @SerializedName("htsClientId")
    @Expose
    private Integer htsClientId = null;

    @SerializedName("physicalHurt")
    @Expose
    private Integer physicalHurt = null;

    @SerializedName("threatenToHurt")
    @Expose
    private Integer threatenToHurt = null;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("hangOutSpots")
    @Expose
    private String hangOutSpots;

    @SerializedName("relativeToIndexClient")
    @Expose
    private Integer relativeToIndexClient = null;

    @SerializedName("currentlyLiveWithPartner")
    @Expose
    private Integer currentlyLiveWithPartner = null;

    @SerializedName("partnerTestedPositive")
    @Expose
    private Integer partnerTestedPositive = null;

    @SerializedName("sexuallyUncomfortable")
    @Expose
    private Integer sexuallyUncomfortable = null;

    @SerializedName("notificationMethod")
    @Expose
    private Integer notificationMethod = null;

    @SerializedName("datePartnerCameForTesting")
    @Expose
    private String datePartnerCameForTesting;

    @SerializedName("isDateOfBirthEstimated")
    @Expose
    private boolean isDateOfBirthEstimated;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("stateId")
    @Expose
    private Integer stateId;

    @SerializedName("elicited")
    @Expose
    private Integer elicited = null;

    @SerializedName("dateTested")
    @Expose
    private String dateTested;

    @SerializedName("countryId")
    @Expose
    private Integer countryId = 1;

    @SerializedName("lga")
    @Expose
    private Integer lga;

    @SerializedName("currentHivStatus")
    @Expose
    private String currentHivStatus;

    public Integer getAcceptedIns() {
        return acceptedIns;
    }

    public void setAcceptedIns(Integer acceptedIns) {
        this.acceptedIns = acceptedIns;
    }

    public Integer getOfferedIns() {
        return offeredIns;
    }

    public void setOfferedIns(Integer offeredIns) {
        this.offeredIns = offeredIns;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAltPhoneNumber() {
        return altPhoneNumber;
    }

    public void setAltPhoneNumber(String altPhoneNumber) {
        this.altPhoneNumber = altPhoneNumber;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(Integer htsClientId) {
        this.htsClientId = htsClientId;
    }

    public Integer getPhysicalHurt() {
        return physicalHurt;
    }

    public void setPhysicalHurt(Integer physicalHurt) {
        this.physicalHurt = physicalHurt;
    }

    public Integer getThreatenToHurt() {
        return threatenToHurt;
    }

    public void setThreatenToHurt(Integer threatenToHurt) {
        this.threatenToHurt = threatenToHurt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHangOutSpots() {
        return hangOutSpots;
    }

    public void setHangOutSpots(String hangOutSpots) {
        this.hangOutSpots = hangOutSpots;
    }

    public Integer getRelativeToIndexClient() {
        return relativeToIndexClient;
    }

    public void setRelativeToIndexClient(Integer relativeToIndexClient) {
        this.relativeToIndexClient = relativeToIndexClient;
    }

    public Integer getCurrentlyLiveWithPartner() {
        return currentlyLiveWithPartner;
    }

    public void setCurrentlyLiveWithPartner(Integer currentlyLiveWithPartner) {
        this.currentlyLiveWithPartner = currentlyLiveWithPartner;
    }

    public Integer getPartnerTestedPositive() {
        return partnerTestedPositive;
    }

    public void setPartnerTestedPositive(Integer partnerTestedPositive) {
        this.partnerTestedPositive = partnerTestedPositive;
    }

    public Integer getSexuallyUncomfortable() {
        return sexuallyUncomfortable;
    }

    public void setSexuallyUncomfortable(Integer sexuallyUncomfortable) {
        this.sexuallyUncomfortable = sexuallyUncomfortable;
    }

    public Integer getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(Integer notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public String getDatePartnerCameForTesting() {
        return datePartnerCameForTesting;
    }

    public void setDatePartnerCameForTesting(String datePartnerCameForTesting) {
        this.datePartnerCameForTesting = datePartnerCameForTesting;
    }

    public boolean isDateOfBirthEstimated() {
        return isDateOfBirthEstimated;
    }

    public void setDateOfBirthEstimated(boolean dateOfBirthEstimated) {
        isDateOfBirthEstimated = dateOfBirthEstimated;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getElicited() {
        return elicited;
    }

    public void setElicited(Integer elicited) {
        this.elicited = elicited;
    }

    public String getDateTested() {
        return dateTested;
    }

    public void setDateTested(String dateTested) {
        this.dateTested = dateTested;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getLga() {
        return lga;
    }

    public void setLga(Integer lga) {
        this.lga = lga;
    }

    public String getCurrentHivStatus() {
        return currentHivStatus;
    }

    public void setCurrentHivStatus(String currentHivStatus) {
        this.currentHivStatus = currentHivStatus;
    }
}
