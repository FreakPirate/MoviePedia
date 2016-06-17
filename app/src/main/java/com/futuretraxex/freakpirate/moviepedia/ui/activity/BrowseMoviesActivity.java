package com.futuretraxex.freakpirate.moviepedia.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.Utility;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.BrowseMoviesFragment;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.ui.fragment.MovieDetailFragment;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.Callback;


public class BrowseMoviesActivity extends AppCompatActivity implements Callback{

    private static final String LOG_TAG = BrowseMoviesActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new BrowseMoviesFragment())
//                    .commit();
//        }
//        if(findViewById(R.id.fragment_movie_detail) != null) {
//
//        }

        if (findViewById(R.id.fragment_movie_detail) != null){
            mTwoPane = true;
        }else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortOrder = Utility.getPreferredSortOrder(this);

        if (sortOrder != null && !sortOrder.equalsIgnoreCase(mSortOrder)){
            BrowseMoviesFragment bmf = (BrowseMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_browse_movies);

            if (bmf != null){
                bmf.onSortOrderChanged();
            }
            mSortOrder = sortOrder;
        }
    }

    @Override
    public void onItemSelected(MovieDataModel model) {
        if (mTwoPane){
            // In two pane mode,
            // show detail view in this activity
            // by adding or replacing the detail fragment using a fragment transcation

            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_MODEL, model);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movie_detail, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(GlobalData.INTENT_KEY_MOVIE_MODEL, model);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(Uri uri) {
        if (mTwoPane){
            // In two pane mode,
            // show detail view in this activity
            // by adding or replacing the detail fragment using a fragment transcation
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_MODEL, Utility.fetchDataFromUri(this, uri));

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movie_detail, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(GlobalData.INTENT_KEY_URI, uri);
            startActivity(intent);
        }
    }
}