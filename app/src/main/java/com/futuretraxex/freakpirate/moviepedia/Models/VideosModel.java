
package com.futuretraxex.freakpirate.moviepedia.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideosResult> results = new ArrayList<VideosResult>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public VideosModel() {
    }

    /**
     * 
     * @param id
     * @param results
     */
    public VideosModel(Integer id, List<VideosResult> results) {
        this.id = id;
        this.results = results;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<VideosResult> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<VideosResult> results) {
        this.results = results;
    }

}
