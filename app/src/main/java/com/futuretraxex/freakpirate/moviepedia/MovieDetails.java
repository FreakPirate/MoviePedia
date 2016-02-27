package com.futuretraxex.freakpirate.moviepedia;

/**
 * Created by FreakPirate on 2/27/2016.
 */
public class MovieDetails {
    private String movieTitle;
    private String movieID;
    private String posterPath;

//    public MovieDetails(String posterPath){
//        this.posterPath = posterPath;
//    }

    public MovieDetails(String movieTitle, String posterPath, String movieID){
        this.movieTitle = movieTitle;
        this.movieID = movieID;
        this.posterPath = posterPath;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public String getMovieID(){
        return movieID;
    }

    public String getPosterPath(){
        return posterPath;
    }
}
