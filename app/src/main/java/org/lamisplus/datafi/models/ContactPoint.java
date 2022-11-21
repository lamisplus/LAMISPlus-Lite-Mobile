package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.lamisplus.datafi.classes.ContactPointClass;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class ContactPoint implements Serializable{

    String type;
    String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}