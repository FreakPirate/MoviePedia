package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.futuretraxex.freakpirate.moviepedia.BuildConfig;
import com.futuretraxex.freakpirate.moviepedia.Models.VideosResult;
import com.futuretraxex.freakpirate.moviepedia.backend.GeneralizedAPI;
import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.Models.ReviewModel;
import com.futuretraxex.freakpirate.moviepedia.Models.ReviewResult;
import com.futuretraxex.freakpirate.moviepedia.Models.VideosModel;
import com.futuretraxex.freakpirate.moviepedia.data.provider.FavouriteContract;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.listener.CustomOnClickListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
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

    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    public static final String DETAIL_MODEL = "MODEL";

    private MovieDataModel mDataModel;

    @Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.movie_release_date) TextView movieReleaseDate;
    @Bind(R.id.movie_average_rating) TextView movieAverageRating;
    @Bind(R.id.plot_synopsis) TextView movieSynopsis;
    @Bind(R.id.plot_synopsis_title) TextView movieSynopsisTitle;
    @Bind(R.id.movie_adult) TextView movieAdult;
    @Bind(R.id.movie_poster) ImageView moviePosterImageView;
    @Bind(R.id.movie_cover) ImageView movieCoverImageView;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsedToolbar;
    @Bind(R.id.movie_details_ll) LinearLayout reviewTrailerLL;
    @Bind(R.id.play_icon_backdrop) ImageView playIconBackdrop;
    @Bind(R.id.detail_rating_bar) RatingBar ratingBar;
    @Bind(R.id.fab_icon) FloatingActionButton fabIcon;

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
        context = getActivity();
        mToolbarColor = context.getResources().getColor(R.color.colorPrimary);
        mStatusBarColor = context.getResources().getColor(R.color.colorPrimaryDark);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getActivity().finish();
            return true;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        setHasOptionsMenu(true);

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();

        if (arguments != null){
            mDataModel = arguments.getParcelable(MovieDetailFragment.DETAIL_MODEL);
        }

        if (mDataModel != null){
            inflateView();
            if (mDataModel.getTOOLBAR_COLOR() == getResources().getColor(R.color.colorPrimary)){
                Log.v(LOG_TAG, "In CheckParcelableColors! toolbarColor == colorPrimary");
                getToolbarColor();
            }
        }else {
            Log.e(LOG_TAG, "mDataModel is NULL!");
        }

        return rootView;
    }

    public void inflateView(){
        movieCoverImageView.setBackgroundColor(mDataModel.getTOOLBAR_COLOR());

        Log.v(LOG_TAG, "In inflateView! PosterPath: " + mDataModel.getPOSTER_PATH(GlobalData.size_w342));

        Picasso.with(context)
                .load(mDataModel.getPOSTER_PATH(GlobalData.size_w342))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(300, 450)
//                .centerInside()
                .into(moviePosterImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(mDataModel.getPOSTER_PATH(GlobalData.size_w342))
                                .error(R.drawable.image_load_error)
                                .resize(300,450)
                                .into(moviePosterImageView);
                    }
                });

        Picasso.with(context)
                .load(mDataModel.getBACKDROP_PATH(GlobalData.size_w500))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(movieCoverImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(mDataModel.getBACKDROP_PATH(GlobalData.size_w500))
                                .error(R.drawable.image_load_error)
                                .into(movieCoverImageView);
                    }
                });

        movieTitle.setText(mDataModel.getMOVIE_TITLE());

//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
//        String releaseDate = formatter.format(Date.parse(mDataModel.getRELEASE_DATE()));

        String releaseDate = dateFormatter(mDataModel.getRELEASE_DATE());
        String averageRating = "TMDB: " + mDataModel.getAVERAGE_RATINGS();
        String adult = "Adult: " + mDataModel.getADULT();

        movieSynopsisTitle.setTextColor(mDataModel.getSTATUS_BAR_COLOR());
        movieReleaseDate.setText(releaseDate);
        movieAverageRating.setText(averageRating);
        ratingBar.setRating(mDataModel.getAVERAGE_RATINGS() / 2);
        movieSynopsis.setText(mDataModel.getPLOT_SYNOPSIS());
        movieAdult.setText(adult);

        if (collapsedToolbar != null){
            collapsedToolbar.setTitle(mDataModel.getMOVIE_TITLE());
            collapsedToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
            collapsedToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);

            mToolbarColor = mDataModel.getTOOLBAR_COLOR();
            mStatusBarColor = mDataModel.getSTATUS_BAR_COLOR();
            collapsedToolbar.setContentScrimColor(mToolbarColor);
            collapsedToolbar.setStatusBarScrimColor(mToolbarColor);
        }

//        moviePosterImageView.setBorderColor(mStatusBarColor);
//        moviePosterImageView.setBorderWidth(5);
//        moviePosterImageView.setShadowRadius(11);
//        moviePosterImageView.setShadowColor(mToolbarColor);

    }

    public String dateFormatter(String initialDate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(initialDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mDataModel != null){

            long movieIdLong = mDataModel.getMOVIE_ID();
            final int movieId = (int) movieIdLong;

            if (mDataModel.getIS_FAVOURITE()){
                fabIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
            }else {
                fabIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }

            fabIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataModel.getIS_FAVOURITE()){
                        mDataModel.setIS_FAVOURITE(false);
                        fabIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                        int deletedRows = context.getContentResolver().delete(
                                FavouriteContract.FavouriteEntry.buildByMovieIdUri(mDataModel.getMOVIE_ID()),
                                null,
                                null
                        );

                        if (deletedRows != 0){
                            Snackbar snackbar = Snackbar.make(
                                    getView(),
                                    mDataModel.getMOVIE_TITLE() + " deleted successfully from database",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        Log.v(LOG_TAG, "Row deleted successfully!");
                    }else {
                        mDataModel.setIS_FAVOURITE(true);
                        fabIcon.setImageResource(R.drawable.ic_favorite_white_24dp);

                        int adult;
                        if (mDataModel.getADULT())
                            adult = 1;
                        else
                            adult = 0;

                        ContentValues values = new ContentValues();

                        values.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, mDataModel.getMOVIE_ID());
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE, mDataModel.getMOVIE_TITLE());
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW, mDataModel.getPLOT_SYNOPSIS());
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_BACKDROP_PATH, mDataModel.getBACKDROP_PATH(GlobalData.size_w500));
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH, mDataModel.getPOSTER_PATH(GlobalData.size_w342));
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE, mDataModel.getRELEASE_DATE());
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_VOTE_AVERAGE, mDataModel.getAVERAGE_RATINGS());
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_POPULARITY, mDataModel.getPOPULARITY());
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_ADULT, adult);
                        values.put(FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE, 1);

                        Uri newUri = context.getContentResolver().insert(
                                FavouriteContract.FavouriteEntry.CONTENT_URI,
                                values
                        );

                        Log.v(LOG_TAG, "Returned URI: " + newUri);

                        if (newUri != null) {
                            Cursor cursorCheck;
                            cursorCheck = context.getContentResolver().query(
                                    newUri,
                                    new String[]{FavouriteContract.FavouriteEntry._ID},
                                    null,
                                    null,
                                    null
                            );

                            if (cursorCheck != null && cursorCheck.getCount() != 0) {
                                Snackbar snackbar = Snackbar.make(
                                        getView(),
                                        mDataModel.getMOVIE_TITLE() + " inserted successfully into database",
                                        Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                cursorCheck.close();
                            } else {
                                Log.e(LOG_TAG, "Insert cursor is returned to be null!");
                            }
                        } else {
                            Log.e(LOG_TAG, "Returned Uri is null!");
                        }
                    }
                }
            });

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
            final Call<VideosModel> callVideos = generalizedAPI.getMovieVideos(movieId, API_KEY);

            callReview.enqueue(new Callback<ReviewModel>() {
                @Override
                public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {

                    if (response.body().getTotalResults() != 0 && getActivity() != null){
                        loadReviews(response.body());

                        callVideos.enqueue(new Callback<VideosModel>() {
                            @Override
                            public void onResponse(Call<VideosModel> call, Response<VideosModel> response) {
                                if (response.body().getResults().size() != 0 && getActivity() != null) {
                                    loadVideos(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<VideosModel> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ReviewModel> call, Throwable t) {

                }
            });
        }
        super.onViewCreated(view, savedInstanceState);
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
        titleView.setTextColor(mDataModel.getSTATUS_BAR_COLOR());
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

    private void loadVideos(final VideosModel result){

        movieCoverImageView.setOnClickListener(new CustomOnClickListener(getActivity(), result, 0));

        View lineBreakView = new View(getActivity());
        lineBreakView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.line_break_height)));
        lineBreakView.setBackgroundColor(getResources().getColor(R.color.line_break_color));

        reviewTrailerLL.addView(lineBreakView);

        TextView titleView = new TextView(getActivity());
        titleView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleView.setTextColor(mDataModel.getSTATUS_BAR_COLOR());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_size_xxlarge));
        titleView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.synopsis_padding), 0, 0);
        titleView.setText("Trailers");
        reviewTrailerLL.addView(titleView);

        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int trailerCount = result.getResults().size();

        playIconBackdrop.setVisibility(View.VISIBLE);

        for (int i=0; i<trailerCount; i++){
            final VideosResult vid = result.getResults().get(i);
            Log.v(MovieDetailFragment.class.getSimpleName(), "Trailer " + i + ": " + result.getResults().toString());

            View view = inflater.inflate(R.layout.item_trailer, null);

            TextView trailerTitle = (TextView) view.findViewById(R.id.trailer_title_textView);
            String title = vid.getType() + " (" + vid.getSite() + "): " + vid.getName() + " " + vid.getSize() + "p";
            trailerTitle.setText(title);

            ImageView playView = (ImageView) view.findViewById(R.id.trailer_play_view);
            playView.setColorFilter(mDataModel.getSTATUS_BAR_COLOR());

            view.setOnClickListener(new CustomOnClickListener(getActivity(), result, i));

            reviewTrailerLL.addView(view);
        }


    }

    private void getToolbarColor() {
        Picasso.with(getActivity())
                .load(mDataModel.getPOSTER_PATH(GlobalData.size_w92))
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

                        mDataModel.addColors(mToolbarColor, mStatusBarColor);
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

//    private MovieDataModel fetchUriData(Uri uri){
//        MovieDataModel fetchModel;
//
//        Cursor cursor = context.getContentResolver().query(
//                uri,
//                null,
//                null,
//                null,
//                null
//        );
//
//        if (cursor != null && cursor.getCount() != 0){
//            Log.v(LOG_TAG, "Cursor is not empty! CursorCount: " + cursor.getCount());
//            cursor.moveToFirst();
//
//            int indexMovieId = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID);
//            long movieId = cursor.getInt(indexMovieId);
//
//            int indexMovieTitle = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE);
//            String movieTitle = cursor.getString(indexMovieTitle);
//
//            int indexOverview = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_OVERVIEW);
//            String overview = cursor.getString(indexOverview);
//
//            int indexBackdropPath = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_BACKDROP_PATH);
//            String backdropPath = cursor.getString(indexBackdropPath);
//
//            int indexPosterPath = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POSTER_PATH);
//            String posterPath = cursor.getString(indexPosterPath);
//
//            int indexReleaseDate = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_RELEASE_DATE);
//            String releaseDate = cursor.getString(indexReleaseDate);
//
//            int indexPopularity = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_POPULARITY);
//            float popularity = cursor.getFloat(indexPopularity);
//
//            int indexVoteAverage = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_VOTE_AVERAGE);
//            float voteAverage = cursor.getFloat(indexVoteAverage);
//
//            int indexAdult = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_ADULT);
//            int adultTemp = cursor.getInt(indexAdult);
//            boolean adult = adultTemp == 1;
//
//            int indexIsFavourite = cursor.getColumnIndex(FavouriteContract.FavouriteEntry.COLUMN_IS_FAVOURITE);
//            int isFavouriteTemp = cursor.getInt(indexIsFavourite);
//            boolean isFavourite = isFavouriteTemp == 1;
//
//            fetchModel = new MovieDataModel(movieTitle, movieId, posterPath, backdropPath, overview,
//                    voteAverage, popularity, releaseDate, adult, isFavourite, mToolbarColor, mStatusBarColor, context);
//        }
//        else {
//            Log.e(LOG_TAG, "Intent Cursor is either empty or cannot be opened!");
//            return null;
//        }
//
//        return fetchModel;
//    }
}
