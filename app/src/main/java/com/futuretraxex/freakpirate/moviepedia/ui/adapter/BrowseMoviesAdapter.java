package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrowseMoviesAdapter extends
        RecyclerView.Adapter<BrowseMoviesAdapter.ViewHolder> {

    private List<MovieData> mMovieData;
    private Context mContext;

    public BrowseMoviesAdapter(List<MovieData> data, Context context){
        this.mMovieData = data;
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
        ViewHolder viewHolder = new ViewHolder(browseView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // get data model based on position
        MovieData data = mMovieData.get(position);

        // set item views based on data model
        ImageView moviePosterView = holder.moviePosterView;

        Picasso.with(mContext)
                .load(data.getPOSTER_PATH())
//                .placeholder(R.drawable.placeholder_poster)
                .into(moviePosterView);
    }

    //Return the total number of items
    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView moviePosterView;
        public TextView movieTitle;

        public ViewHolder(View itemView){
            super(itemView);

            moviePosterView = (ImageView) itemView.findViewById(R.id.poster_image_item);
        }
    }
}