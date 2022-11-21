package org.lamisplus.datafi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Results<T> implements Serializable {

    @SerializedName("results")
    @Expose
    private List<T> results = new ArrayList<>();

    @SerializedName("links")
    @Expose
    private List<T> links = new ArrayList<>();

    public List<T> getLinks() {
        return links;
    }

    public void setLinks(List<T> links) {
        this.links = links;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
