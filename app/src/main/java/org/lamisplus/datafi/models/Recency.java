package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Recency implements Serializable {

    @SerializedName("personId")
    private int personId;

    @SerializedName("htsClientId")
    private int htsClientId;

    @SerializedName("recency")
    private RecencyDetails recencyDetails;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(int htsClientId) {
        this.htsClientId = htsClientId;
    }

    public RecencyDetails getRecencyDetails() {
        return recencyDetails;
    }

    public void setRecencyDetails(RecencyDetails recencyDetails) {
        this.recencyDetails = recencyDetails;
    }

    public static class RecencyDetails{
        @SerializedName("optOutRTRI")
        private String optOutRTRI;

        @SerializedName("optOutRTRITestName")
        private String optOutRTRITestName;

        @SerializedName("optOutRTRITestDate")
        private String optOutRTRITestDate;

        @SerializedName("rencencyId")
        private String rencencyId;

        @SerializedName("controlLine")
        private String controlLine;

        @SerializedName("verififcationLine")
        private String verififcationLine;

        @SerializedName("longTermLine")
        private String longTermLine;

        @SerializedName("rencencyInterpretation")
        private String rencencyInterpretation;

        @SerializedName("hasViralLoad")
        private String hasViralLoad;

        @SerializedName("sampleCollectedDate")
        private String sampleCollectedDate;

        @SerializedName("sampleReferanceNumber")
        private String sampleReferanceNumber;

        @SerializedName("dateSampleSentToPCRLab")
        private String dateSampleSentToPCRLab;

        @SerializedName("sampleTestDate")
        private String sampleTestDate;

        @SerializedName("sampleType")
        private String sampleType;

        @SerializedName("receivingPcrLab")
        private String receivingPcrLab;

        @SerializedName("viralLoadResultClassification")
        private String viralLoadResultClassification;

        @SerializedName("recencyResult")
        private String recencyResult;

        @SerializedName("finalRecencyResult")
        private String finalRecencyResult;

        public String getOptOutRTRI() {
            return optOutRTRI;
        }

        public void setOptOutRTRI(String optOutRTRI) {
            this.optOutRTRI = optOutRTRI;
        }

        public String getOptOutRTRITestName() {
            return optOutRTRITestName;
        }

        public void setOptOutRTRITestName(String optOutRTRITestName) {
            this.optOutRTRITestName = optOutRTRITestName;
        }

        public String getOptOutRTRITestDate() {
            return optOutRTRITestDate;
        }

        public void setOptOutRTRITestDate(String optOutRTRITestDate) {
            this.optOutRTRITestDate = optOutRTRITestDate;
        }

        public String getRencencyId() {
            return rencencyId;
        }

        public void setRencencyId(String rencencyId) {
            this.rencencyId = rencencyId;
        }

        public String getControlLine() {
            return controlLine;
        }

        public void setControlLine(String controlLine) {
            this.controlLine = controlLine;
        }

        public String getVerififcationLine() {
            return verififcationLine;
        }

        public void setVerififcationLine(String verififcationLine) {
            this.verififcationLine = verififcationLine;
        }

        public String getLongTermLine() {
            return longTermLine;
        }

        public void setLongTermLine(String longTermLine) {
            this.longTermLine = longTermLine;
        }

        public String getRencencyInterpretation() {
            return rencencyInterpretation;
        }

        public void setRencencyInterpretation(String rencencyInterpretation) {
            this.rencencyInterpretation = rencencyInterpretation;
        }

        public String getHasViralLoad() {
            return hasViralLoad;
        }

        public void setHasViralLoad(String hasViralLoad) {
            this.hasViralLoad = hasViralLoad;
        }

        public String getSampleCollectedDate() {
            return sampleCollectedDate;
        }

        public void setSampleCollectedDate(String sampleCollectedDate) {
            this.sampleCollectedDate = sampleCollectedDate;
        }

        public String getSampleReferanceNumber() {
            return sampleReferanceNumber;
        }

        public void setSampleReferanceNumber(String sampleReferanceNumber) {
            this.sampleReferanceNumber = sampleReferanceNumber;
        }

        public String getDateSampleSentToPCRLab() {
            return dateSampleSentToPCRLab;
        }

        public void setDateSampleSentToPCRLab(String dateSampleSentToPCRLab) {
            this.dateSampleSentToPCRLab = dateSampleSentToPCRLab;
        }

        public String getSampleTestDate() {
            return sampleTestDate;
        }

        public void setSampleTestDate(String sampleTestDate) {
            this.sampleTestDate = sampleTestDate;
        }

        public String getSampleType() {
            return sampleType;
        }

        public void setSampleType(String sampleType) {
            this.sampleType = sampleType;
        }

        public String getReceivingPcrLab() {
            return receivingPcrLab;
        }

        public void setReceivingPcrLab(String receivingPcrLab) {
            this.receivingPcrLab = receivingPcrLab;
        }

        public String getViralLoadResultClassification() {
            return viralLoadResultClassification;
        }

        public void setViralLoadResultClassification(String viralLoadResultClassification) {
            this.viralLoadResultClassification = viralLoadResultClassification;
        }

        public String getRecencyResult() {
            return recencyResult;
        }

        public void setRecencyResult(String recencyResult) {
            this.recencyResult = recencyResult;
        }

        public String getFinalRecencyResult() {
            return finalRecencyResult;
        }

        public void setFinalRecencyResult(String finalRecencyResult) {
            this.finalRecencyResult = finalRecencyResult;
        }
    }

}
