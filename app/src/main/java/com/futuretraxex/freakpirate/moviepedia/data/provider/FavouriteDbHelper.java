package com.futuretraxex.freakpirate.moviepedia.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FreakPirate on 5/21/2016.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavouriteDbHelper.class.getSimpleName();

    // name & version
    private static final String DATABASE_NAME = "moviepedia.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
               FavouriteContract.FavouriteEntry.TABLE_FAVOURITE + "(" +
               FavouriteContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
               FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
               FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
               FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
               FavouriteContract.FavouriteEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
               FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH + " TEXT, " +
               FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE + " TEXT, " +
               FavouriteContract.FavouriteEntry.COLUMN_POPULARITY + " REAL, " +
               FavouriteContract.FavouriteEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
               FavouriteContract.FavouriteEntry.COLUMN_ADULT + " INTEGER, " +
               FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE + " INTEGER NOT NULL, " +
               "CONSTRAINT unique_id UNIQUE (" + FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
               " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.FavouriteEntry.TABLE_FAVOURITE);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavouriteContract.FavouriteEntry.TABLE_FAVOURITE + "'");

        // re-create database
        onCreate(db);
    }
}
