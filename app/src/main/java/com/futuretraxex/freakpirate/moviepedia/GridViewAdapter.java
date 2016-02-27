package com.futuretraxex.freakpirate.moviepedia;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<MoviePoster> {
    private static final String LOG_TAG = GridViewAdapter.class.getSimpleName();

    /**
     * @param context        The current context. Used to inflate the layout file.
     * @param MoviePosters A List of MoviePoster objects to display in a list
     */
    public GridViewAdapter(Activity context, List<MoviePoster> MoviePosters) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, MoviePosters);
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
        MoviePoster MoviePoster = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.poster_image);
        iconView.setImageResource(MoviePoster.image);

        return convertView;
    }
}