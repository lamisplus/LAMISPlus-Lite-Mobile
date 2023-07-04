package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PartnerRegistration implements Serializable {

    @SerializedName("ancNo")
    @Expose
    private String ancNo;

    @SerializedName("hivStatus")
    @Expose
    private String hivStatus;

    @SerializedName("acceptHivTest")
    @Expose
    private String acceptHivTest;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("hbStatus")
    @Expose
    private String hbStatus;

    @SerializedName("hcStatus")
    @Expose
    private String hcStatus;

    @SerializedName("postTestCounseled")
    @Expose
    private String postTestCounseled;

    @SerializedName("preTestCounseled")
    @Expose
    private String preTestCounseled;

    @SerializedName("referredTo")
    @Expose
    private String referredTo;

    @SerializedName("referredToOthers")
    @Expose
    private String referredToOthers;

    @SerializedName("syphillisStatus")
    @Expose
    private String syphillisStatus;

    public String getAncNo() {
        return ancNo;
    }

    public void setAncNo(String ancNo) {
        this.ancNo = ancNo;
    }

    public String getHivStatus() {
        return hivStatus;
    }

    public void setHivStatus(String hivStatus) {
        this.hivStatus = hivStatus;
    }

    public String getAcceptHivTest() {
        return acceptHivTest;
    }

    public void setAcceptHivTest(String acceptHivTest) {
        this.acceptHivTest = acceptHivTest;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHbStatus() {
        return hbStatus;
    }

    public void setHbStatus(String hbStatus) {
        this.hbStatus = hbStatus;
    }

    public String getHcStatus() {
        return hcStatus;
    }

    public void setHcStatus(String hcStatus) {
        this.hcStatus = hcStatus;
    }

    public String getPostTestCounseled() {
        return postTestCounseled;
    }

    public void setPostTestCounseled(String postTestCounseled) {
        this.postTestCounseled = postTestCounseled;
    }

    public String getPreTestCounseled() {
        return preTestCounseled;
    }

    public void setPreTestCounseled(String preTestCounseled) {
        this.preTestCounseled = preTestCounseled;
    }

    public String getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(String referredTo) {
        this.referredTo = referredTo;
    }

    public String getReferredToOthers() {
        return referredToOthers;
    }

    public void setReferredToOthers(String referredToOthers) {
        this.referredToOthers = referredToOthers;
    }

    public String getSyphillisStatus() {
        return syphillisStatus;
    }

    public void setSyphillisStatus(String syphillisStatus) {
        this.syphillisStatus = syphillisStatus;
    }
}
