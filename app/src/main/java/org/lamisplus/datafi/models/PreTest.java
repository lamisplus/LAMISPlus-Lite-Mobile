package org.lamisplus.datafi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PreTest implements Serializable {

    @SerializedName("htsClientId")
    private int htsClientId;

    @SerializedName("personId")
    private int personId;

    @SerializedName("knowledgeAssessment")
    private KnowledgeAssessment knowledgeAssessment;

    @SerializedName("riskAssessment")
    private RiskAssessment riskAssessment;

    @SerializedName("sexPartnerRiskAssessment")
    private SexPartnerRiskAssessment sexPartnerRiskAssessment;

    @SerializedName("stiScreening")
    private StiScreening stiScreening;

    @SerializedName("tbScreening")
    private TbScreening tbScreening;

    public int getHtsClientId() {
        return htsClientId;
    }

    public void setHtsClientId(int htsClientId) {
        this.htsClientId = htsClientId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public KnowledgeAssessment getKnowledgeAssessment() {
        return knowledgeAssessment;
    }

    public void setKnowledgeAssessment(KnowledgeAssessment knowledgeAssessment) {
        this.knowledgeAssessment = knowledgeAssessment;
    }

    public RiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public StiScreening getStiScreening() {
        return stiScreening;
    }

    public void setStiScreening(StiScreening stiScreening) {
        this.stiScreening = stiScreening;
    }

    public TbScreening getTbScreening() {
        return tbScreening;
    }

    public void setTbScreening(TbScreening tbScreening) {
        this.tbScreening = tbScreening;
    }

    public SexPartnerRiskAssessment getSexPartnerRiskAssessment() {
        return sexPartnerRiskAssessment;
    }

    public void setSexPartnerRiskAssessment(SexPartnerRiskAssessment sexPartnerRiskAssessment) {
        this.sexPartnerRiskAssessment = sexPartnerRiskAssessment;
    }
}
