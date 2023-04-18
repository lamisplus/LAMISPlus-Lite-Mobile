package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PMTCTEnrollment implements Serializable {

    @SerializedName("ancNo")
    @Expose
    private String ancNo;

    @SerializedName("artStartDate")
    @Expose
    private String artStartDate;

    @SerializedName("artStartTime")
    @Expose
    private String artStartTime;

    @SerializedName("entryPoint")
    @Expose
    private String entryPoint;

    @SerializedName("gaweeks")
    @Expose
    private String gaweeks;

    @SerializedName("gravida")
    @Expose
    private String gravida;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("pmtctEnrollmentDate")
    @Expose
    private String pmtctEnrollmentDate;

    @SerializedName("tbStatus")
    @Expose
    private String tbStatus;

    public String getAncNo() {
        return ancNo;
    }

    public void setAncNo(String ancNo) {
        this.ancNo = ancNo;
    }

    public String getArtStartDate() {
        return artStartDate;
    }

    public void setArtStartDate(String artStartDate) {
        this.artStartDate = artStartDate;
    }

    public String getArtStartTime() {
        return artStartTime;
    }

    public void setArtStartTime(String artStartTime) {
        this.artStartTime = artStartTime;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPmtctEnrollmentDate() {
        return pmtctEnrollmentDate;
    }

    public void setPmtctEnrollmentDate(String pmtctEnrollmentDate) {
        this.pmtctEnrollmentDate = pmtctEnrollmentDate;
    }

    public String getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus;
    }
}
