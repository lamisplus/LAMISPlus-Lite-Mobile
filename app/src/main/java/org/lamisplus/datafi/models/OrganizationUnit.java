package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "organizationunit")
public class OrganizationUnit extends Model implements Serializable {

    @SerializedName("unit_id")
    @Column(name = "unit_id")
    @Expose
    private Integer unit_id;

    @SerializedName("name")
    @Column(name = "name")
    @Expose
    private String name;

    @SerializedName("description")
    @Column(name = "description")
    @Expose
    private String description;

    @SerializedName("organisation_unit_level_id")
    @Column(name = "organisation_unit_level_id")
    @Expose
    private String organisation_unit_level_id;

    @SerializedName("parent_organisation_unit_id")
    @Column(name = "parent_organisation_unit_id")
    @Expose
    private String parent_organisation_unit_id;

    @SerializedName("archived")
    @Column(name = "archived")
    @Expose
    private String archived;

    @SerializedName("uuid")
    @Column(name = "uuid")
    @Expose
    private String uuid;

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganisation_unit_level_id() {
        return organisation_unit_level_id;
    }

    public void setOrganisation_unit_level_id(String organisation_unit_level_id) {
        this.organisation_unit_level_id = organisation_unit_level_id;
    }

    public String getParent_organisation_unit_id() {
        return parent_organisation_unit_id;
    }

    public void setParent_organisation_unit_id(String parent_organisation_unit_id) {
        this.parent_organisation_unit_id = parent_organisation_unit_id;
    }

    public String getArchived() {
        return archived;
    }

    public void setArchived(String archived) {
        this.archived = archived;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
