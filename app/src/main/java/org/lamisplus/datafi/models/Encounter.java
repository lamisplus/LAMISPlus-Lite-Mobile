package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Table(name = "encounter")
public class Encounter extends Model implements Serializable {
    private Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Type type = new TypeToken<List<Address>>(){}.getType();

    @Column(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @Column(name = "person")
    @SerializedName("person")
    private String person;

    @Column(name = "packageName")
    @SerializedName("packageName")
    private String packageName;

    @Column(name = "personId")
    @SerializedName("personId")
    @Expose
    private Integer personId;

    @Column(name = "dataValues")
    @SerializedName("dataValues")
    @Expose
    private String dataValues;

    @Column(name = "synced")
    @SerializedName("synced")
    @Expose
    private boolean synced;

    @Column(name = "dateTime")
    @SerializedName("dateTime")
    private String dateTime;

    @Column(name = "lastEditDateTime")
    @SerializedName("lastEditDateTime")
    private String lastEditDateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getDataValues() {
        return dataValues;
    }

    public void setDataValues(String values) {
        this.dataValues = values;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLastEditDateTime() {
        return lastEditDateTime;
    }

    public void setLastEditDateTime(String lastEditDateTime) {
        this.lastEditDateTime = lastEditDateTime;
    }
}
