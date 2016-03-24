package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.backend.FetchMovieDB;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.adapter.BrowseMoviesAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.DynamicSpanCountCalculator;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.GridSpacingItemDecoration;
import com.futuretraxex.freakpirate.moviepedia.data.Models.MovieDataModel;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrowseMoviesFragment extends Fragment {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recycler_view_browse_movies)
    RecyclerView rvMovieData;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;


    BrowseMoviesAdapter adapter;

    String sortOrder;
    Boolean safeSearch;

    public BrowseMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView;

        if(isNetworkAvailable()){
            rootView = inflater.inflate(R.layout.fragment_browse_movies, container, false);
            ButterKnife.bind(this, rootView);
            initUI();
        }else {
            rootView = inflater.inflate(R.layout.error_egg, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.error_egg_text_view);
            textView.setText(getActivity().getResources().getString(R.string.network_error));

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.error_egg_container);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initUI(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sortOrder = sharedPreferences.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        );

        safeSearch = sharedPreferences.getBoolean(
                getString(R.string.pref_adult_key),
                true
        );

        progressBar.setVisibility(View.VISIBLE);

        FetchMovieDB task = new FetchMovieDB(sortOrder,
                safeSearch,
                1,
                getActivity(),
                new FetchMovieDB.AsyncResponse() {
                    @Override
                    public void onProcessFinish(MovieDataModel[] output) {
                       setAdapter(output);
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
                        adapter.clear();
                        // ...the data has come back, add new items to your adapter...
                        adapter.addAll(new ArrayList<MovieDataModel>(Arrays.asList(output)));
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                    }
                });

        task.execute();
    }

    private void setAdapter(MovieDataModel[] result){
        Context context = getActivity();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_item_spacing);
        boolean includeEdge = true;
        int minItemWidth = getResources().getDimensionPixelSize(R.dimen.min_column_width);
        int spanCount;

        adapter = new BrowseMoviesAdapter(new ArrayList<MovieDataModel>(Arrays.asList(result)), getActivity());
        rvMovieData.setAdapter(adapter);

        DynamicSpanCountCalculator dscc = new DynamicSpanCountCalculator(context, minItemWidth);
        spanCount = dscc.getSpanCount();

        //Hiding progress bar
//        progressBar.setVisibility(View.GONE);

        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        rvMovieData.setLayoutManager(layoutManager);
        rvMovieData.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, includeEdge));


//        rvMovieData.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                loadMoreDataFromApi(page);
//            }
//        });
    }

    private void loadMoreDataFromApi(int offset){

        FetchMovieDB task = new FetchMovieDB(sortOrder,
                safeSearch,
                offset,
                getActivity(),
                new FetchMovieDB.AsyncResponse() {
                    @Override
                    public void onProcessFinish(MovieDataModel[] output) {
                        int curSize = adapter.getItemCount();
                        ArrayList<MovieDataModel> resultArray = new ArrayList<MovieDataModel>(Arrays.asList(output));
                        // updating existing list
                        adapter.addAll(resultArray);
                        // curSize should represent the first element that got added
                        // resultAsArray.size() represents the itemCount
                        adapter.notifyItemRangeInserted(curSize, resultArray.size()-1);
                    }
                });

        task.execute();
    }

}
