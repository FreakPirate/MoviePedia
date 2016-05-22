package com.futuretraxex.freakpirate.moviepedia.data.universal;

import com.futuretraxex.freakpirate.moviepedia.backend.FetchMovieDB;
import com.futuretraxex.freakpirate.moviepedia.backend.URIBuilder;
import com.futuretraxex.freakpirate.moviepedia.data.parsers.BrowseMoviesParser;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.BrowseMoviesActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.MovieDetailActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.SettingsActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.BrowseMoviesAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.ToolbarSpinnerAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.BrowseMoviesFragment;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.MovieDetailFragment;
import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;

/**
 * Created by FreakPirate on 3/1/2016.
 */
public class GlobalData {
    public final static String DETAIL_ACTIVITY_INTENT_STRING = "Movie Detail";

    public final static String LOG_TAG_FETCH_BROWSE_MOVIE_DB = FetchMovieDB.class.getSimpleName();
    public final static String LOG_TAG_URI_BUILDER = URIBuilder.class.getSimpleName();
    public final static String LOG_TAG_BROWSE_MOVIE_PARSER = BrowseMoviesParser.class.getSimpleName();
    public final static String LOG_TAG_BROWSE_MOVIES_ACTIVITY = BrowseMoviesActivity.class.getSimpleName();
    public final static String LOG_TAG_MOVIE_DETAIL_ACTIVITY = MovieDetailActivity.class.getSimpleName();
    public final static String LOG_TAG_SETTINGS_ACTIVITY = SettingsActivity.class.getSimpleName();
    public final static String LOG_TAG_BROWSE_MOVIES_ADAPTER = BrowseMoviesAdapter.class.getSimpleName();
    public final static String LOG_TAG_TOOLBAR_SPINNER_ADAPTER = ToolbarSpinnerAdapter.class.getSimpleName();
    public final static String LOG_TAG_DETAIL_ACTIVITY_FRAGMENT = MovieDetailFragment.class.getSimpleName();
    public final static String LOG_TAG_MAIN_ACTIVITY_FRAGMENT = BrowseMoviesFragment.class.getSimpleName();
    public final static String LOG_TAG_MOVIE_DATA = MovieDataModel.class.getSimpleName();

    public static Boolean preferenceChanged = false;

    public static final String size_w92 = "w92";
    public static final String size_w154 = "w154";
    public static final String size_w185 = "w185";
    public static final String size_w342 = "w342";
    public static final String size_w500 = "w500";
    public static final String size_w780 = "w780";
}
