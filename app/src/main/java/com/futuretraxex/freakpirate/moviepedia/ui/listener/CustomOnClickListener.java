package com.futuretraxex.freakpirate.moviepedia.ui.listener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.futuretraxex.freakpirate.moviepedia.Models.VideosModel;
import com.futuretraxex.freakpirate.moviepedia.backend.URIBuilder;

/**
 * Created by FreakPirate on 3/24/2016.
 */
public class CustomOnClickListener implements View.OnClickListener {

    Activity context;
    VideosModel trailerModel;
    int viewId;

    public CustomOnClickListener(Activity context, VideosModel model, int viewId){
        this.context = context;
        this.trailerModel = model;
        this.viewId = viewId;
    }

    @Override
    public void onClick(View v) {

        String baseTrailerUrl = "http://www.youtube.com/watch";
        Log.v(CustomOnClickListener.class.getSimpleName(),
                "ViewID:" + v.getId());
        String trailerId = trailerModel.getResults().get(viewId).getKey();

        Uri trailerUri = URIBuilder.buildTrailerUri(baseTrailerUrl, trailerId);

        Log.v(CustomOnClickListener.class.getSimpleName(),
                "TrailerID: " + trailerId + "\nTrailerURL: " + trailerUri.toString());

//        Intent intent = new Intent(context, MovieDetailActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setData(trailerUri);
//
//        if (intent.resolveActivity(context.getPackageManager()) != null){
//            context.startActivity(intent);
//        }else {
//            Log.e(MovieDetailFragment.class.getSimpleName(), "Error: can't resolve activity");
//        }

        context.startActivity(new Intent(Intent.ACTION_VIEW, trailerUri));
    }
}
