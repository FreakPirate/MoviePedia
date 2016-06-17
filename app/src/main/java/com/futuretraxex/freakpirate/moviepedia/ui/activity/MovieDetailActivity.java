package com.futuretraxex.freakpirate.moviepedia.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.Utility;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.MovieDetailFragment;
import com.futuretraxex.freakpirate.moviepedia.R;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            MovieDataModel model;

            Intent intent = getIntent();
            if (intent != null){
                if (intent.hasExtra(GlobalData.INTENT_KEY_MOVIE_MODEL)){
                    model = intent.getParcelableExtra(GlobalData.INTENT_KEY_MOVIE_MODEL);
                }else if (intent.hasExtra(GlobalData.INTENT_KEY_URI)) {
                    Uri uri = intent.getParcelableExtra(GlobalData.INTENT_KEY_URI);
                    model = Utility.fetchDataFromUri(this, uri);
                }else {
                    Log.e(LOG_TAG, "Intent has neither extras!");
                    model = null;
                }
            }else {
                Log.e(LOG_TAG, "Intent is null!");
                model = null;
            }

            args.putParcelable(MovieDetailFragment.DETAIL_MODEL, model);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, fragment)
                    .commit();
        }
    }
}
