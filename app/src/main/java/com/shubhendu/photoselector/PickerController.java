package com.shubhendu.photoselector;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import java.util.List;

public class PickerController {

    private RecyclerView recyclerView;
    private RecyclerView.OnItemTouchListener OnItemTouchListener;
    ActionBar actionBar;
    String prefixSubTitle;

    PickerController(Context context, ActionBar actionBar, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.actionBar = actionBar;
        this.prefixSubTitle = context.getResources().getString(R.string.selected);

        OnItemTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
    }

    public void setRecyclerViewClickable(final boolean isClickable) {
        if (isClickable)
            recyclerView.removeOnItemTouchListener(OnItemTouchListener);
        else {
            recyclerView.addOnItemTouchListener(OnItemTouchListener);
        }

    }

    public void setActionbarTitle(int total) {
        if (PhotoSelectorConstants.ALBUM_SELECTOR_COUNT == 1)
            actionBar.setSubtitle(null);
        else
            actionBar.setSubtitle(prefixSubTitle +"(" + String.valueOf(total) + "/" + PhotoSelectorConstants.ALBUM_SELECTOR_COUNT + ")");
    }
}