package com.shubhendu.photoselector;

import android.graphics.Color;

public class PhotoSelectorConstants {

    public static int ALBUM_THUMBNAIL_SIZE = -1;
    public static int ALBUM_SELECTOR_COUNT = 10;
    public static int PHOTO_SPAN_COUNT = 3;
    public static final String WHERE_CLAUSE = "bucket_display_name = ? AND bucket_id = ?";
    public static final String PREVIOUS_SELECTED_PATHS = "previous_paths";
    public static int ACTION_BAR_COLOR = Color.parseColor("#3F51B5");
    public static int STATUS_BAR_COLOR = Color.parseColor("#303F9F");
    public final static int ALBUM_REQUEST_CODE = 27;

}
