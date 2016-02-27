package com.futuretraxex.freakpirate.moviepedia;

import android.net.Uri;
import android.util.Log;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class URIBuilder {

    private final String LOG_TAG = URIBuilder.class.getSimpleName();

    private final static String PARAM_SORT_ORDER = "sort_by";
    private final static String PARAM_API_KEY = "api_key";
    private final static String API_KEY = BuildConfig.API_KEY;

    public static Uri buildGridUri(String baseURL, String sortOrder){
        Uri gridURI = Uri.parse(baseURL)
                .buildUpon()
                .appendQueryParameter(PARAM_SORT_ORDER, sortOrder)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        return gridURI;
    }

    public static Uri buildDetailUri(){
        return null;
    }
}
