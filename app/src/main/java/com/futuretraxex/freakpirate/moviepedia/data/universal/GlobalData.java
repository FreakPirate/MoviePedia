package com.futuretraxex.freakpirate.moviepedia.data.universal;

import com.futuretraxex.freakpirate.moviepedia.backend.FetchBrowseMovieDB;
import com.futuretraxex.freakpirate.moviepedia.backend.URIBuilder;
import com.futuretraxex.freakpirate.moviepedia.data.parsers.BrowseMoviesParser;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.BrowseMoviesActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.MovieDetailActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.SettingsActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.BrowseMoviesAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.ToolbarSpinnerAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.BrowseMoviesFragment;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.MovieDetailFragment;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieData;

/**
 * Created by FreakPirate on 3/1/2016.
 */
public class GlobalData {
    public final static String DETAIL_ACTIVITY_INTENT_STRING = "Movie Detail";

    public final static String LOG_TAG_FETCH_BROWSE_MOVIE_DB = FetchBrowseMovieDB.class.getSimpleName();
    public final static String LOG_TAG_URI_BUILDER = URIBuilder.class.getSimpleName();
    public final static String LOG_TAG_BROWSE_MOVIE_PARSER = BrowseMoviesParser.class.getSimpleName();
    public final static String LOG_TAG_BROWSE_MOVIES_ACTIVITY = BrowseMoviesActivity.class.getSimpleName();
    public final static String LOG_TAG_MOVIE_DETAIL_ACTIVITY = MovieDetailActivity.class.getSimpleName();
    public final static String LOG_TAG_SETTINGS_ACTIVITY = SettingsActivity.class.getSimpleName();
    public final static String LOG_TAG_BROWSE_MOVIES_ADAPTER = BrowseMoviesAdapter.class.getSimpleName();
    public final static String LOG_TAG_TOOLBAR_SPINNER_ADAPTER = ToolbarSpinnerAdapter.class.getSimpleName();
    public final static String LOG_TAG_DETAIL_ACTIVITY_FRAGMENT = MovieDetailFragment.class.getSimpleName();
    public final static String LOG_TAG_MAIN_ACTIVITY_FRAGMENT = BrowseMoviesFragment.class.getSimpleName();
    public final static String LOG_TAG_MOVIE_DATA = MovieData.class.getSimpleName();

    public static Boolean preferenceChanged = false;
}
