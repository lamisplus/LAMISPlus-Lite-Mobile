package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Elicitation implements Serializable {

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
    private int sex;

    @SerializedName("htsClientId")
    @Expose
    private int htsClientId;

    @SerializedName("physicalHurt")
    @Expose
    private int physicalHurt;

    @SerializedName("threatenToHurt")
    @Expose
    private int threatenToHurt;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("hangOutSpots")
    @Expose
    private String hangOutSpots;

    @SerializedName("relativeToIndexClient")
    @Expose
    private int relativeToIndexClient;

    @SerializedName("currentlyLiveWithPartner")
    @Expose
    private int currentlyLiveWithPartner;

    @SerializedName("partnerTestedPositive")
    @Expose
    private int partnerTestedPositive;

    @SerializedName("sexuallyUncomfortable")
    @Expose
    private int sexuallyUncomfortable;

    @SerializedName("notificationMethod")
    @Expose
    private int notificationMethod;

    @SerializedName("datePartnerCameForTesting")
    @Expose
    private String datePartnerCameForTesting;

    @SerializedName("isDateOfBirthEstimated")
    @Expose
    private boolean isDateOfBirthEstimated;

    @SerializedName("age")
    @Expose
    private String age;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(int htsClientId) {
        this.htsClientId = htsClientId;
    }

    public int getPhysicalHurt() {
        return physicalHurt;
    }

    public void setPhysicalHurt(int physicalHurt) {
        this.physicalHurt = physicalHurt;
    }

    public int getThreatenToHurt() {
        return threatenToHurt;
    }

    public void setThreatenToHurt(int threatenToHurt) {
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

    public int getRelativeToIndexClient() {
        return relativeToIndexClient;
    }

    public void setRelativeToIndexClient(int relativeToIndexClient) {
        this.relativeToIndexClient = relativeToIndexClient;
    }

    public int getCurrentlyLiveWithPartner() {
        return currentlyLiveWithPartner;
    }

    public void setCurrentlyLiveWithPartner(int currentlyLiveWithPartner) {
        this.currentlyLiveWithPartner = currentlyLiveWithPartner;
    }

    public int getPartnerTestedPositive() {
        return partnerTestedPositive;
    }

    public void setPartnerTestedPositive(int partnerTestedPositive) {
        this.partnerTestedPositive = partnerTestedPositive;
    }

    public int getSexuallyUncomfortable() {
        return sexuallyUncomfortable;
    }

    public void setSexuallyUncomfortable(int sexuallyUncomfortable) {
        this.sexuallyUncomfortable = sexuallyUncomfortable;
    }

    public int getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(int notificationMethod) {
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
}
