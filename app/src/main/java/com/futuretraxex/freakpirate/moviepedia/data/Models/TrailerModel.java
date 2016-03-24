
package com.futuretraxex.freakpirate.moviepedia.data.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("quicktime")
    @Expose
    private List<Object> quicktime = new ArrayList<Object>();
    @SerializedName("youtube")
    @Expose
    private List<TrailerResult> youtube = new ArrayList<TrailerResult>();

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

    public TrailerModel withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The quicktime
     */
    public List<Object> getQuicktime() {
        return quicktime;
    }

    /**
     * 
     * @param quicktime
     *     The quicktime
     */
    public void setQuicktime(List<Object> quicktime) {
        this.quicktime = quicktime;
    }

    public TrailerModel withQuicktime(List<Object> quicktime) {
        this.quicktime = quicktime;
        return this;
    }

    /**
     * 
     * @return
     *     The youtube
     */
    public List<TrailerResult> getYoutube() {
        return youtube;
    }

    /**
     * 
     * @param youtube
     *     The youtube
     */
    public void setYoutube(List<TrailerResult> youtube) {
        this.youtube = youtube;
    }

    public TrailerModel withYoutube(List<TrailerResult> youtube) {
        this.youtube = youtube;
        return this;
    }

}
