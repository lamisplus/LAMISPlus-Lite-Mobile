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

    @Column(name = "fromServer")
    private Integer fromServer = 0; //Default value is 0. If its pulled from server then value becomes 1. If Edited and requires changes then value becomes 2

    @Column(name = "personId")
    @SerializedName("personId")
    private Integer personId = null;

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
    private transient  List<ContactPoint> contactPointList = new ArrayList<>();

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
    private Integer genderId;

    @SerializedName("sexId")
    @Column(name = "sexId")
    @Expose
    private Integer sexId;

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
    private Integer educationId;

    @SerializedName("maritalStatusId")
    @Column(name = "maritalStatusId")
    @Expose
    private Integer maritalStatusId;

    @SerializedName("dateOfRegistration")
    @Column(name = "dateOfRegistration")
    @Expose
    private String dateOfRegistration;

    @SerializedName("emrId")
    @Column(name = "emrId")
    @Expose
    private String emrId;

    @SerializedName("facilityId")
    @Column(name = "facilityId")
    @Expose
    private Integer facilityId;

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
    private Integer organizationId;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public Integer getFromServer() {
        return fromServer;
    }

    public void setFromServer(Integer fromServer) {
        this.fromServer = fromServer;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
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

    public void setAddress(List<Address> addressList) {
        this.addressList = addressList;
    }

    public Address getAddresses() {
        if(address != null) {
            if (!address.isEmpty()) {
                return pullAddressList().get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    public Contact getContacts() {
        if(contact != null) {
            if (!contact.isEmpty()) {
                return pullContactList().get(0);
            }
        }
        return null;
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
        Type type = new TypeToken<List<Contact>>() {
        }.getType();
        List<Contact> contactList = gson.fromJson(contact, type);
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

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getSexId() {
        return sexId;
    }

    public void setSexId(Integer sexId) {
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
        if(otherName != null) {
            return otherName;
        }
        return "";
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public Integer getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStatusId(Integer maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public Integer getEducationId() {
        return educationId;
    }

    public void setEducationId(Integer educationId) {
        this.educationId = educationId;
    }

    public String getEmrId() {
        return emrId;
    }

    public void setEmrId(String emrId) {
        this.emrId = emrId;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

}