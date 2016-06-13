package com.futuretraxex.freakpirate.moviepedia.backend;

import android.net.Uri;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.BuildConfig;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;

import java.net.URL;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class URIBuilder {

    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_INCLUDE_ADULT = "include_adult";
    private final static String PARAM_PAGE = "page";

    private final static String API_KEY = BuildConfig.API_KEY;

    private final static String PARAM_TRAILER_ID = "v";

    public static Uri buildGridUri(String baseURL, String sortOrder, Boolean includeAdult, int pageNum){
        Uri gridURI = Uri.parse(baseURL)
                .buildUpon()
                .appendEncodedPath(sortOrder)
                .appendQueryParameter(PARAM_INCLUDE_ADULT, includeAdult.toString())
                .appendQueryParameter(PARAM_PAGE, Integer.toString(pageNum))
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        Log.d(GlobalData.LOG_TAG_URI_BUILDER, gridURI.toString());

        return gridURI;
    }

    public static Uri buildTrailerUri(String baseUrl, String trailerId){
        Uri trailerUri = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter(PARAM_TRAILER_ID, trailerId)
                .build();

        Log.v(GlobalData.LOG_TAG_URI_BUILDER, trailerUri.toString());

        return trailerUri;
    }
}
