package com.futuretraxex.freakpirate.moviepedia;

import android.net.Uri;
import android.util.Log;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class URIBuilder {

    private final String LOG_TAG = URIBuilder.class.getSimpleName();

    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    private final String PARAM_SORT_ORDER = "sort_by";
    private final String PARAM_API_KEY = "api_key";
    private String SORT_ORDER;
    private final String API_KEY;

    public URIBuilder(){
        SORT_ORDER = "popularity.desc";
        API_KEY = BuildConfig.API_KEY;
    }

    public URIBuilder(String sort_order){
        this.SORT_ORDER = sort_order;
        this.API_KEY = BuildConfig.API_KEY;
    }

    public Uri build(){
        Uri tmdbURI = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_SORT_ORDER, SORT_ORDER)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        return tmdbURI;
    }
}
