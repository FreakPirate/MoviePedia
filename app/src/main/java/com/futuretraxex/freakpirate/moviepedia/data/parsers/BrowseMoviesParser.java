package com.futuretraxex.freakpirate.moviepedia.data.parsers;

import com.futuretraxex.freakpirate.moviepedia.data.MovieDataModel;

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
    private final String PARAM_ADULT = "adult";

    private String jsonStr;

    private int index = 20;

    public BrowseMoviesParser(String jsonStr){
        this.jsonStr = jsonStr;
    }

    public MovieDataModel[] parse() throws JSONException{

        JSONObject gridJsonObj = new JSONObject(jsonStr);
        JSONArray gridArray = gridJsonObj.getJSONArray(PARAM_RESULTS);

        MovieDataModel[] detailsList = new MovieDataModel[index];

        for (int i=0; i<gridArray.length(); i++){

            JSONObject detailObject = gridArray.getJSONObject(i);

            String movieTitle = detailObject.getString(PARAM_TITLE);
            String movieID = detailObject.getString(PARAM_ID);
            String posterPath = detailObject.getString(PARAM_POSTER_PATH);
            String backdropPath = detailObject.getString(PARAM_BACKDROP_PATH);
            String plotSynopsis = detailObject.getString(PARAM_PLOT_SYNOPSIS);
            String averageRating = detailObject.getString(PARAM_AVERAGE_RATING);
            String releaseDate = detailObject.getString(PARAM_RELEASE_DATE);
            String adult = detailObject.getString(PARAM_ADULT);

            MovieDataModel temp = new MovieDataModel(movieTitle, movieID, posterPath, backdropPath,
                    plotSynopsis, averageRating, releaseDate, adult);

            detailsList[i] = temp;
        }

//        for (int i=0; i<index; i++){
//            Log.v(GlobalData.LOG_TAG_BROWSE_MOVIE_PARSER, "Movie: " + detailsList[i].getMOVIE_TITLE());
//        }

        return detailsList;
    }
}
