package com.futuretraxex.freakpirate.moviepedia.data.universal;

import com.futuretraxex.freakpirate.moviepedia.ui.fragment.BrowseMoviesFragment;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.DetailActivityFragment;

/**
 * Created by FreakPirate on 3/1/2016.
 */
public class GlobalData {
    public final static String DETAIL_ACTIVITY_INTENT_STRING = "Movie Detail";

    public final static String LOG_TAG_DETAIL_ACTIVITY_FRAGMENT = DetailActivityFragment.class.getSimpleName();
    public final static String LOG_TAG_MAIN_ACTIVITY_FRAGMENT = BrowseMoviesFragment.class.getSimpleName();

    public static Boolean preferenceChanged = false;
}
