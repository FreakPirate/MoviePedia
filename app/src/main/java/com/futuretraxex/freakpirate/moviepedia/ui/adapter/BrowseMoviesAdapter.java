package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.MovieDetailActivity;
import com.futuretraxex.freakpirate.moviepedia.data.Models.MovieDataModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class BrowseMoviesAdapter extends
        RecyclerView.Adapter<BrowseMoviesAdapter.ViewHolder> {

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

        ImageView moviePosterView = holder.moviePosterView;
        Picasso.with(mContext)
                .load(data.getPOSTER_PATH(GlobalData.size_w342))
                .placeholder(R.color.colorPrimaryDark)
                .into(moviePosterView);

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            MovieDataModel details = movieDataModelList.get(position);

            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING, details);
            context.startActivity(intent);

        }
    }
}