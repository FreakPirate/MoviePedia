package com.futuretraxex.freakpirate.moviepedia.ui.helper;

import android.net.Uri;

import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;

/**
 * Created by FreakPirate on 6/17/2016.
 */
public interface Callback {
//    This function helps handle callbacks from BrowseMoviesAdapter
    public void onItemSelected(MovieDataModel model);

//    for FavouriteAdapter
    public void onItemSelected(Uri uri);
}
