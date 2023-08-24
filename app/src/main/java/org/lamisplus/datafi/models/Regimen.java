package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "regimen")
public class Regimen extends Model implements Serializable {
    @SerializedName("regimen_id")
    @Column(name = "regimen_id")
    @Expose
    private Integer regimen_id;

    @SerializedName("description")
    @Column(name = "description")
    @Expose
    private String description;

    @SerializedName("composition")
    @Column(name = "composition")
    @Expose
    private String composition;

    @SerializedName("regimen_type_id")
    @Column(name = "regimen_type_id")
    @Expose
    private Integer regimen_type_id;

    @SerializedName("active")
    @Column(name = "active")
    @Expose
    private String active;

    @SerializedName("priority")
    @Column(name = "priority")
    @Expose
    private Integer priority;

    public Integer getRegimen_id() {
        return regimen_id;
    }

    public void setRegimen_id(Integer regimen_id) {
        this.regimen_id = regimen_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public Integer getRegimen_type_id() {
        return regimen_type_id;
    }

    public void setRegimen_type_id(Integer regimen_type_id) {
        this.regimen_type_id = regimen_type_id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
