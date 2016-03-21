package com.futuretraxex.freakpirate.moviepedia.backend;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.futuretraxex.freakpirate.moviepedia.ui.adapter.BrowseMoviesAdapter;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.DynamicSpanCountCalculator;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.GridSpacingItemDecoration;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.R;
import com.futuretraxex.freakpirate.moviepedia.data.parsers.BrowseMoviesParser;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class FetchMovieDB extends AsyncTask <String, Void, MovieDataModel[]> {

    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    private ProgressBar progressBar;

    private View rootView;
    private Activity context;

    private Boolean INCLUDE_ADULT;
    private String SORT_ORDER;

    public FetchMovieDB(Activity context, View rootView, Boolean safeSearch){
        this.context = context;
        this.rootView = rootView;

        this.INCLUDE_ADULT = !safeSearch;
    }

    @Override
    protected MovieDataModel[] doInBackground(String... params) {

        if (params.length == 0){
            return null;
        }

        this.SORT_ORDER = params[0];

        Uri gridURI = URIBuilder.buildGridUri(BASE_URL, SORT_ORDER, INCLUDE_ADULT);

        HttpURLConnection urlConnection = null;
        BufferedReader  reader = null;

        String jsonStr = null;

        try {
            URL url = new URL(gridURI.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null){
                //Nothing to do
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null){
                //Since it's a JSON, adding a newline isn't necessary
                //But it does make debugging a lot easier if you print out
                //the completed buffer for debugging

                buffer.append(line + '\n');
            }

            if(buffer.length() == 0){
                //Stream was empty.
                //No point in parsing.
                return null;
            }

            jsonStr = buffer.toString();
//            Log.v(GlobalData.LOG_TAG_FETCH_BROWSE_MOVIE_DB, "JSON String: " + jsonStr);

        }catch (MalformedURLException e){
            Log.e(GlobalData.LOG_TAG_FETCH_BROWSE_MOVIE_DB, "ERROR", e);
            return null;
        }catch (IOException e){
            Log.e(GlobalData.LOG_TAG_FETCH_BROWSE_MOVIE_DB, "ERROR", e);
            return null;

        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                }catch (final IOException e){
                     Log.e(GlobalData.LOG_TAG_FETCH_BROWSE_MOVIE_DB, "Error closing stream", e);
                }
            }
        }

        BrowseMoviesParser parser = new BrowseMoviesParser(jsonStr);
        MovieDataModel[] detailsList = null;

        try {
            detailsList = parser.parse();
        } catch (JSONException e) {
            Log.e(GlobalData.LOG_TAG_FETCH_BROWSE_MOVIE_DB, "ERROR", e);
            e.printStackTrace();
        }

        return detailsList;
    }

    @Override
    protected void onPostExecute(MovieDataModel[] result) {
        if (result != null){

            int spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.grid_item_spacing);
            boolean includeEdge = true;

            int minItemWidth = context.getResources().getDimensionPixelSize(R.dimen.min_column_width);

            DynamicSpanCountCalculator dscc = new DynamicSpanCountCalculator(context, minItemWidth);
            int spanCount = dscc.getSpanCount();

            //Hiding progress bar
            progressBar.setVisibility(View.GONE);

            // Look up the recycler view
            RecyclerView rvMovieData = (RecyclerView) rootView.findViewById(R.id.recycler_view_browse_movies);

            BrowseMoviesAdapter viewAdapter = new BrowseMoviesAdapter(Arrays.asList(result), context);
            rvMovieData.setAdapter(viewAdapter);

            GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
            rvMovieData.setLayoutManager(layoutManager);

            rvMovieData.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, includeEdge));

//            rvMovieData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    MovieDataModel details = (MovieDataModel) parent.getItemAtPosition(position);
//
//                    Intent intent = new Intent(context, MovieDetailActivity.class);
//                    intent.putExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING, details);
//                    context.startActivity(intent);
//                }
//
//            });
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Setting progress bar until posters get visible
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }
}
