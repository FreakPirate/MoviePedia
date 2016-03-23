package com.futuretraxex.freakpirate.moviepedia.data.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> results = new ArrayList<TrailerResult>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public TrailerModel() {
    }

    /**
     * 
     * @param id
     * @param results
     */
    public TrailerModel(int id, List<TrailerResult> results) {
        this.id = id;
        this.results = results;
    }

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<TrailerResult> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<TrailerResult> results) {
        this.results = results;
    }

}
