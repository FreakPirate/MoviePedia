package com.futuretraxex.freakpirate.moviepedia.backend;

import android.net.Uri;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.BuildConfig;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class URIBuilder {

    private final static String LOG_TAG = URIBuilder.class.getSimpleName();

    private final static String PARAM_SORT_ORDER = "sort_by";
    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_INCLUDE_ADULT = "include_adult";

    private final static String API_KEY = BuildConfig.API_KEY;

    public static Uri buildGridUri(String baseURL, String sortOrder, Boolean includeAdult){
        Uri gridURI = Uri.parse(baseURL)
                .buildUpon()
                .appendQueryParameter(PARAM_SORT_ORDER, sortOrder)
                .appendQueryParameter(PARAM_INCLUDE_ADULT, includeAdult.toString())
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        Log.d(LOG_TAG, gridURI.toString());

        return gridURI;
    }

    public static Uri buildDetailUri(){
        return null;
    }
}
