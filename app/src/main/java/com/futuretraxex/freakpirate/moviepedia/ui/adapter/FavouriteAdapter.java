package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.provider.FavouriteContract;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by FreakPirate on 5/22/2016.
 */
public class FavouriteAdapter extends CursorAdapter {
    private final String LOG_TAG = FavouriteAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderId;

    public FavouriteAdapter(Context context, Cursor cursor, int flags, int loaderId){
        super(context, cursor, flags);

        mContext = context;
        sLoaderId = loaderId;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_browse_movies, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In Bind View");

        int posterIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH);
        String posterPath = cursor.getString(posterIndex);
        Log.i(LOG_TAG, "Extracted Poster path: " + posterPath);

        Picasso.with(mContext)
                .load(posterPath)
                .placeholder(R.color.colorPrimaryDark)
                .into(viewHolder.moviePosterView);

        int titleIndex = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE);
        String movieTitle = cursor.getString(titleIndex);
        viewHolder.movieTitle.setText(movieTitle);

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
    }

    public static class ViewHolder{
        public ImageView moviePosterView;
        public TextView movieTitle;
        public RelativeLayout cardContainer;
        public ImageView favIconImageView;

        public ViewHolder(View itemView){
            this.favIconImageView = (ImageView) itemView.findViewById(R.id.card_fav_icon);
            this.moviePosterView = (ImageView) itemView.findViewById(R.id.poster_image_item);
            this.movieTitle = (TextView) itemView.findViewById(R.id.card_title);
            this.cardContainer = (RelativeLayout) itemView.findViewById(R.id.card_text_container);
        }
    }
}
