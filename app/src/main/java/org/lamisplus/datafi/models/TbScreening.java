package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TbScreening implements Serializable {

    @SerializedName("currentCough")
    private String currentCough;

    @SerializedName("fever")
    private String fever;

    @SerializedName("lymphadenopathy")
    private String lymphadenopathy;

    @SerializedName("nightSweats")
    private String nightSweats;

    @SerializedName("weightLoss")
    private String weightLoss;

    public String isCurrentCough() {
        return currentCough;
    }

    public void setCurrentCough(String currentCough) {
        this.currentCough = currentCough;
    }

    public String isFever() {
        return fever;
    }

    public void setFever(String fever) {
        this.fever = fever;
    }

    public String isLymphadenopathy() {
        return lymphadenopathy;
    }

    public void setLymphadenopathy(String lymphadenopathy) {
        this.lymphadenopathy = lymphadenopathy;
    }

    public String isNightSweats() {
        return nightSweats;
    }

    public void setNightSweats(String nightSweats) {
        this.nightSweats = nightSweats;
    }

    public String isWeightLoss() {
        return weightLoss;
    }

    public void setWeightLoss(String weightLoss) {
        this.weightLoss = weightLoss;
    }
}
