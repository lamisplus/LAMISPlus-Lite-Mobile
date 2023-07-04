package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InfantRegistration implements Serializable {

    @SerializedName("ancNo")
    @Expose
    private String ancNo;

    @SerializedName("dateOfDelivery")
    @Expose
    private String dateOfDelivery;

    @SerializedName("dateOfinfantInfo")
    @Expose
    private String dateOfinfantInfo;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("hospitalNumber")
    @Expose
    private String hospitalNumber;

    @SerializedName("middleName")
    @Expose
    private String middleName;

    @SerializedName("nin")
    @Expose
    private String nin;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    public String getAncNo() {
        return ancNo;
    }

    public void setAncNo(String ancNo) {
        this.ancNo = ancNo;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getDateOfinfantInfo() {
        return dateOfinfantInfo;
    }

    public void setDateOfinfantInfo(String dateOfinfantInfo) {
        this.dateOfinfantInfo = dateOfinfantInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHospitalNumber() {
        return hospitalNumber;
    }

    public void setHospitalNumber(String hospitalNumber) {
        this.hospitalNumber = hospitalNumber;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
