package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "biometricsrecapture")
public class BiometricsRecapture extends Model implements Serializable {

    @Column(name = "patientId")
    @SerializedName("patientId")
    @Expose
    private Integer patientId;

    //This is the person id from the person table and should not be confused with the patientId column from the server
    @Column(name = "person")
    @SerializedName("person")
    private Integer person;

    @SerializedName("capturedBiometricsList")
    private transient List<BiometricsList> biometricsList = new ArrayList<>();

    @Column(name = "capturedBiometricsList")
    @Expose
    private String capturedBiometricsList;

    @Column(name = "deviceName")
    @SerializedName("deviceName")
    @Expose
    private String deviceName;

    @Column(name = "biometricType")
    @SerializedName("biometricType")
    @Expose
    private String biometricType;

    @Column(name = "iso")
    @SerializedName("iso")
    @Expose
    private boolean iso;

    @Column(name = "type")
    @SerializedName("type")
    @Expose
    private String type;

    @Column(name = "reason")
    @SerializedName("reason")
    @Expose
    private String reason;

    @Column(name = "syncStatus")
    @SerializedName("syncStatus")
    private int SyncStatus;

    @Column(name = "dateTime")
    @SerializedName("dateTime")
    private String dateTime;


    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public String getCapturedBiometricsList() {
        return capturedBiometricsList;
    }

    public void setCapturedBiometricsList(String capturedBiometricsList) {
        this.capturedBiometricsList = capturedBiometricsList;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBiometricType() {
        return biometricType;
    }

    public void setBiometricType(String biometricType) {
        this.biometricType = biometricType;
    }

    public boolean isIso() {
        return iso;
    }

    public void setIso(boolean iso) {
        this.iso = iso;
    }

    public int getSyncStatus() {
        return SyncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        SyncStatus = syncStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
