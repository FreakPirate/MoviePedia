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
    private final String PARAM_RELEASE_DATE = "release_date";
    private final String PARAM_PLOT_SYNOPSIS = "overview";
    private final String PARAM_BACKDROP_PATH = "backdrop_path";
    private final String PARAM_AVERAGE_RATING = "vote_average";

    private final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private final String poster_size = "w342";
    private final String cover_size = "w780";

    private String jsonStr;

    private int index = 20;

    public GridJSONParser(String jsonStr){
        this.jsonStr = jsonStr;
    }

    public MovieDetails[] parse() throws JSONException{

        JSONObject gridJsonObj = new JSONObject(jsonStr);
        JSONArray gridArray = gridJsonObj.getJSONArray(PARAM_RESULTS);

        MovieDetails[] detailsList = new MovieDetails[index];

        for (int i=0; i<gridArray.length(); i++){

            JSONObject detailObject = gridArray.getJSONObject(i);

            String movieTitle = detailObject.getString(PARAM_TITLE);
            String movieID = detailObject.getString(PARAM_ID);
            String posterPath = detailObject.getString(PARAM_POSTER_PATH);
            String backdropPath = detailObject.getString(PARAM_BACKDROP_PATH);
            String plotSynopsis = detailObject.getString(PARAM_PLOT_SYNOPSIS);
            String averageRating = detailObject.getString(PARAM_AVERAGE_RATING);
            String releaseDate = detailObject.getString(PARAM_RELEASE_DATE);

            posterPath = IMAGE_BASE_URL + poster_size + '/' + posterPath;
            backdropPath = IMAGE_BASE_URL + cover_size + '/' + backdropPath;

            MovieDetails temp = new MovieDetails(movieTitle, movieID, posterPath, backdropPath,
                    plotSynopsis, averageRating, releaseDate);

            detailsList[i] = temp;
        }

        for (int i=0; i<index; i++){
            Log.v(LOG_TAG, "Movie: " + detailsList[i].getMOVIE_TITLE());
        }

        return detailsList;
    }
}
