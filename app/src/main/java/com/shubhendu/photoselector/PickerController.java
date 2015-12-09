package com.shubhendu.photoselector;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

public class PickerController {

    public static int ALBUM_THUMNAIL_SIZE = -1;
    public static int ALBUM_PICKER_COUNT = 10;
    public static int PHOTO_SPAN_COUNT = 3;

    private RecyclerView recyclerView;
    private RecyclerView.OnItemTouchListener OnItemTouchListener;
    ActionBar actionBar;
    String bucketTitle;

    PickerController(ActionBar actionBar, RecyclerView recyclerView, String bucketTitle) {
        this.recyclerView = recyclerView;
        this.actionBar = actionBar;
        this.bucketTitle = bucketTitle;

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

    /**
     * @param isAble true == can clickable
     */
    public void setRecyclerViewClickable(final boolean isAble) {
        if (isAble)
            recyclerView.removeOnItemTouchListener(OnItemTouchListener);
        else {
            recyclerView.addOnItemTouchListener(OnItemTouchListener);
        }

    }

    public void setActionbarTitle(int total) {
        if (ALBUM_PICKER_COUNT == 1)
            actionBar.setTitle(bucketTitle);
        else
            actionBar.setTitle(bucketTitle + "(" + String.valueOf(total) + "/" + ALBUM_PICKER_COUNT + ")");
    }
}