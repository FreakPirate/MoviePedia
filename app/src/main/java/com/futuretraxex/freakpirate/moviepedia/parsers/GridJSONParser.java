package com.futuretraxex.freakpirate.moviepedia.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class GridJSONParser {

    private final static String LOG_TAG = GridJSONParser.class.getSimpleName();

    private final static String PARAM_RESULTS = "results";
    private final static String PARAM_POSTER_PATH = "poster_path";
    private final static String PARAM_TITLE = "title";
    private final static String PARAM_ID = "id";

    public static String[] parse(String tmdbJsonStr) throws JSONException{

        JSONObject tmdbJsonObj = new JSONObject(tmdbJsonStr);
        JSONArray resultsArray = tmdbJsonObj.getJSONArray(PARAM_RESULTS);

        String[] resultStrs = new String[20];

        for (int i=0; i<resultsArray.length(); i++){
            String poster_path;
            String title;
            String id;

            JSONObject movieDetails = resultsArray.getJSONObject(i);

            poster_path = movieDetails.getString(PARAM_POSTER_PATH);
            title = movieDetails.getString(PARAM_TITLE);
            id = movieDetails.getString(PARAM_ID);

            // Asterisks (*) here acts as a splitter for title, path and Id's
            resultStrs [i] = title + "~" + poster_path + "~" + id;
        }

        for (String s : resultStrs){
            Log.v(LOG_TAG, "Movie: " + s);
        }

        return resultStrs;
    }
}
