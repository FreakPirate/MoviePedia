package com.futuretraxex.freakpirate.moviepedia.backend;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.futuretraxex.freakpirate.moviepedia.ui.adapter.BrowseMoviesAdapter;
import com.futuretraxex.freakpirate.moviepedia.ui.activity.DetailActivity;
import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.ui.helper.MovieDetails;
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
public class FetchDBTask extends AsyncTask <String, Void, MovieDetails[]> {

    private final String LOG_TAG = FetchDBTask.class.getSimpleName();
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    private ProgressBar progressBar;

    private View rootView;
    private Activity context;

    private Boolean INCLUDE_ADULT;
    private String SORT_ORDER;

    public FetchDBTask(Activity context, View rootView, Boolean safeSearch){
        this.context = context;
        this.rootView = rootView;

        this.INCLUDE_ADULT = !safeSearch;
    }

    @Override
    protected MovieDetails[] doInBackground(String... params) {

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
            Log.v(LOG_TAG, "JSON String: " + jsonStr);

        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "ERROR", e);
            return null;
        }catch (IOException e){
            Log.e(LOG_TAG, "ERROR", e);
            return null;

        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                }catch (final IOException e){
                     Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        BrowseMoviesParser parser = new BrowseMoviesParser(jsonStr);
        MovieDetails[] detailsList = null;

        try {
            detailsList = parser.parse();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "ERROR", e);
            e.printStackTrace();
        }

        return detailsList;
    }

    @Override
    protected void onPostExecute(MovieDetails[] result) {
        if (result != null){

            //Hiding progress bar
            progressBar.setVisibility(View.GONE);

            BrowseMoviesAdapter viewAdapter = new BrowseMoviesAdapter(context, Arrays.asList(result));
            GridView gridView = (GridView) context.findViewById(R.id.movies_grid);
            gridView.setAdapter(viewAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieDetails details = (MovieDetails) parent.getItemAtPosition(position);

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(GlobalData.DETAIL_ACTIVITY_INTENT_STRING, details);
                    context.startActivity(intent);
                }

            });
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
