package com.futuretraxex.freakpirate.moviepedia.data.parsers;

import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class BrowseMoviesParser {

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
    private final String cover_size = "w500";

    private String jsonStr;

    private int index = 20;

    public BrowseMoviesParser(String jsonStr){
        this.jsonStr = jsonStr;
    }

    public MovieData[] parse() throws JSONException{

        JSONObject gridJsonObj = new JSONObject(jsonStr);
        JSONArray gridArray = gridJsonObj.getJSONArray(PARAM_RESULTS);

        MovieData[] detailsList = new MovieData[index];

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

            MovieData temp = new MovieData(movieTitle, movieID, posterPath, backdropPath,
                    plotSynopsis, averageRating, releaseDate);

            detailsList[i] = temp;
        }

//        for (int i=0; i<index; i++){
//            Log.v(GlobalData.LOG_TAG_BROWSE_MOVIE_PARSER, "Movie: " + detailsList[i].getMOVIE_TITLE());
//        }

        return detailsList;
    }
}
