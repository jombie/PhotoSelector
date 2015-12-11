package com.shubhendu.photoselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12/11/15.
 */
public class PhotoSelector {

    private ArrayList<String> selectedPaths = new ArrayList<>();
    private Context context;

    private PhotoSelector(Context context) {
        this.context = context;
    }

    public static PhotoSelector with(Context context) {
        return new PhotoSelector(context);
    }

    public PhotoSelector setPreviousSelectedPaths(ArrayList<String> selectedPaths) {
        this.selectedPaths = selectedPaths;
        return this;
    }

    public PhotoSelector setAlbumThumbnailSize(int size) {
        PhotoSelectorConstants.ALBUM_THUMBNAIL_SIZE = size;
        return this;
    }

    public PhotoSelector setMaxSelectImages(int count) {
        if (count <= 0) count = 1;
        PhotoSelectorConstants.ALBUM_SELECTOR_COUNT = count;
        return this;
    }

    public PhotoSelector setActionBarColor(int actionbarColor) {
        PhotoSelectorConstants.ACTION_BAR_COLOR = actionbarColor;
        return this;
    }

    public PhotoSelector setActionBarColor(int actionbarColor, int statusBarColor) {
        PhotoSelectorConstants.ACTION_BAR_COLOR = actionbarColor;
        PhotoSelectorConstants.STATUS_BAR_COLOR = statusBarColor;
        return this;
    }

    public void startAlbum() {
        if (PhotoSelectorConstants.ALBUM_THUMBNAIL_SIZE == -1)
            PhotoSelectorConstants.ALBUM_THUMBNAIL_SIZE = (int) context.getResources().getDimension(R.dimen.album_item_height);

        Intent i = new Intent(context, PhotoSelectorActivity.class);
        i.putStringArrayListExtra(PhotoSelectorConstants.PREVIOUS_SELECTED_PATHS, selectedPaths);
        ((Activity) context).startActivityForResult(i, PhotoSelectorConstants.ALBUM_REQUEST_CODE);
    }

}
