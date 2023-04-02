package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "lab")
public class Lab extends Model implements Serializable {

    @SerializedName("name")
    @Column(name = "name")
    @Expose
    private String name;

    @SerializedName("lab_code")
    @Column(name = "lab_code")
    @Expose
    private String lab_code;

    @SerializedName("selected")
    @Column(name = "selected")
    @Expose
    private String selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLab_code() {
        return lab_code;
    }

    public void setLab_code(String lab_code) {
        this.lab_code = lab_code;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
