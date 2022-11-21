package org.lamisplus.datafi.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.lamisplus.datafi.utilities.LamisCustomHandler;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Table(name = "person")
public class Person extends Model implements Serializable {

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Type addressType = new TypeToken<List<Address>>() {
    }.getType();
    private Type contactType = new TypeToken<List<Contact>>() {
    }.getType();
    private Type contactPointType = new TypeToken<List<ContactPoint>>() {
    }.getType();
    private Type identifierType = new TypeToken<List<PatientIdentifier>>() {
    }.getType();

    @Column(name = "synced")
    private boolean synced;

    @Column(name = "personId")
    @SerializedName("personId")
    private String personId;

    @Column(name = "active")
    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("addressList")
    private List<Address> addressList = new ArrayList<>();

    @Column(name = "address")
    @Expose
    private String address;

    @SerializedName("contactList")
    private List<Contact> contactList = new ArrayList<>();

    @Column(name = "contact")
    @Expose
    private String contact;

    @SerializedName("contactPoint")
    private List<ContactPoint> contactPointList = new ArrayList<>();

    @Column(name = "contactPoint")
    @Expose
    private String contactPoint;

    @SerializedName("dateOfBirth")
    @Column(name = "dateOfBirth")
    @Expose
    private String dateOfBirth;

    @SerializedName("deceased")
    @Column(name = "deceased")
    @Expose
    private boolean deceased;

    @SerializedName("deceasedDateTime")
    @Column(name = "deceasedDateTime")
    @Expose
    private String deceasedDateTime;

    @SerializedName("firstName")
    @Column(name = "firstName")
    @Expose
    private String firstName;

    @SerializedName("otherName")
    @Column(name = "otherName")
    @Expose
    private String otherName;

    @SerializedName("genderId")
    @Column(name = "genderId")
    @Expose
    private int genderId;

    @SerializedName("sexId")
    @Column(name = "sexId")
    @Expose
    private int sexId;

    @SerializedName("identifierList")
    private List<PatientIdentifier> identifierList = new ArrayList<>();

    @Column(name = "identifier")
    @Expose
    private String identifier;

    @SerializedName("surname")
    @Column(name = "surname")
    @Expose
    private String surname;

    @SerializedName("educationId")
    @Column(name = "educationId")
    @Expose
    private int educationId;

    @SerializedName("maritalStatusId")
    @Column(name = "maritalStatusId")
    @Expose
    private int maritalStatusId;

    @SerializedName("dateOfRegistration")
    @Column(name = "dateOfRegistration")
    @Expose
    private String dateOfRegistration;

    @SerializedName("employmentStatusId")
    @Column(name = "employmentStatusId")
    @Expose
    private int employmentStatusId;

    @SerializedName("emrId")
    @Column(name = "emrId")
    @Expose
    private String emrId;

    @SerializedName("facilityId")
    @Column(name = "facilityId")
    @Expose
    private int facilityId;

    @SerializedName("isDateOfBirthEstimated")
    @Column(name = "isDateOfBirthEstimated")
    @Expose
    private boolean isDateOfBirthEstimated;

    @SerializedName("ninNumber")
    @Column(name = "ninNumber")
    @Expose
    private String ninNumber;

    @SerializedName("organizationId")
    @Column(name = "organizationId")
    @Expose
    private int organizationId;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Address> getAddress() {
        return addressList;
    }

    public Address getAddresses() {
        if (!address.isEmpty()) {
            return pullAddressList().get(0);
        } else {
            return null;
        }
    }

    public Contact getContacts() {
        if (!contact.isEmpty()) {
            return pullContactList().get(0);
        } else {
            return null;
        }
    }

    public void setAddress(List<Address> addressList) {
        this.addressList = addressList;
    }

    public void setAddresses(Address addresses) {
        addressList = addressList != null ? addressList : new ArrayList<>();
        if (addressList.isEmpty())
            addressList.add(addresses);
        else
            addressList.set(0, addresses);
    }

    public List<Address> pullAddressList() {
        List<Address> addressList = gson.fromJson(address, addressType);
        return addressList;
    }

    public void setAddressList() {
        this.address = gson.toJson(addressList, addressType);
    }

    public List<Contact> getContact() {
        return contactList;
    }

    public void setContact(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Contact> pullContactList() {
        List<Contact> contactList = gson.fromJson(contact, contactType);
        return contactList;
    }

    public void setContactList() {
        this.contact = gson.toJson(contactList, contactType);
    }

    public String getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    public List<ContactPoint> pullContactPointList() {
        Type type = new TypeToken<List<ContactPoint>>() {
        }.getType();
        List<ContactPoint> contactPoints = new Gson().fromJson(contactPoint, type);
        return contactPoints;
    }

    public void setContactPointList() {
        this.contactPoint = gson.toJson(contactPointList, contactPointType);
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean getDeceased() {
        return deceased;
    }

    public void setDeceased(boolean deceased) {
        this.deceased = deceased;
    }

    public String getDeceasedDateTime() {
        return deceasedDateTime;
    }

    public void setDeceasedDateTime(String deceasedDateTime) {
        this.deceasedDateTime = deceasedDateTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getSexId() {
        return sexId;
    }

    public void setSexId(int sexId) {
        this.sexId = sexId;
    }

    public List<PatientIdentifier> getIdentifier() {
        return identifierList;
    }

    public PatientIdentifier getIdentifiers() {
        if (!identifier.isEmpty()) {
            return pullIdentifierList().get(0);
        } else {
            return null;
        }
    }

    public void setIdentifierList(List<PatientIdentifier> identifierList) {
        this.identifierList = identifierList;
    }

    public List<PatientIdentifier> pullIdentifierList() {
        List<PatientIdentifier> identifierList = gson.fromJson(this.identifier, identifierType);
        return identifierList;
    }

    public void setIdentifierList() {
        this.identifier = gson.toJson(identifierList, identifierType);
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

    public int getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStatusId(int maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public int getEducationId() {
        return educationId;
    }

    public void setEducationId(int educationId) {
        this.educationId = educationId;
    }

    public int getEmploymentStatusId() {
        return employmentStatusId;
    }

    public void setEmploymentStatusId(int employmentStatusId) {
        this.employmentStatusId = employmentStatusId;
    }

    public String getEmrId() {
        return emrId;
    }

    public void setEmrId(String emrId) {
        this.emrId = emrId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public boolean isDateOfBirthEstimated() {
        return isDateOfBirthEstimated;
    }

    public void setDateOfBirthEstimated(boolean dateOfBirthEstimated) {
        isDateOfBirthEstimated = dateOfBirthEstimated;
    }

    public String getNinNumber() {
        return ninNumber;
    }

    public void setNinNumber(String ninNumber) {
        this.ninNumber = ninNumber;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

}