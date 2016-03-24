package com.futuretraxex.freakpirate.moviepedia.data.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerResult {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public TrailerResult withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The size
     */
    public String getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(String size) {
        this.size = size;
    }

    public TrailerResult withSize(String size) {
        this.size = size;
        return this;
    }

    /**
     * 
     * @return
     *     The source
     */
    public String getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    public TrailerResult withSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public TrailerResult withType(String type) {
        this.type = type;
        return this;
    }

}
