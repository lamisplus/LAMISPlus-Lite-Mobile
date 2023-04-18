package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LabourDelivery implements Serializable {

    @SerializedName("ancNo")
    @Expose
    private String ancNo;

    @SerializedName("artStartedLdWard")
    @Expose
    private String artStartedLdWard;

    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;

    @SerializedName("childGivenArvWithin72")
    @Expose
    private String childGivenArvWithin72;

    @SerializedName("childStatus")
    @Expose
    private String childStatus;

    @SerializedName("dateOfDelivery")
    @Expose
    private String dateOfDelivery;

    @SerializedName("deliveryTime")
    @Expose
    private String deliveryTime;

    @SerializedName("episiotomy")
    @Expose
    private String episiotomy;

    @SerializedName("feedingDecision")
    @Expose
    private String feedingDecision;

    @SerializedName("gaweeks")
    @Expose
    private Integer gaweeks;

    @SerializedName("hbstatus")
    @Expose
    private String hbstatus;

    @SerializedName("hcstatus")
    @Expose
    private String hcstatus;

    @SerializedName("hivExposedInfantGivenHbWithin24hrs")
    @Expose
    private String hivExposedInfantGivenHbWithin24hrs;

    @SerializedName("maternalOutcome")
    @Expose
    private String maternalOutcome;

    @SerializedName("modeOfDelivery")
    @Expose
    private String modeOfDelivery;

    @SerializedName("numberOfInfantsAlive")
    @Expose
    private Integer numberOfInfantsAlive;

    @SerializedName("numberOfInfantsDead")
    @Expose
    private Integer numberOfInfantsDead;

    @SerializedName("onArt")
    @Expose
    private String onArt;

    @SerializedName("referalSource")
    @Expose
    private String referalSource;

    @SerializedName("romDeliveryInterval")
    @Expose
    private String romDeliveryInterval;

    @SerializedName("vaginalTear")
    @Expose
    private String vaginalTear;

    public String getAncNo() {
        return ancNo;
    }

    public void setAncNo(String ancNo) {
        this.ancNo = ancNo;
    }

    public String getArtStartedLdWard() {
        return artStartedLdWard;
    }

    public void setArtStartedLdWard(String artStartedLdWard) {
        this.artStartedLdWard = artStartedLdWard;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getChildGivenArvWithin72() {
        return childGivenArvWithin72;
    }

    public void setChildGivenArvWithin72(String childGivenArvWithin72) {
        this.childGivenArvWithin72 = childGivenArvWithin72;
    }

    public String getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(String childStatus) {
        this.childStatus = childStatus;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getEpisiotomy() {
        return episiotomy;
    }

    public void setEpisiotomy(String episiotomy) {
        this.episiotomy = episiotomy;
    }

    public String getFeedingDecision() {
        return feedingDecision;
    }

    public void setFeedingDecision(String feedingDecision) {
        this.feedingDecision = feedingDecision;
    }

    public Integer getGaweeks() {
        return gaweeks;
    }

    public void setGaweeks(Integer gaweeks) {
        this.gaweeks = gaweeks;
    }

    public String getHbstatus() {
        return hbstatus;
    }

    public void setHbstatus(String hbstatus) {
        this.hbstatus = hbstatus;
    }

    public String getHcstatus() {
        return hcstatus;
    }

    public void setHcstatus(String hcstatus) {
        this.hcstatus = hcstatus;
    }

    public String getHivExposedInfantGivenHbWithin24hrs() {
        return hivExposedInfantGivenHbWithin24hrs;
    }

    public void setHivExposedInfantGivenHbWithin24hrs(String hivExposedInfantGivenHbWithin24hrs) {
        this.hivExposedInfantGivenHbWithin24hrs = hivExposedInfantGivenHbWithin24hrs;
    }

    public String getMaternalOutcome() {
        return maternalOutcome;
    }

    public void setMaternalOutcome(String maternalOutcome) {
        this.maternalOutcome = maternalOutcome;
    }

    public String getModeOfDelivery() {
        return modeOfDelivery;
    }

    public void setModeOfDelivery(String modeOfDelivery) {
        this.modeOfDelivery = modeOfDelivery;
    }

    public Integer getNumberOfInfantsAlive() {
        return numberOfInfantsAlive;
    }

    public void setNumberOfInfantsAlive(Integer numberOfInfantsAlive) {
        this.numberOfInfantsAlive = numberOfInfantsAlive;
    }

    public Integer getNumberOfInfantsDead() {
        return numberOfInfantsDead;
    }

    public void setNumberOfInfantsDead(Integer numberOfInfantsDead) {
        this.numberOfInfantsDead = numberOfInfantsDead;
    }

    public String getOnArt() {
        return onArt;
    }

    public void setOnArt(String onArt) {
        this.onArt = onArt;
    }

    public String getReferalSource() {
        return referalSource;
    }

    public void setReferalSource(String referalSource) {
        this.referalSource = referalSource;
    }

    public String getRomDeliveryInterval() {
        return romDeliveryInterval;
    }

    public void setRomDeliveryInterval(String romDeliveryInterval) {
        this.romDeliveryInterval = romDeliveryInterval;
    }

    public String getVaginalTear() {
        return vaginalTear;
    }

    public void setVaginalTear(String vaginalTear) {
        this.vaginalTear = vaginalTear;
    }
}
