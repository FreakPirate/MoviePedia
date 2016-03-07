package com.futuretraxex.freakpirate.moviepedia.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieDetails;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrowseMoviesAdapter extends ArrayAdapter<MovieDetails> {
    private static final String LOG_TAG = BrowseMoviesAdapter.class.getSimpleName();
    private Context context;

    public BrowseMoviesAdapter(Activity context, List<MovieDetails> details) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, details);

        this.context = context;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the MoviePoster object from the ArrayAdapter at the appropriate position
        MovieDetails details = getItem(position);
        ImageView view = (ImageView) convertView;

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_browse_movies, parent, false);
        }
        if (view == null) {
            view = (ImageView) convertView.findViewById(R.id.poster_image);
        }

        Picasso.with(context)
                .load(details.getPOSTER_PATH())
//                .placeholder(R.drawable.placeholder_poster)
                .into(view);

        return view;
    }

}