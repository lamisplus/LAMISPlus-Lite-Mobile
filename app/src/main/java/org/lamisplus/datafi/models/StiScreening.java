package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StiScreening implements Serializable {

    @SerializedName("complaintsGenitalSore")
    private String complaintsGenitalSore;

    @SerializedName("complaintsOfScrotal")
    private String complaintsOfScrotal;

    @SerializedName("urethralDischarge")
    private String urethralDischarge;

    @SerializedName("lowerAbdominalPains")
    private String lowerAbdominalPains;

    @SerializedName("vaginalDischarge")
    private String vaginalDischarge;


    public void setComplaintsGenitalSore(String complaintsGenitalSore) {
        this.complaintsGenitalSore = complaintsGenitalSore;
    }

    public void setComplaintsOfScrotal(String complaintsOfScrotal) {
        this.complaintsOfScrotal = complaintsOfScrotal;
    }

    public void setUrethralDischarge(String urethralDischarge) {
        this.urethralDischarge = urethralDischarge;
    }

    public String getComplaintsGenitalSore() {
        return complaintsGenitalSore;
    }

    public String getComplaintsOfScrotal() {
        return complaintsOfScrotal;
    }

    public String getUrethralDischarge() {
        return urethralDischarge;
    }

    public String getLowerAbdominalPains() {
        return lowerAbdominalPains;
    }

    public void setLowerAbdominalPains(String lowerAbdominalPains) {
        this.lowerAbdominalPains = lowerAbdominalPains;
    }

    public String getVaginalDischarge() {
        return vaginalDischarge;
    }

    public void setVaginalDischarge(String vaginalDischarge) {
        this.vaginalDischarge = vaginalDischarge;
    }
}
