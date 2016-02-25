package com.futuretraxex.freakpirate.moviepedia;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MoviePoster [] moviePosters = {
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.drawable.ic_poster),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
            new MoviePoster(R.mipmap.ic_interstellar),
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridViewAdapter moviePosterAdapter = new GridViewAdapter(getActivity(), Arrays.asList(moviePosters));

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(moviePosterAdapter);

        return rootView;
    }
}
