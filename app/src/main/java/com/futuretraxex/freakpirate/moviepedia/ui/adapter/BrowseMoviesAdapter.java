package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class BrowseMoviesAdapter extends
        RecyclerView.Adapter<BrowseMoviesAdapter.ViewHolder> {

    private final String LOG_TAG = BrowseMoviesAdapter.class.getSimpleName();

    private ArrayList<MovieDataModel> mMovieDataModel;
    private Context mContext;

    public BrowseMoviesAdapter(ArrayList<MovieDataModel> data, Context context){
        this.mMovieDataModel = data;
        this.mContext = context;
    }


    // Inflating layout from xml and returning the view holder
    @Override
    public BrowseMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View browseView = inflater.inflate(R.layout.item_browse_movies, parent, false);

        // Return a new holder instance
         return new ViewHolder(context, mMovieDataModel, browseView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // get data model based on position
        MovieDataModel data = mMovieDataModel.get(position);

        holder.favIconImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);

        Uri movieIdUri = FavouriteContract.FavouriteEntry.buildByMovieIdUri(data.getMOVIE_ID());
        Cursor cursorIsFav = mContext.getContentResolver().query(
                movieIdUri,
                new String[]{FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE},
                null,
                null,
                null
        );

        if (cursorIsFav != null){
            if (cursorIsFav.getCount() != 0){
                holder.favIconImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
            }else {
                holder.favIconImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }
            cursorIsFav.close();
        }else {
            Log.v(LOG_TAG, mMovieDataModel.get(position).getMOVIE_TITLE() + ": Cursor is null");
            holder.favIconImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        TextView movieTitle = holder.movieTitle;
        movieTitle.setText(data.getMOVIE_TITLE());

        // set item views based on data model
        Picasso.with(mContext)
                .load(data.getPOSTER_PATH(GlobalData.size_w92))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        int mToolbarColor = mContext.getResources().getColor(R.color.colorPrimary);
                        int mStatusBarColor = mContext.getResources().getColor(R.color.colorPrimaryDark);

                        Palette palette = Palette.from(bitmap).generate();
                        int toolbarColor = palette.getVibrantColor(mToolbarColor);
                        int statusBarColor = palette.getVibrantColor(mStatusBarColor);


                        RelativeLayout cardContainer = holder.cardContainer;
                        cardContainer.setBackgroundColor(toolbarColor);

                        toolbarColor = Color.argb(120, Color.red(toolbarColor),
                                Color.green(toolbarColor), Color.blue(toolbarColor));

                        mMovieDataModel.get(position).addColors(toolbarColor, statusBarColor);
//                        Log.v(GlobalData.LOG_TAG_DETAIL_ACTIVITY_FRAGMENT,
//                                "Toolbar: " + toolbarColor + "\tStatusBar: " + statusBarColor);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        final ImageView moviePosterView = holder.moviePosterView;
        Picasso.with(mContext)
                .load(data.getPOSTER_PATH(GlobalData.size_w342))
                .placeholder(R.color.colorPrimaryDark)
                .into(moviePosterView);

        holder.favIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDataModel movieData = mMovieDataModel.get(position);
                long movieId = movieData.getMOVIE_ID();
                String movieTitle = movieData.getMOVIE_TITLE();

                Uri uri = FavouriteContract.FavouriteEntry.buildByMovieIdUri(movieId);

                Cursor cursor = mContext.getContentResolver().query(
                        uri,
                        new String[]{FavouriteContract.FavouriteEntry._ID},
                        null,
                        null,
                        null
                );

                if (cursor != null) {
                    if (cursor.getCount() != 0) {
//                        Data already present and needs to be deleted first
                        int rowsDeleted = mContext.getContentResolver().delete(
                                uri,
                                null,
                                null
                        );

                        if (rowsDeleted != 0) {
                            Toast.makeText(mContext, movieTitle + " removed from favourites successfully!", Toast.LENGTH_SHORT).show();
                            holder.favIconImageView.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                        } else {
                            Log.e(LOG_TAG, movieTitle + " is not deleted!");
                        }
                    } else {
//                        Data is not present in database and needs to be inserted
                        Log.v(LOG_TAG, "Empty Cursor");
                        int adult;
                        if (movieData.getADULT()) {
                            adult = 1;
                        } else {
                            adult = 0;
                        }

                        ContentValues favouriteValues = new ContentValues();
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, movieId);
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE, movieTitle);
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW, movieData.getPLOT_SYNOPSIS());
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_BACKDROP_PATH, movieData.getBACKDROP_PATH(GlobalData.size_w500));
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH, movieData.getPOSTER_PATH(GlobalData.size_w342));
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE, movieData.getRELEASE_DATE());
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_VOTE_AVERAGE, movieData.getAVERAGE_RATINGS());
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_POPULARITY, movieData.getPOPULARITY());
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_ADULT, adult);
                        favouriteValues.put(FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE, 1);

                        Uri newUri = mContext.getContentResolver().insert(
                                FavouriteContract.FavouriteEntry.CONTENT_URI,
                                favouriteValues
                        );

                        Log.v(LOG_TAG, "Returned URI: " + newUri);

                        if (newUri != null) {
                            Cursor cursorCheck;
                            cursorCheck = mContext.getContentResolver().query(
                                    newUri,
                                    new String[]{FavouriteContract.FavouriteEntry._ID},
                                    null,
                                    null,
                                    null
                            );

                            if (cursorCheck != null) {
                                if (cursorCheck.getCount() != 0) {
                                    Toast.makeText(mContext, movieTitle + " inserted Successfully!", Toast.LENGTH_SHORT).show();
                                    holder.favIconImageView.setImageResource(R.drawable.ic_favorite_white_24dp);
                                } else {
                                    Log.e(LOG_TAG, "Insertion into favourites unsuccessfull!");
                                }
                                cursorCheck.close();
                            } else {
                                Log.e(LOG_TAG, "Insert cursor is returned to be null!");
                            }
                        } else {
                            Log.e(LOG_TAG, "Returned Uri is null!");
                        }

                    }
                    cursor.close();
                } else {
                    Log.e(LOG_TAG, "Cursor is null");
                }
            }
        });

    }

    // Clean all elements of the recycler
    public void clear() {
        mMovieDataModel.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<MovieDataModel> list) {
        mMovieDataModel.addAll(list);
        notifyDataSetChanged();
    }

    //Return the total number of items
    @Override
    public int getItemCount() {
        return mMovieDataModel.size();
    }


    private void dynamicToolbarColor(final MovieDataModel movie) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView moviePosterView;
        public ImageView favIconImageView;
        public TextView movieTitle;
        public RelativeLayout cardContainer;
        private List<MovieDataModel> movieDataModelList;
        private Context context;

        public ViewHolder(Context context, List<MovieDataModel> list, View itemView){
            super(itemView);
            this.movieDataModelList = list;
            this.context = context;
            this.moviePosterView = (ImageView) itemView.findViewById(R.id.poster_image_item);
            this.movieTitle = (TextView) itemView.findViewById(R.id.card_title);
            this.cardContainer = (RelativeLayout) itemView.findViewById(R.id.card_text_container);
            this.favIconImageView = (ImageView) itemView.findViewById(R.id.card_fav_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            MovieDataModel details = movieDataModelList.get(position);
            ((Callback) context).onItemSelected(details);

//            Intent intent = new Intent(context, MovieDetailActivity.class);
//            intent.putExtra(GlobalData.INTENT_KEY_MOVIE_MODEL, details);
//            context.startActivity(intent);
        }
    }
}