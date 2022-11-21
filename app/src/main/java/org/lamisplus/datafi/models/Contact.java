package org.lamisplus.datafi.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.lamisplus.datafi.classes.ContactPointClass;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contact implements Serializable {

    @SerializedName("address")
    @Expose
    private String address;

    private String[] line;

    @SerializedName("contactPoint")
    @Expose
    private String contactPoint;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("relationshipId")
    @Expose
    private int relationshipId;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("otherName")
    @Expose
    private String otherName;

    public String[] getAddress() {
        Type type = new TypeToken<List<Contact.Address>>() {
        }.getType();
        Contact.Address contactAddress = new Gson().fromJson(address, Contact.Address.class);
        return contactAddress.line;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getLine() {
        return line;
    }

    public void setLine(String[] line) {
        this.line = line;
    }

    public List<ContactPoint> getContactPoint() {
        Type type = new TypeToken<List<ContactPoint>>() {
        }.getType();
        List<ContactPoint> contactPoints = new Gson().fromJson(contactPoint, type);
        return contactPoints;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public static class Address {
        private String[] line;

        public Address(String[] line) {
            this.line = line;
        }

        public void setLine(String[] line) {
            this.line = line;
        }

        public String[] getLine() {
            return line;
        }
    }

}
