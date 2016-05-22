package com.futuretraxex.freakpirate.moviepedia.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by FreakPirate on 5/22/2016.
 */
public class FavouriteProvider extends ContentProvider {
    private static final String LOG_TAG = FavouriteProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavouriteDbHelper mOpenHelper;

    private static final int FAVOURITE = 100;
    private static final int FAVOURITE_WITH_MOVIE_ID = 200;

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouriteContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, FavouriteContract.FavouriteEntry.TABLE_FAVOURITE, FAVOURITE);
        matcher.addURI(authority, FavouriteContract.FavouriteEntry.TABLE_FAVOURITE + "/#", FAVOURITE_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FavouriteDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            // All favourites selected
            case FAVOURITE:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            // Individual Favourite based on ID selected
            case FAVOURITE_WITH_MOVIE_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                        projection,
                        FavouriteContract.FavouriteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FAVOURITE:
                return FavouriteContract.FavouriteEntry.CONTENT_DIR_TYPE;

            case FAVOURITE_WITH_MOVIE_ID:
                return FavouriteContract.FavouriteEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case FAVOURITE:{
                long _id = db.insert(FavouriteContract.FavouriteEntry.TABLE_FAVOURITE, null, values);

                // insert unless it is already contained in the database
                if (_id > 0)
                    returnUri = FavouriteContract.FavouriteEntry.buildByMovieIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into: " + uri);

                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case FAVOURITE:{
                rowsDeleted = db.delete(
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                        selection,
                        selectionArgs
                );

                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE + "'");
                break;
            }

            case FAVOURITE_WITH_MOVIE_ID:{
                rowsDeleted = db.delete(
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                        FavouriteContract.FavouriteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}
                );

                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE + "'");
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if (values == null){
            throw new IllegalArgumentException("Cannot have null Content Values");
        }

        switch (match){
            case FAVOURITE:
                rowsUpdated = db.update(
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case FAVOURITE_WITH_MOVIE_ID:
                rowsUpdated = db.update(
                        FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                        values,
                        FavouriteContract.FavouriteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case FAVOURITE:{
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int rowsInserted = 0;

                try {

                    for (ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null Content Values");
                        }

                        long _id = -1;

                        try{
                            _id = db.insertOrThrow(
                                    FavouriteContract.FavouriteEntry.TABLE_FAVOURITE,
                                    null,
                                    value
                            );
                        }catch (SQLiteConstraintException e){
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1){
                            rowsInserted++;
                        }
                    }

                    if (rowsInserted > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                }finally {
                    // all transactions occur at once
                    db.endTransaction();
                }

                if (rowsInserted > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
