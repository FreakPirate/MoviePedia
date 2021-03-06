package com.futuretraxex.freakpirate.moviepedia.ui.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by FreakPirate on 3/21/2016.
 */
public class DynamicSpanCountCalculator {

    private Context context;
    private int minItemWidth;

    public DynamicSpanCountCalculator(Context context, int minItemWidth){
        this.context = context;
        this.minItemWidth = minItemWidth;
    }

    public int getSpanCount(){
        float screenWidth;

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        int spanCount = (int) screenWidth / minItemWidth;

//        Log.v(FetchMovieDB.class.getSimpleName(), "WindowManager: " + screenWidth);

        if (spanCount <= 2){
            spanCount = 2;
        }else if(spanCount >=3){
            spanCount = 3;
        }else {
            spanCount = 2;
        }

        Log.v(DynamicSpanCountCalculator.class.getSimpleName(), "SpanCount: " + spanCount);

        return spanCount;
    }
}
