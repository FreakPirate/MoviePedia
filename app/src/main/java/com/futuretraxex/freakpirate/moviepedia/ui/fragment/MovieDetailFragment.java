package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.futuretraxex.freakpirate.moviepedia.data.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private MovieDataModel movieDataModel;

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView movieAverageRating;
    @Bind(R.id.plot_synopsis) TextView movieSynopsis;
    @Bind(R.id.movie_adult) TextView movieAdult;
    @Bind(R.id.movie_poster) CircularImageView moviePosterImageView;
    @Bind(R.id.movie_cover) ImageView movieCoverImageView;
    @BindColor(R.color.poster_white) int white;
    @BindColor(R.color.poster_gray) int gray;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsedToolbar;

    int mToolbarColor;
    int mStatusBarColor;

    private Context context;

    public MovieDetailFragment() {
        this.context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getActivity().getIntent();


        if (intent != null && intent.hasExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING)) {
            movieDataModel = intent.getParcelableExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING);

            inflateView();
//            dynamicToolbarColor();
            toolbarTextAppearance();
        }else {
            Log.d(GlobalData.LOG_TAG_DETAIL_ACTIVITY_FRAGMENT, "Unable to fetch Intent data");
        }

        return rootView;
    }

    public void inflateView(){

        Picasso.with(context)
                .load(movieDataModel.getPOSTER_PATH(GlobalData.size_w342))
//                .error(R.drawable.placeholder_poster)
//                .resize(300,450)
//                .centerInside()
                .into(moviePosterImageView);

        Picasso.with(context)
                .load(movieDataModel.getBACKDROP_PATH(GlobalData.size_w500))
//                    .error(R.drawable.placeholder_backdrop)
                .into(movieCoverImageView);

        movieTitle.setText(movieDataModel.getMOVIE_TITLE());

        String releaseDate = "Release Date: " + movieDataModel.getRELEASE_DATE();
        String averageRating = "Average Rating: " + movieDataModel.getAVERAGE_RATINGS();
        String adult = "Adult: " + movieDataModel.getADULT();

        movieReleaseDate.setText(releaseDate);
        movieAverageRating.setText(averageRating);
        movieSynopsis.setText(movieDataModel.getPLOT_SYNOPSIS());
        movieAdult.setText(adult);
    }

    private void toolbarTextAppearance(){
        collapsedToolbar.setTitle(movieDataModel.getMOVIE_TITLE());

        collapsedToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsedToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);

        mToolbarColor = movieDataModel.getTOOLBAR_COLOR();
        mStatusBarColor = movieDataModel.getSTATUS_BAR_COLOR();


        collapsedToolbar.setContentScrimColor(mToolbarColor);
        collapsedToolbar.setStatusBarScrimColor(mStatusBarColor);

        moviePosterImageView.setBorderColor(mStatusBarColor);
        moviePosterImageView.setBorderWidth(5);
        moviePosterImageView.setShadowRadius(11);
        moviePosterImageView.setShadowColor(mToolbarColor);
    }

    private void dynamicToolbarColor() {
        Picasso.with(getActivity())
                .load(movieDataModel.getBACKDROP_PATH(GlobalData.size_w92))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette palette = Palette.from(bitmap).generate();
                        int toolbarColor = palette.getDarkVibrantColor(mToolbarColor);
                        int statusBarColor = palette.getDarkVibrantColor(mToolbarColor);

                        mToolbarColor = Color.argb(220, Color.red(toolbarColor),
                                Color.green(toolbarColor), Color.blue(toolbarColor));

                        mStatusBarColor = Color.argb(255, Color.red(statusBarColor),
                                Color.green(statusBarColor), Color.blue(statusBarColor));

//                        Log.v(GlobalData.LOG_TAG_DETAIL_ACTIVITY_FRAGMENT,
//                                "Toolbar: " + toolbarColor + "\tStatusBar: " + statusBarColor);

                        collapsedToolbar.setContentScrimColor(mToolbarColor);
                        collapsedToolbar.setStatusBarScrimColor(mStatusBarColor);

                        moviePosterImageView.setBorderColor(mStatusBarColor);
                        moviePosterImageView.setBorderWidth(5);
                        moviePosterImageView.setShadowRadius(11);
                        moviePosterImageView.setShadowColor(mToolbarColor);

                        movieCoverImageView.setImageDrawable(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }
}
