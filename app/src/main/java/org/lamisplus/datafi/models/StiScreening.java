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

    public String isComplaintsGenitalSore() {
        return complaintsGenitalSore;
    }

    public void setComplaintsGenitalSore(String complaintsGenitalSore) {
        this.complaintsGenitalSore = complaintsGenitalSore;
    }

    public String isComplaintsOfScrotal() {
        return complaintsOfScrotal;
    }

    public void setComplaintsOfScrotal(String complaintsOfScrotal) {
        this.complaintsOfScrotal = complaintsOfScrotal;
    }

    public String isUrethralDischarge() {
        return urethralDischarge;
    }

    public void setUrethralDischarge(String urethralDischarge) {
        this.urethralDischarge = urethralDischarge;
    }
}
