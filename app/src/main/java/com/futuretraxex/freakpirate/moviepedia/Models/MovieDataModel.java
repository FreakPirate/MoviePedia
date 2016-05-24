package com.futuretraxex.freakpirate.moviepedia.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FreakPirate on 2/27/2016.
 */

//Implements Parcelable to pass object amongst activities
public class MovieDataModel implements Parcelable {
    private String MOVIE_TITLE;
    private String MOVIE_ID;
    private String POSTER_PATH;
    private String BACKDROP_PATH;
    private String PLOT_SYNOPSIS;
    private float AVERAGE_RATINGS;
    private float POPULARITY;
    private String RELEASE_DATE;
    private String ADULT;
    private int TOOLBAR_COLOR;
    private int STATUS_BAR_COLOR;

    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

//    public MovieDataModel(String POSTER_PATH){
//        this.POSTER_PATH = POSTER_PATH;
//    }

    //This will be called once the Object is instantiated on the sender's end.
    public MovieDataModel(String movieTitle, String movieID, String posterPath, String backdropPath,
                          String plotSynopsis, float averageRatings, float popularity, String releaseDate, String adult,
                          int toolbarColor, int statusBarColor){
        this.MOVIE_TITLE = movieTitle;
        this.MOVIE_ID = movieID;
        this.POSTER_PATH = posterPath;
        this.BACKDROP_PATH = backdropPath;
        this.PLOT_SYNOPSIS = plotSynopsis;
        this.AVERAGE_RATINGS = averageRatings;
        this.RELEASE_DATE = releaseDate;
        this.POPULARITY = popularity;

        if (adult.equalsIgnoreCase("true")){
            this.ADULT = "Yes";
        }else {
            this.ADULT = "No";
        }

        this.TOOLBAR_COLOR = toolbarColor;
        this.STATUS_BAR_COLOR = statusBarColor;
    }

    public void addColors(int toolbarColor, int statusBarColor){
        this.TOOLBAR_COLOR = toolbarColor;
        this.STATUS_BAR_COLOR = statusBarColor;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.MOVIE_TITLE,
                this.MOVIE_ID,
                this.POSTER_PATH,
                this.BACKDROP_PATH,
                this.PLOT_SYNOPSIS,
                String.valueOf(this.AVERAGE_RATINGS),
                String.valueOf(this.POPULARITY),
                this.RELEASE_DATE,
                this.ADULT,
                Integer.toString(this.TOOLBAR_COLOR),
                Integer.toString(this.STATUS_BAR_COLOR)
        });
    }


    //This will inflate the MovieDataModel object
    //Once it has reached its destination activity
    public MovieDataModel(Parcel in){
        int arraySize = 11;

        String[] receivedData = new String[arraySize];
        in.readStringArray(receivedData);

        this.MOVIE_TITLE = receivedData[0];
        this.MOVIE_ID = receivedData[1];
        this.POSTER_PATH = receivedData[2];
        this.BACKDROP_PATH = receivedData[3];
        this.PLOT_SYNOPSIS = receivedData[4];
        this.AVERAGE_RATINGS = Float.parseFloat(receivedData[5]);
        this.POPULARITY = Float.parseFloat(receivedData[6]);
        this.RELEASE_DATE = receivedData[7];
        this.ADULT = receivedData[8];
        this.TOOLBAR_COLOR = Integer.parseInt(receivedData[9]);
        this.STATUS_BAR_COLOR = Integer.parseInt(receivedData[10]);
    }

    public String getMOVIE_TITLE(){
        return MOVIE_TITLE;
    }

    public String getMOVIE_ID(){
        return MOVIE_ID;
    }

    public String getPOSTER_PATH(String size){
        return IMAGE_BASE_URL + size + '/' + POSTER_PATH;
    }

    public String getBACKDROP_PATH(String size){
        return IMAGE_BASE_URL + size + '/' + BACKDROP_PATH;
    }

    public String getPLOT_SYNOPSIS(){
        return PLOT_SYNOPSIS;
    }

    public float getAVERAGE_RATINGS(){
        return AVERAGE_RATINGS;
    }

    public float getPOPULARITY(){
        return POPULARITY;
    }

    public String getRELEASE_DATE(){
        return RELEASE_DATE;
    }

    public String getADULT(){
        return ADULT;
    }

    public int getTOOLBAR_COLOR(){
        return TOOLBAR_COLOR;
    }

    public int getSTATUS_BAR_COLOR(){
        return STATUS_BAR_COLOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //BoilerPlate
    //Failure to add this results in the following exception
    //"android.os.BadParcelableException: Parcelable protocol
    //requires a Parcelable.Creator object called  CREATOR on class"
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieDataModel createFromParcel(Parcel in) {
            return new MovieDataModel(in);
        }

        public MovieDataModel[] newArray(int size) {
            return new MovieDataModel[size];
        }
    };
}
