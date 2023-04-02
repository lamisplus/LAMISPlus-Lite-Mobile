package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "account")
public class Account extends Model implements Serializable {

    @SerializedName("username")
    @Column(name = "username")
    @Expose
    private String username;

    @SerializedName("password")
    @Column(name = "password")
    @Expose
    private String password;

    @SerializedName("currentOrganisationUnitId")
    @Column(name = "currentOrganisationUnitId")
    @Expose
    private Integer currentOrganisationUnitId;

    @SerializedName("currentOrganisationUnitName")
    @Column(name = "currentOrganisationUnitName")
    @Expose
    private String currentOrganisationUnitName;

    @SerializedName("email")
    @Column(name = "email")
    @Expose
    private String email;

    @SerializedName("serverUrl")
    @Column(name = "serverUrl")
    @Expose
    private String serverUrl;

    @SerializedName("selected")
    @Column(name = "selected")
    @Expose
    private Integer selected;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCurrentOrganisationUnitId() {
        return currentOrganisationUnitId;
    }

    public void setCurrentOrganisationUnitId(Integer currentOrganisationUnitId) {
        this.currentOrganisationUnitId = currentOrganisationUnitId;
    }

    public String getCurrentOrganisationUnitName() {
        return currentOrganisationUnitName;
    }

    public void setCurrentOrganisationUnitName(String currentOrganisationUnitName) {
        this.currentOrganisationUnitName = currentOrganisationUnitName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}
