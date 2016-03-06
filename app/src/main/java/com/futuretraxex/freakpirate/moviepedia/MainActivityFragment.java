package com.futuretraxex.freakpirate.moviepedia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretraxex.freakpirate.moviepedia.network.FetchDBTask;

import butterknife.Bind;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String SORT_ORDER_POPULARITY = "popularity.desc";
    private final String SORT_ORDER_RATING = "vote_average.desc";

//    MoviePoster [] moviePosters = {
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster),
//            new MoviePoster(R.drawable.ic_poster)
//    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

//        GridViewAdapter moviePosterAdapter = new GridViewAdapter(getActivity(), Arrays.asList(moviePosters));
//        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
//        gridView.setAdapter(moviePosterAdapter);

        if(isNetworkAvailable()){
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            networkHandler(rootView);
        }else {
            rootView = inflater.inflate(R.layout.egg_error_layout, container, false);

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

    private void networkHandler(View rootView){

        FetchDBTask dbTask = new FetchDBTask(getActivity(), rootView);
        dbTask.execute(SORT_ORDER_POPULARITY);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
