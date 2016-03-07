package com.futuretraxex.freakpirate.moviepedia.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.backend.FetchDBTask;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrowseMoviesFragment extends Fragment {

    private String SORT_ORDER;
    private Boolean SAFE_SEARCH;

    private View rootView;

//    MoviePoster [] moviePosters = {
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster)
//    };

    public BrowseMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        BrowseMoviesAdapter moviePosterAdapter = new BrowseMoviesAdapter(getActivity(), Arrays.asList(moviePosters));
//        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
//        gridView.setAdapter(moviePosterAdapter);
        setHasOptionsMenu(true);

        if(isNetworkAvailable()){
            rootView = inflater.inflate(R.layout.fragment_browse_movies, container, false);
            updateUI();
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
            updateUI();
        }
        super.onResume();
    }

    private void networkHandler(View rootView, String sortOrder, Boolean safeSearch){

        FetchDBTask dbTask = new FetchDBTask(getActivity(), rootView, safeSearch);
        dbTask.execute(sortOrder);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateUI(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SORT_ORDER = sharedPreferences.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default_value)
        );

        SAFE_SEARCH = sharedPreferences.getBoolean(
                getString(R.string.pref_adult_key),
                true
        );

        networkHandler(rootView, SORT_ORDER, SAFE_SEARCH);
    }
}
