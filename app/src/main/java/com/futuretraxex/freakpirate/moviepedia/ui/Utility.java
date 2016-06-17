package com.futuretraxex.freakpirate.moviepedia.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.provider.FavouriteContract;

/**
 * Created by FreakPirate on 6/15/2016.
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static String getPreferredSortOrder(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(
                context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default_value)
        );
    }

    public static MovieDataModel fetchDataFromUri(Context context, Uri uri){
        MovieDataModel fetchModel;

        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() != 0){
            Log.v(LOG_TAG, "Cursor is not empty! CursorCount: " + cursor.getCount());
            cursor.moveToFirst();

            int indexMovieId = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID);
            long movieId = cursor.getInt(indexMovieId);

            int indexMovieTitle = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE);
            String movieTitle = cursor.getString(indexMovieTitle);

            int indexOverview = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW);
            String overview = cursor.getString(indexOverview);

            int indexBackdropPath = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_BACKDROP_PATH);
            String backdropPath = cursor.getString(indexBackdropPath);

            int indexPosterPath = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH);
            String posterPath = cursor.getString(indexPosterPath);

            int indexReleaseDate = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE);
            String releaseDate = cursor.getString(indexReleaseDate);

            int indexPopularity = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POPULARITY);
            float popularity = cursor.getFloat(indexPopularity);

            int indexVoteAverage = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_VOTE_AVERAGE);
            float voteAverage = cursor.getFloat(indexVoteAverage);

            int indexAdult = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ADULT);
            int adultTemp = cursor.getInt(indexAdult);
            boolean adult = adultTemp == 1;

            int indexIsFavourite = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE);
            int isFavouriteTemp = cursor.getInt(indexIsFavourite);
            boolean isFavourite = isFavouriteTemp == 1;

            int toolbarColor = context.getResources().getColor(R.color.colorPrimary);
            int statusBarColor = context.getResources().getColor(R.color.colorPrimaryDark);

            fetchModel = new MovieDataModel(movieTitle, movieId, posterPath, backdropPath, overview,
                    voteAverage, popularity, releaseDate, adult, isFavourite, toolbarColor, statusBarColor, context);
        }
        else {
            Log.e(LOG_TAG, "Intent Cursor is either empty or cannot be opened!");
            return null;
        }

        return fetchModel;
    }
}
