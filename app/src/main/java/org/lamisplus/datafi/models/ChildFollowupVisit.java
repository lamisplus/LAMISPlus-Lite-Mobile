package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChildFollowupVisit implements Serializable {

    @SerializedName("infantVisitRequestDto")
    private ChildFollowupVisit.InfantVisitRequestDto infantVisitRequestDto;

    @SerializedName("infantArvDto")
    private InfantArvDto infantArvDto;

    @SerializedName("infantMotherArtDto")
    private InfantMotherArtDto infantMotherArtDto;

    @SerializedName("infantPCRTestDto")
    private InfantPCRTestDto infantPCRTestDto;

    public InfantVisitRequestDto getInfantVisitRequestDto() {
        return infantVisitRequestDto;
    }

    public void setInfantVisitRequestDto(InfantVisitRequestDto infantVisitRequestDto) {
        this.infantVisitRequestDto = infantVisitRequestDto;
    }

    public InfantArvDto getInfantArvDto() {
        return infantArvDto;
    }

    public void setInfantArvDto(InfantArvDto infantArvDto) {
        this.infantArvDto = infantArvDto;
    }

    public InfantMotherArtDto getInfantMotherArtDto() {
        return infantMotherArtDto;
    }

    public void setInfantMotherArtDto(InfantMotherArtDto infantMotherArtDto) {
        this.infantMotherArtDto = infantMotherArtDto;
    }

    public InfantPCRTestDto getInfantPCRTestDto() {
        return infantPCRTestDto;
    }

    public void setInfantPCRTestDto(InfantPCRTestDto infantPCRTestDto) {
        this.infantPCRTestDto = infantPCRTestDto;
    }

    public static class InfantVisitRequestDto implements Serializable {
        @SerializedName("ageAtCtx")
        private String ageAtCtx;

        @SerializedName("ancNumber")
        private String ancNumber;

        @SerializedName("bodyWeight")
        private String bodyWeight;

        @SerializedName("breastFeeding")
        private String breastFeeding;

        @SerializedName("ctxStatus")
        private String ctxStatus;

        @SerializedName("infantHospitalNumber")
        private String infantHospitalNumber;

        @SerializedName("uuid")
        private String uuid;

        @SerializedName("visitDate")
        private String visitDate;

        @SerializedName("visitStatus")
        private String visitStatus;

        @SerializedName("infantOutcomeAt18Months")
        private String infantOutcomeAt18Months;

        public String getAgeAtCtx() {
            return ageAtCtx;
        }

        public void setAgeAtCtx(String ageAtCtx) {
            this.ageAtCtx = ageAtCtx;
        }

        public String getAncNumber() {
            return ancNumber;
        }

        public void setAncNumber(String ancNumber) {
            this.ancNumber = ancNumber;
        }

        public String getBodyWeight() {
            return bodyWeight;
        }

        public void setBodyWeight(String bodyWeight) {
            this.bodyWeight = bodyWeight;
        }

        public String getBreastFeeding() {
            return breastFeeding;
        }

        public void setBreastFeeding(String breastFeeding) {
            this.breastFeeding = breastFeeding;
        }

        public String getCtxStatus() {
            return ctxStatus;
        }

        public void setCtxStatus(String ctxStatus) {
            this.ctxStatus = ctxStatus;
        }

        public String getInfantHospitalNumber() {
            return infantHospitalNumber;
        }

        public void setInfantHospitalNumber(String infantHospitalNumber) {
            this.infantHospitalNumber = infantHospitalNumber;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public String getVisitStatus() {
            return visitStatus;
        }

        public void setVisitStatus(String visitStatus) {
            this.visitStatus = visitStatus;
        }

        public String getInfantOutcomeAt18Months() {
            return infantOutcomeAt18Months;
        }

        public void setInfantOutcomeAt18Months(String infantOutcomeAt18Months) {
            this.infantOutcomeAt18Months = infantOutcomeAt18Months;
        }
    }

    public static class InfantArvDto implements Serializable {
        @SerializedName("ageAtCtx")
        private String ageAtCtx;

        @SerializedName("visitDate")
        private String visitDate;

        @SerializedName("infantHospitalNumber")
        private String infantHospitalNumber;

        @SerializedName("ancNumber")
        private String ancNumber;

        @SerializedName("arvDeliveryPoint")
        private String arvDeliveryPoint;

        @SerializedName("infantArvTime")
        private String infantArvTime;

        @SerializedName("infantArvType")
        private String infantArvType;

        public String getAgeAtCtx() {
            return ageAtCtx;
        }

        public void setAgeAtCtx(String ageAtCtx) {
            this.ageAtCtx = ageAtCtx;
        }

        public String getAncNumber() {
            return ancNumber;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public String getInfantHospitalNumber() {
            return infantHospitalNumber;
        }

        public void setInfantHospitalNumber(String infantHospitalNumber) {
            this.infantHospitalNumber = infantHospitalNumber;
        }

        public void setAncNumber(String ancNumber) {
            this.ancNumber = ancNumber;
        }

        public String getArvDeliveryPoint() {
            return arvDeliveryPoint;
        }

        public void setArvDeliveryPoint(String arvDeliveryPoint) {
            this.arvDeliveryPoint = arvDeliveryPoint;
        }

        public String getInfantArvTime() {
            return infantArvTime;
        }

        public void setInfantArvTime(String infantArvTime) {
            this.infantArvTime = infantArvTime;
        }

        public String getInfantArvType() {
            return infantArvType;
        }

        public void setInfantArvType(String infantArvType) {
            this.infantArvType = infantArvType;
        }
    }

    public static class InfantMotherArtDto implements Serializable {
        @SerializedName("ancNumber")
        private String ancNumber;

        @SerializedName("motherArtInitiationTime")
        private String motherArtInitiationTime;

        @SerializedName("motherArtRegimen")
        private String motherArtRegimen;

        @SerializedName("regimenTypeId")
        private Integer regimenTypeId;

        @SerializedName("regimenId")
        private Integer regimenId;

        public String getAncNumber() {
            return ancNumber;
        }

        public void setAncNumber(String ancNumber) {
            this.ancNumber = ancNumber;
        }

        public String getMotherArtInitiationTime() {
            return motherArtInitiationTime;
        }

        public void setMotherArtInitiationTime(String motherArtInitiationTime) {
            this.motherArtInitiationTime = motherArtInitiationTime;
        }

        public String getMotherArtRegimen() {
            return motherArtRegimen;
        }

        public void setMotherArtRegimen(String motherArtRegimen) {
            this.motherArtRegimen = motherArtRegimen;
        }

        public Integer getRegimenTypeId() {
            return regimenTypeId;
        }

        public void setRegimenTypeId(Integer regimenTypeId) {
            this.regimenTypeId = regimenTypeId;
        }

        public Integer getRegimenId() {
            return regimenId;
        }

        public void setRegimenId(Integer regimenId) {
            this.regimenId = regimenId;
        }
    }

    public static class InfantPCRTestDto implements Serializable {
        @SerializedName("ageAtTest")
        private Integer ageAtTest = 0;

        @SerializedName("visitDate")
        private String visitDate;

        @SerializedName("infantHospitalNumber")
        private String infantHospitalNumber;

        @SerializedName("ancNumber")
        private String ancNumber;

        @SerializedName("dateResultReceivedAtFacility")
        private String dateResultReceivedAtFacility;

        @SerializedName("dateResultReceivedByCaregiver")
        private String dateResultReceivedByCaregiver;

        @SerializedName("dateSampleCollected")
        private String dateSampleCollected;

        @SerializedName("dateSampleSent")
        private String dateSampleSent;

        @SerializedName("results")
        private String results;

        @SerializedName("testType")
        private String testType;

        public Integer getAgeAtTest() {
            return ageAtTest;
        }

        public void setAgeAtTest(Integer ageAtTest) {
            this.ageAtTest = ageAtTest;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public String getInfantHospitalNumber() {
            return infantHospitalNumber;
        }

        public void setInfantHospitalNumber(String infantHospitalNumber) {
            this.infantHospitalNumber = infantHospitalNumber;
        }

        public String getAncNumber() {
            return ancNumber;
        }

        public void setAncNumber(String ancNumber) {
            this.ancNumber = ancNumber;
        }

        public String getDateResultReceivedAtFacility() {
            return dateResultReceivedAtFacility;
        }

        public void setDateResultReceivedAtFacility(String dateResultReceivedAtFacility) {
            this.dateResultReceivedAtFacility = dateResultReceivedAtFacility;
        }

        public String getDateResultReceivedByCaregiver() {
            return dateResultReceivedByCaregiver;
        }

        public void setDateResultReceivedByCaregiver(String dateResultReceivedByCaregiver) {
            this.dateResultReceivedByCaregiver = dateResultReceivedByCaregiver;
        }

        public String getDateSampleCollected() {
            return dateSampleCollected;
        }

        public void setDateSampleCollected(String dateSampleCollected) {
            this.dateSampleCollected = dateSampleCollected;
        }

        public String getDateSampleSent() {
            return dateSampleSent;
        }

        public void setDateSampleSent(String dateSampleSent) {
            this.dateSampleSent = dateSampleSent;
        }

        public String getResults() {
            return results;
        }

        public void setResults(String results) {
            this.results = results;
        }

        public String getTestType() {
            return testType;
        }

        public void setTestType(String testType) {
            this.testType = testType;
        }
    }
}
