package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.lamisplus.datafi.R;

import java.io.Serializable;

public class Address implements Serializable {

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("countryId")
    @Expose
    private Integer countryId = 1;

    @SerializedName("district")
    @Expose
    private String district;

    @SerializedName("line")
    @Expose
    private String[] line;

    @SerializedName("organisationUnitId")
    @Expose
    private String organisationUnitId;

    @SerializedName("postalCode")
    @Expose
    private String postalCode;

    @SerializedName("stateId")
    @Expose
    private Integer stateId = null;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String[] getLine() {
        return line;
    }

    public void setLine(String[] line) {
        this.line = line;
    }

    public String getOrganisationUnitId() {
        return organisationUnitId;
    }

    public void setOrganisationUnitId(String organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }
}
