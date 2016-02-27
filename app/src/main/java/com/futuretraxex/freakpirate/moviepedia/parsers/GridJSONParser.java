package com.futuretraxex.freakpirate.moviepedia.parsers;

import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.MovieDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class GridJSONParser {

    private final String LOG_TAG = GridJSONParser.class.getSimpleName();

    private final String PARAM_RESULTS = "results";
    private final String PARAM_POSTER_PATH = "poster_path";
    private final String PARAM_TITLE = "title";
    private final String PARAM_ID = "id";

    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private final String size = "w342";

    private String jsonStr;

    public GridJSONParser(String jsonStr){
        this.jsonStr = jsonStr;
    }

    public MovieDetails[] parse() throws JSONException{

        JSONObject gridJsonObj = new JSONObject(jsonStr);
        JSONArray gridArray = gridJsonObj.getJSONArray(PARAM_RESULTS);

        MovieDetails[] detailsList = new MovieDetails[20];

        for (int i=0; i<gridArray.length(); i++){

            JSONObject movieDetails = gridArray.getJSONObject(i);

            String posterPath = movieDetails.getString(PARAM_POSTER_PATH);
            String movieTitle = movieDetails.getString(PARAM_TITLE);
            String movieID = movieDetails.getString(PARAM_ID);

            posterPath = POSTER_BASE_URL + size + '/' + posterPath;

            MovieDetails temp = new MovieDetails(movieTitle, posterPath, movieID);

            detailsList[i] = temp;
        }

        for (int i=0; i<20; i++){
            Log.v(LOG_TAG, "Movie: " + detailsList[i].getMovieTitle());
        }

        return detailsList;
    }
}
