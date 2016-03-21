package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.MovieDetailActivity;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieDataModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrowseMoviesAdapter extends
        RecyclerView.Adapter<BrowseMoviesAdapter.ViewHolder> {

    private List<MovieDataModel> mMovieDataModel;
    private Context mContext;

    public BrowseMoviesAdapter(List<MovieDataModel> data, Context context){
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        // get data model based on position
        MovieDataModel data = mMovieDataModel.get(position);

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
        return mMovieDataModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView moviePosterView;
        public TextView movieTitle;
        private List<MovieDataModel> movieDataModelList;
        private Context context;

        public ViewHolder(Context context, List<MovieDataModel> list, View itemView){
            super(itemView);
            this.movieDataModelList = list;
            this.context = context;
            this.moviePosterView = (ImageView) itemView.findViewById(R.id.poster_image_item);

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