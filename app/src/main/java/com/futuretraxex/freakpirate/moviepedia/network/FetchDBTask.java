package com.futuretraxex.freakpirate.moviepedia.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.MovieDetails;
import com.futuretraxex.freakpirate.moviepedia.URIBuilder;
import com.futuretraxex.freakpirate.moviepedia.parsers.GridJSONParser;

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
public class FetchDBTask extends AsyncTask <String, Void, String> {

    private final String LOG_TAG = FetchDBTask.class.getSimpleName();
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0){
            return null;
        }

        Uri gridURI = URIBuilder.buildGridUri(BASE_URL, params[0]);

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

        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null){
            GridJSONParser parser = new GridJSONParser(result);
            MovieDetails[] detailsList;

            try {
                detailsList = parser.parse();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "ERROR", e);
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
