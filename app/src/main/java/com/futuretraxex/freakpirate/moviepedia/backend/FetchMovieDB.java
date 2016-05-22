package com.futuretraxex.freakpirate.moviepedia.backend;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;
import com.futuretraxex.freakpirate.moviepedia.Models.MovieDataModel;
import com.futuretraxex.freakpirate.moviepedia.data.parsers.BrowseMoviesParser;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by FreakPirate on 2/25/2016.
 */
public class FetchMovieDB extends AsyncTask <Void, Void, MovieDataModel[]> {

    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";

//    private ProgressBar progressBar;

    private Boolean INCLUDE_ADULT;
    private String SORT_ORDER;
    private int PAGE_NUM;
    private Context context;

    public interface AsyncResponse{
        void onProcessFinish(MovieDataModel[] output);
    }

    public AsyncResponse delegate = null;

    public FetchMovieDB(String sortOrder, Boolean safeSearch, int pageNum, Context context, AsyncResponse delegate){
//        this.progressBar = progressBar;
        this.SORT_ORDER = sortOrder;
        this.INCLUDE_ADULT = !safeSearch;
        this.PAGE_NUM = pageNum;
        this.context = context;
        this.delegate = delegate;
    }


    @Override
    protected MovieDataModel[] doInBackground(Void... params) {

        Uri gridURI = URIBuilder.buildGridUri(BASE_URL, SORT_ORDER, INCLUDE_ADULT, PAGE_NUM);

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

        BrowseMoviesParser parser = new BrowseMoviesParser(jsonStr, context);
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
            delegate.onProcessFinish(result);
        }
        else {
            Log.v(GlobalData.LOG_TAG_FETCH_BROWSE_MOVIE_DB, "Empty Response: " + result.toString());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Display progress bar until posters are loaded
//        progressBar.setVisibility(View.VISIBLE);
    }
}
