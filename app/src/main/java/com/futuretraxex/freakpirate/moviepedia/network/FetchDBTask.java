package com.futuretraxex.freakpirate.moviepedia.network;

import android.os.AsyncTask;
import android.util.Log;

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

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0){
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader  reader = null;

        String tmdbJsonStr = null;

        try {
            URIBuilder uriBuilder = new URIBuilder(params[0]);
            URL tmdbURL = new URL(uriBuilder.build().toString());

            urlConnection = (HttpURLConnection) tmdbURL.openConnection();
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

            tmdbJsonStr = buffer.toString();
            Log.v(LOG_TAG, "JSON String: " + tmdbJsonStr);

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

        return tmdbJsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        int index = 20;

        if (result != null){
            try {
                String[] movieDB = GridJSONParser.parse(result);

                String[] titleList = new String[index];
                String[] posterList = new String[index];
                String[] idList = new String[index];

                for (int i=0; i<index; i++){
                    String movieDetail = movieDB[i];
                    String[] splitParts = movieDetail.split("~");

                    titleList[i] = splitParts[0];
                    posterList[i] = splitParts[1];
                    idList[i] = splitParts[2];
                }

                posterList = imageURLCreator(posterList, index, "w185");

                for (int i=0; i<index; i++){
                    Log.v(LOG_TAG, titleList[i]);
                }

            }catch (JSONException e){
                Log.e(LOG_TAG, "ERROR", e);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private String[] imageURLCreator(String[] posterPathList, int index, String size){

        String BASE_URL = "http://image.tmdb.org/t/p/";

        for (int i=0; i<index; i++){
            posterPathList[i] = BASE_URL + size + '/' + posterPathList[i];
        }

        return posterPathList;
    }
}
