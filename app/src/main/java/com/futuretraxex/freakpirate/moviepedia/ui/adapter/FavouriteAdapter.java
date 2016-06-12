package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.provider.FavouriteContract;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.MovieDetailActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by FreakPirate on 5/22/2016.
 */

public class FavouriteAdapter extends CursorRecyclerViewAdapter<FavouriteAdapter.ViewHolder> {
    private final String LOG_TAG = FavouriteAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderId;

    public FavouriteAdapter(Context context, Cursor cursor, int flags, int loaderId){
        super(context, cursor);

        mContext = context;
        sLoaderId = loaderId;
    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        Log.d(LOG_TAG, "In new View");
//
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_browse_movies, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        view.setTag(viewHolder);
//
//        return view;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ViewHolder viewHolder = (ViewHolder) view.getTag();
//
//        Log.d(LOG_TAG, "In Bind View");
//
//        int posterIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH);
//        String posterPath = cursor.getString(posterIndex);
//        Log.i(LOG_TAG, "Extracted Poster path: " + posterPath);
//
//        Picasso.with(mContext)
//                .load(posterPath)
//                .placeholder(R.color.colorPrimaryDark)
//                .into(viewHolder.moviePosterView);
//
//        int titleIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE);
//        String movieTitle = cursor.getString(titleIndex);
//        viewHolder.movieTitle.setText(movieTitle);
//
//        int isFavIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE);
//        int isFavourite = cursor.getInt(isFavIndex);
//
//        if (isFavourite == 1){
//            Picasso.with(mContext)
//                    .load(R.drawable.ic_favorite_white_24dp)
//                    .into(viewHolder.favIconImageView);
//        }else {
//            Picasso.with(mContext)
//                    .load(R.drawable.ic_favorite_border_white_24dp)
//                    .into(viewHolder.favIconImageView);
//        }
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "In onCreateViewHolder");

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_browse_movies, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {

        Log.d(LOG_TAG, "In Bind View");

        int movieIdIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID);
        viewHolder.movieId = cursor.getLong(movieIdIndex);

        int posterIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH);
        String posterPath = cursor.getString(posterIndex);
        Log.i(LOG_TAG, "Extracted Poster path: " + posterPath);

        Picasso.with(mContext)
                .load(posterPath)
                .placeholder(R.color.colorPrimaryDark)
                .into(viewHolder.moviePosterView);

        int titleIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE);
        viewHolder.movieTitle = cursor.getString(titleIndex);
        viewHolder.movieTitleTextView.setText(viewHolder.movieTitle);

        int isFavIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE);
        int isFavourite = cursor.getInt(isFavIndex);

        if (isFavourite == 1){
            Picasso.with(mContext)
                    .load(R.drawable.ic_favorite_white_24dp)
                    .into(viewHolder.favIconImageView);
        }else {
            Picasso.with(mContext)
                    .load(R.drawable.ic_favorite_border_white_24dp)
                    .into(viewHolder.favIconImageView);
        }

        viewHolder.favIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri deleteUri = FavouriteContract.FavouriteEntry.buildByMovieIdUri(viewHolder.movieId);

                int rowsDeleted = mContext.getContentResolver().delete(
                        deleteUri,
                        null,
                        null
                );

                if (rowsDeleted != 0){
                    Toast.makeText(mContext, "Movie: " + viewHolder.movieTitle + " deleted successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    Log.v(LOG_TAG, "Rows deleted: " + rowsDeleted);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView moviePosterView;
        public TextView movieTitleTextView;
        public RelativeLayout cardContainer;
        public ImageView favIconImageView;
        public Context mContext;
        public long movieId;
        public String movieTitle;

        public ViewHolder(View itemView, Context context){
            super(itemView);
            this.favIconImageView = (ImageView) itemView.findViewById(R.id.card_fav_icon);
            this.moviePosterView = (ImageView) itemView.findViewById(R.id.poster_image_item);
            this.movieTitleTextView = (TextView) itemView.findViewById(R.id.card_title);
            this.cardContainer = (RelativeLayout) itemView.findViewById(R.id.card_text_container);
            this.mContext = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Uri uri = FavouriteContract.FavouriteEntry.buildByMovieIdUri(movieId);

            Intent intent = new Intent(mContext, MovieDetailActivity.class);
            intent.putExtra(GlobalData.INTENT_KEY_URI, uri.toString());
            mContext.startActivity(intent);
        }
    }
}
