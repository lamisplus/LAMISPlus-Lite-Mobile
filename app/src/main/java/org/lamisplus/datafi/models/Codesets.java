package org.lamisplus.datafi.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Table(name = "codesets")
public class Codesets extends Model implements Serializable {

    @SerializedName("codeset_id")
    @Expose
    @Column(name = "codeset_id")
    private int codeset_id;

    @SerializedName("codeset_group")
    @Expose
    @Column(name = "codeset_group")
    private String codeset_group;

    @SerializedName("display")
    @Expose
    @Column(name = "display")
    private String display;

    @SerializedName("language")
    @Expose
    @Column(name = "language")
    private String language;

    @SerializedName("version")
    @Expose
    @Column(name = "version")
    private String version;

    @SerializedName("code")
    @Expose
    @Column(name = "code")
    private String code;

    @SerializedName("date_created")
    @Expose
    @Column(name = "date_created")
    private String date_created;

    @SerializedName("created_by")
    @Expose
    @Column(name = "created_by")
    private String created_by;

    @SerializedName("date_modified")
    @Expose
    @Column(name = "date_modified")
    private String date_modified;

    @SerializedName("modified_by")
    @Expose
    @Column(name = "modified_by")
    private String modified_by;

    @SerializedName("archived")
    @Expose
    @Column(name = "archived")
    private String archived;

    public int getCodeset_id() {
        return codeset_id;
    }

    public void setCodeset_id(int codeset_id) {
        this.codeset_id = codeset_id;
    }

    public String getCodeset_group() {
        return codeset_group;
    }

    public void setCodeset_group(String codeset_group) {
        this.codeset_group = codeset_group;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getArchived() {
        return archived;
    }

    public void setArchived(String archived) {
        this.archived = archived;
    }
}
