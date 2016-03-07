package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieData;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private MovieData info;

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView movieAverageRating;
    @Bind(R.id.plot_synopsis) TextView movieSynopsis;
    @Bind(R.id.movie_adult) TextView movieAdult;

    @Bind(R.id.movie_poster) ImageView moviePosterImageView;
    @Bind(R.id.movie_cover) ImageView movieCoverImageView;

    @BindColor(R.color.poster_white) int white;
    @BindColor(R.color.poster_gray) int gray;

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;

    private Context context;

    public MovieDetailFragment() {
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING)) {
            info = intent.getParcelableExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING);
            toolbarTextAppearance();
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
                .resize(300,450)
                .into(moviePosterImageView);

        Picasso.with(context)
                .load(info.getBACKDROP_PATH())
//                    .error(R.drawable.placeholder_backdrop)
                .into(movieCoverImageView);

        movieTitle.setText(info.getMOVIE_TITLE());

        String releaseDate = "Release Date: " + info.getRELEASE_DATE();
        String averageRating = "Average Rating: " + info.getAVERAGE_RATINGS();
        String adult = "Adult: " + info.getADULT();

        movieReleaseDate.setText(releaseDate);
        movieAverageRating.setText(averageRating);
        movieSynopsis.setText(info.getPLOT_SYNOPSIS());
        movieAdult.setText(adult);
    }

    private void toolbarTextAppearance(){
        collapsingToolbarLayout.setTitle(info.getMOVIE_TITLE());

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);


        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.placeholder_poster);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(
                        getResources().getColor(R.color.colorPrimary)));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(
                        getResources().getColor(R.color.colorPrimaryDark)));
            }
        });
    }
}
