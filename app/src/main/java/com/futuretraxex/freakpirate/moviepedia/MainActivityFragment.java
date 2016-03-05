package com.futuretraxex.freakpirate.moviepedia;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.futuretraxex.freakpirate.moviepedia.network.FetchDBTask;

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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        GridViewAdapter moviePosterAdapter = new GridViewAdapter(getActivity(), Arrays.asList(moviePosters));
//        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
//        gridView.setAdapter(moviePosterAdapter);

        networkHandler(rootView);

        return rootView;
    }

    private void networkHandler(View rootView){

        FetchDBTask dbTask = new FetchDBTask(getActivity(), rootView);
        dbTask.execute(SORT_ORDER_POPULARITY);
    }
}
