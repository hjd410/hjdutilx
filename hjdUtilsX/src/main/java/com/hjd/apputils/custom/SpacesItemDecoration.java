package com.hjd.apputils.custom;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by HJD on 2019/3/11/0011 and 16:37.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 * Live beautifully,dream passionately,love completely.
 * /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }
    }
}
