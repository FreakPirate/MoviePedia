package com.futuretraxex.freakpirate.moviepedia;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private MovieDetails info;

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView movieAverageRating;
    @Bind(R.id.plot_synopsis) TextView movieSynopsis;
    @Bind(R.id.movie_id) TextView movieID;

    @Bind(R.id.movie_poster) ImageView moviePosterImageView;
    @Bind(R.id.movie_cover) ImageView movieCoverImageView;

    @Bind(R.id.movie_poster_container) RelativeLayout layout;

    @BindColor(R.color.poster_white) int white;
    @BindColor(R.color.poster_gray) int gray;

    private Context context;

    public DetailActivityFragment() {
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING)) {
            info = intent.getParcelableExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING);
            inflateView();
        }else {
            Log.d(GlobalData.LOG_TAG_DETAIL_ACTIVITY_FRAGMENT, "Unable to fetch Intent data");
        }

        return rootView;
    }

    public void inflateView(){

        Picasso.with(context)
                .load(info.getPOSTER_PATH())
//                    .error(R.drawable.placeholder_poster)
                .into(moviePosterImageView);

        Picasso.with(context)
                .load(info.getBACKDROP_PATH())
//                    .error(R.drawable.placeholder_backdrop)
                .into(movieCoverImageView);

        movieTitle.setText(info.getMOVIE_TITLE());

        String releaseDate = "Release Date: " + info.getRELEASE_DATE();
        String averageRating = "Average Rating: " + info.getAVERAGE_RATINGS();
        String id = "Movie ID: " + info.getMOVIE_ID();

        movieReleaseDate.setText(releaseDate);
        movieAverageRating.setText(averageRating);
        movieSynopsis.setText(info.getPLOT_SYNOPSIS());

        movieID.setVisibility(View.VISIBLE);
        movieID.setText(id);
    }
}
