package org.lamisplus.datafi.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.lamisplus.datafi.models.Biometrics;

import java.util.ArrayList;
import java.util.List;

public class BiometricsClass{
    @SerializedName("patientUUID")
    @Expose
    private String PatientUUID;

    @SerializedName("fingerPrintList")
    @Expose
    private List<Biometrics> FingerPrintList;


    public String getPatientUUID() {
        return PatientUUID;
    }
    public void setPatientUUID(String patientUUID) {
        this.PatientUUID = patientUUID;
    }

    public List<Biometrics> getFingerPrintList() {
        return FingerPrintList;
    }
    public void setFingerPrintList(ArrayList<Biometrics> fingerPrintList)   {
        this.FingerPrintList = fingerPrintList;
    }
}
