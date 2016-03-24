package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.futuretraxex.freakpirate.moviepedia.BuildConfig;
import com.futuretraxex.freakpirate.moviepedia.backend.GeneralizedAPI;
import com.futuretraxex.freakpirate.moviepedia.backend.URIBuilder;
import com.futuretraxex.freakpirate.moviepedia.data.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.Models.ReviewModel;
import com.futuretraxex.freakpirate.moviepedia.data.Models.ReviewResult;
import com.futuretraxex.freakpirate.moviepedia.data.Models.TrailerModel;
import com.futuretraxex.freakpirate.moviepedia.data.Models.TrailerResult;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.listener.CustomOnClickListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private MovieDataModel movieDataModel;

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView movieAverageRating;
    @Bind(R.id.plot_synopsis) TextView movieSynopsis;
    @Bind(R.id.plot_synopsis_title) TextView movieSynopsisTitle;
    @Bind(R.id.movie_adult) TextView movieAdult;
    @Bind(R.id.movie_poster) CircularImageView moviePosterImageView;
    @Bind(R.id.movie_cover) ImageView movieCoverImageView;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsedToolbar;
    @Bind(R.id.movie_details_ll) LinearLayout reviewTrailerLL;
    @Bind(R.id.play_icon_backdrop) ImageView playIconBackdrop;

    int mToolbarColor;
    int mStatusBarColor;

    private final String BASE_URL_REVIEW_TRAILER = "https://api.themoviedb.org/";
    private final String API_KEY = BuildConfig.API_KEY;

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

            checkParcelableColors();
        }else {
            Log.d(GlobalData.LOG_TAG_DETAIL_ACTIVITY_FRAGMENT, "Unable to fetch Intent data");
        }

        return rootView;
    }

    public void checkParcelableColors(){
        if (movieDataModel.getTOOLBAR_COLOR() == getResources().getColor(R.color.colorPrimary)){
            getToolbarColor();
        }else {
            inflateView();
        }
    }

    public void inflateView(){

        movieCoverImageView.setBackgroundColor(movieDataModel.getTOOLBAR_COLOR());

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

        movieSynopsisTitle.setTextColor(movieDataModel.getSTATUS_BAR_COLOR());
        movieReleaseDate.setText(releaseDate);
        movieAverageRating.setText(averageRating);
        movieSynopsis.setText(movieDataModel.getPLOT_SYNOPSIS());
        movieAdult.setText(adult);

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

        int movieId = Integer.parseInt(movieDataModel.getMOVIE_ID());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_REVIEW_TRAILER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        GeneralizedAPI generalizedAPI = retrofit.create(GeneralizedAPI.class);

        Call<ReviewModel> callReview = generalizedAPI.getMovieReviews(movieId, API_KEY);
        final Call<TrailerModel> callTrailer = generalizedAPI.getMovieTrailer(movieId, API_KEY);

        callReview.enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {

                if (response.body().getTotalResults() != 0){
                    loadReviews(response.body());
                }

                callTrailer.enqueue(new Callback<TrailerModel>() {
                    @Override
                    public void onResponse(Call<TrailerModel> call, Response<TrailerModel> response) {
                        if (response.body().getYoutube().size() != 0){
                            loadTrailer(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerModel> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {

            }
        });
    }


    private void loadReviews(ReviewModel reviewContent){

        int reviewCount = reviewContent.getTotalResults();

        View lineBreakView = new View(getActivity());
        lineBreakView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.line_break_height)));
        lineBreakView.setBackgroundColor(getResources().getColor(R.color.line_break_color));

        reviewTrailerLL.addView(lineBreakView);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView titleView = new TextView(getActivity());
        titleView.setLayoutParams(layoutParams);
        titleView.setTextColor(movieDataModel.getSTATUS_BAR_COLOR());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_xxlarge));
        titleView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.synopsis_padding), 0, 0);
        titleView.setText("Reviews");
        reviewTrailerLL.addView(titleView);

        for (int i=0; i<reviewCount; i++){
            ReviewResult result = reviewContent.getResults().get(i);
            String author = result.getAuthor();
            String content = result.getContent();

            TextView authorView = new TextView(getActivity());
            authorView.setLayoutParams(layoutParams);
            authorView.setTextColor(getResources().getColor(R.color.body_text_1));
            authorView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_large));
            authorView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.synopsis_padding), 0, 0);
            authorView.setText(author);

            TextView contentView = new TextView(getActivity());
            contentView.setLayoutParams(layoutParams);
            contentView.setTextColor(getResources().getColor(R.color.body_text_2));
            contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_medium));
            contentView.setText(content);

            if (i == reviewCount-1){
                contentView.setPadding(0,0,0,getResources().getDimensionPixelSize(R.dimen.synopsis_padding));
            }

            reviewTrailerLL.addView(authorView);
            reviewTrailerLL.addView(contentView);
        }
    }

    private void loadTrailer(final TrailerModel result){

        playIconBackdrop.setVisibility(View.VISIBLE);
        movieCoverImageView.setOnClickListener(new CustomOnClickListener(getActivity(), result, 0));

        View lineBreakView = new View(getActivity());
        lineBreakView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.line_break_height)));
        lineBreakView.setBackgroundColor(getResources().getColor(R.color.line_break_color));

        reviewTrailerLL.addView(lineBreakView);

        TextView titleView = new TextView(getActivity());
        titleView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleView.setTextColor(movieDataModel.getSTATUS_BAR_COLOR());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_xxlarge));
        titleView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.synopsis_padding), 0, 0);
        titleView.setText("Trailers");
        reviewTrailerLL.addView(titleView);

        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int trailerCount = result.getYoutube().size();

        for (int i=0; i<trailerCount; i++){
            final TrailerResult tr = result.getYoutube().get(i);
            Log.v(MovieDetailFragment.class.getSimpleName(), "Trailer " + i + ": " + result.getYoutube().toString());

            View view = inflater.inflate(R.layout.item_trailer, null);

            TextView trailerTitle = (TextView) view.findViewById(R.id.trailer_title_textView);
            String title = "Youtube: " + tr.getName();
            trailerTitle.setText(title);

            ImageView playView = (ImageView) view.findViewById(R.id.trailer_play_view);
            playView.setColorFilter(movieDataModel.getSTATUS_BAR_COLOR());

            view.setOnClickListener(new CustomOnClickListener(getActivity(), result, i));

            reviewTrailerLL.addView(view);
        }


    }

    private void getToolbarColor() {
        Picasso.with(getActivity())
                .load(movieDataModel.getPOSTER_PATH(GlobalData.size_w92))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette palette = Palette.from(bitmap).generate();
                        int toolbarColor = palette.getVibrantColor(mToolbarColor);
                        int statusBarColor = palette.getVibrantColor(mStatusBarColor);

                        mToolbarColor = Color.argb(120, Color.red(toolbarColor),
                                Color.green(toolbarColor), Color.blue(toolbarColor));

                        mStatusBarColor = Color.argb(250, Color.red(statusBarColor),
                                Color.green(statusBarColor), Color.blue(statusBarColor));

//                        Log.v(GlobalData.LOG_TAG_DETAIL_ACTIVITY_FRAGMENT,
//                                "Toolbar: " + toolbarColor + "\tStatusBar: " + statusBarColor);

                        movieDataModel.addColors(mToolbarColor, mStatusBarColor);
                        inflateView();
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
