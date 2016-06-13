package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.backend.FetchMovieDB;
import com.futuretraxex.freakpirate.moviepedia.data.provider.FavouriteContract;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.BrowseMoviesAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.FavouriteAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.DynamicSpanCountCalculator;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.EndlessRecyclerViewScrollListener;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.GridSpacingItemDecoration;
import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */

public class BrowseMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recycler_view_browse_movies)
    RecyclerView rvMovieData;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.error_db_empty_image_view)
    ImageView errorLayout;
    @Bind(R.id.relativeLayoutMain)
    RelativeLayout parentLayout;

    private BrowseMoviesAdapter mBrowseMoviesAdapter;
    private FavouriteAdapter mFavouriteAdapter;

    private static final int FAVOURITE_LOADER_ID = 1;

    private String sortOrder;
    private Boolean safeSearch;

    private final String LOG_TAG = BrowseMoviesFragment.class.getSimpleName();

    public BrowseMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_browse_movies, container, false);
        ButterKnife.bind(this, rootView);
//            rvMovieData.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if (isNetworkAvailable()) {
            addGridViewDecoration();
        }

        initUI();
        return rootView;
    }


    @Override
    public void onResume() {
        if(GlobalData.preferenceChanged){
            GlobalData.preferenceChanged = false;
            initUI();
        }
        super.onResume();
    }

    private void initUI(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sortOrder = sharedPreferences.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        );

        Log.v(LOG_TAG, "Sort order fetched from shared prefs: " + sortOrder);

        safeSearch = sharedPreferences.getBoolean(
                getString(R.string.pref_adult_key),
                true
        );

        if (sortOrder.equalsIgnoreCase(getString(R.string.pref_value_favourite))){
            Log.v(LOG_TAG, "In Favourites");
            swipeContainer.setEnabled(false);
            rvMovieData.setAdapter(null);

            mFavouriteAdapter = new FavouriteAdapter(getContext(), null, 0, FAVOURITE_LOADER_ID);
            rvMovieData.setAdapter(mFavouriteAdapter);
            getLoaderManager().initLoader(FAVOURITE_LOADER_ID, null, this);
        }else {
            if (isNetworkAvailable()){
                addGridOnScrollListener();
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                swipeContainer.setEnabled(true);

                FetchMovieDB task = new FetchMovieDB(sortOrder,
                        safeSearch,
                        1,
                        getActivity(),
                        new FetchMovieDB.AsyncResponse() {
                            @Override
                            public void onProcessFinish(MovieDataModel[] output) {
                                if (mBrowseMoviesAdapter != null){
                                    mBrowseMoviesAdapter.clear();
                                }

                                mBrowseMoviesAdapter = new BrowseMoviesAdapter(new ArrayList<MovieDataModel>(Arrays.asList(output)), getActivity());
                                rvMovieData.setAdapter(mBrowseMoviesAdapter);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                task.execute();

                // Configure the refreshing colors
                swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
                // Setup refresh listener which triggers new data loading
                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Your code to refresh the list here.
                        // Make sure you call swipeContainer.setRefreshing(false)
                        // once the network request has completed successfully.

                        refreshDataSet();
                    }
                });
            }else {
                Snackbar snackbar = Snackbar.make(parentLayout, "No network connection detected!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Exit", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().finish();
                            }
                        });
                snackbar.show();
                errorLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void refreshDataSet(){
        FetchMovieDB task = new FetchMovieDB(sortOrder,
                safeSearch,
                1,
                getActivity(),
                new FetchMovieDB.AsyncResponse() {
                    @Override
                    public void onProcessFinish(MovieDataModel[] output) {
                        // Remember to CLEAR OUT old items before appending in the new ones
                        mBrowseMoviesAdapter.clear();
                        // ...the data has come back, add new items to your mBrowseMoviesAdapter...
                        mBrowseMoviesAdapter.addAll(new ArrayList<MovieDataModel>(Arrays.asList(output)));
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                    }
                });

        task.execute();
    }

//    private void setAdapter(MovieDataModel[] result){
//        if (mBrowseMoviesAdapter != null){
//            mBrowseMoviesAdapter.clear();
//        }
//
//        mBrowseMoviesAdapter = new BrowseMoviesAdapter(new ArrayList<MovieDataModel>(Arrays.asList(result)), getActivity());
//        rvMovieData.setAdapter(mBrowseMoviesAdapter);
//    }

    private void addGridViewDecoration(){
        Context context = getActivity();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_item_spacing);
        boolean includeEdge = true;
        int minItemWidth = getResources().getDimensionPixelSize(R.dimen.min_column_width);
        int spanCount;

        DynamicSpanCountCalculator dscc = new DynamicSpanCountCalculator(context, minItemWidth);
        spanCount = dscc.getSpanCount();

        //Hiding progress bar
//        progressBar.setVisibility(View.GONE);

        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        rvMovieData.setLayoutManager(layoutManager);

        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(spanCount, spacingInPixels, includeEdge);
        rvMovieData.removeItemDecoration(decoration);
        rvMovieData.addItemDecoration(decoration);
    }

    private void addGridOnScrollListener(){
        Context context = getActivity();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_item_spacing);
        boolean includeEdge = true;
        int minItemWidth = getResources().getDimensionPixelSize(R.dimen.min_column_width);
        int spanCount;

        DynamicSpanCountCalculator dscc = new DynamicSpanCountCalculator(context, minItemWidth);
        spanCount = dscc.getSpanCount();

        //Hiding progress bar
//        progressBar.setVisibility(View.GONE);

        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);

        rvMovieData.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreDataFromApi(page);
            }
        });
    }
    private void loadMoreDataFromApi(int offset){

        FetchMovieDB task = new FetchMovieDB(sortOrder,
                safeSearch,
                offset,
                getActivity(),
                new FetchMovieDB.AsyncResponse() {
                    @Override
                    public void onProcessFinish(MovieDataModel[] output) {
                        int curSize = mBrowseMoviesAdapter.getItemCount();
                        ArrayList<MovieDataModel> resultArray = new ArrayList<MovieDataModel>(Arrays.asList(output));
                        // updating existing list
                        mBrowseMoviesAdapter.addAll(resultArray);
                        // curSize should represent the first element that got added
                        // resultAsArray.size() represents the itemCount
                        mBrowseMoviesAdapter.notifyItemRangeInserted(curSize, resultArray.size() - 1);
                    }
                });

        task.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                FavouriteContract.FavouriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0){
            errorLayout.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar.make(parentLayout, "Database is empty!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else {
            errorLayout.setVisibility(View.GONE);
            mFavouriteAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavouriteAdapter.swapCursor(null);
    }
}
