package com.futuretraxex.freakpirate.moviepedia;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FreakPirate on 2/27/2016.
 */

//Implements Parcelable to pass object amongst activities
public class MovieDetails implements Parcelable {
    private String MOVIE_TITLE;
    private String MOVIE_ID;
    private String POSTER_PATH;
    private String BACKDROP_PATH;
    private String PLOT_SYNOPSIS;
    private String AVERAGE_RATINGS;
    private String RELEASE_DATE;


//    public MovieDetails(String POSTER_PATH){
//        this.POSTER_PATH = POSTER_PATH;
//    }

    //This will be called once the Object is instantiated on the sender's end.
    public MovieDetails(String movieTitle, String movieID, String posterPath, String backdropPath,
                        String plotSynopsis, String averageRatings, String releaseDate){
        this.MOVIE_TITLE = movieTitle;
        this.MOVIE_ID = movieID;
        this.POSTER_PATH = posterPath;
        this.BACKDROP_PATH = backdropPath;
        this.PLOT_SYNOPSIS = plotSynopsis;
        this.AVERAGE_RATINGS = averageRatings;
        this.RELEASE_DATE = releaseDate;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.MOVIE_TITLE,
                this.MOVIE_ID,
                this.POSTER_PATH,
                this.BACKDROP_PATH,
                this.PLOT_SYNOPSIS,
                this.AVERAGE_RATINGS,
                this.RELEASE_DATE
        });
    }

    //This will inflate the MovieDetails object
    //Once it has reached its destination activity
    public MovieDetails(Parcel in){
        String[] receivedData = new String[7];
        in.readStringArray(receivedData);

        this.MOVIE_TITLE = receivedData[0];
        this.MOVIE_ID = receivedData[1];
        this.POSTER_PATH = receivedData[2];
        this.BACKDROP_PATH = receivedData[3];
        this.PLOT_SYNOPSIS = receivedData[4];
        this.AVERAGE_RATINGS = receivedData[5];
        this.RELEASE_DATE = receivedData[6];
    }

    public String getMOVIE_TITLE(){
        return MOVIE_TITLE;
    }

    public String getMOVIE_ID(){
        return MOVIE_ID;
    }

    public String getPOSTER_PATH(){
        return POSTER_PATH;
    }

    public String getBACKDROP_PATH(){
        return BACKDROP_PATH;
    }

    public String getPLOT_SYNOPSIS(){
        return PLOT_SYNOPSIS;
    }

    public String getAVERAGE_RATINGS(){
        return AVERAGE_RATINGS;
    }

    public String getRELEASE_DATE(){
        return RELEASE_DATE;
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
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
}
