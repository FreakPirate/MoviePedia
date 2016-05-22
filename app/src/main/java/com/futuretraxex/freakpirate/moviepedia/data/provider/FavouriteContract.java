package com.futuretraxex.freakpirate.moviepedia.data.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by FreakPirate on 5/21/2016.
 */
public class FavouriteContract {
//    Content authority
    public static final String CONTENT_AUTHORITY = "com.futuretraxex.freakpirate.moviepedia.provider";

//    Base URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavouriteEntry implements BaseColumns{
        // Table name
        public static final String TABLE_FAVOURITE = "favourite";

        // Columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";

        // Content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVOURITE)
                .build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITE;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITE;

        // for building Uri's on query
        public static Uri buildByMovieIdUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
