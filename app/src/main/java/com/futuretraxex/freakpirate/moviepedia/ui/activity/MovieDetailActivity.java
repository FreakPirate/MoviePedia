package com.futuretraxex.freakpirate.moviepedia.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.futuretraxex.freakpirate.moviepedia.ui.fragment.MovieDetailFragment;
import com.futuretraxex.freakpirate.moviepedia.R;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, new MovieDetailFragment())
                    .commit();
        }
    }

}
