package com.futuretraxex.freakpirate.moviepedia.ui.helper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.futuretraxex.freakpirate.moviepedia.data.universal.GlobalData;

/**
 * Created by FreakPirate on 3/19/2016.
 */
public class AutoFitColumnGridLayoutManager extends GridLayoutManager {

    private int minItemWidth;

    public AutoFitColumnGridLayoutManager(Context context, int spanCount, int minItemWidth) {
        super(context, spanCount);
        this.minItemWidth = minItemWidth;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        updateSpanCount();
        super.onLayoutChildren(recycler, state);
    }

    private void updateSpanCount(){
        int spanCount = getWidth() / minItemWidth;
        if (spanCount < 1){
            spanCount = 1;
        }

        this.setSpanCount(spanCount);
    }
}
