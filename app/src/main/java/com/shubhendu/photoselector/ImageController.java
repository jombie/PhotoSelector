package com.shubhendu.photoselector;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImageController {

    private Context context;
	private ContentResolver resolver;
    private String combinedAlbumName;

	public ImageController(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
        combinedAlbumName = context.getResources().getString(R.string.combined_album_name);
	}

    public void queryAll(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, BaseColumns._ID);
        String[] columnNames = cursor.getColumnNames();
        int size = columnNames.length;
        int[] columnIndexes = new int[size];
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            if(i > 0) sb.append(" | ");
            sb.append(columnNames[i]);
            columnIndexes[i] = cursor.getColumnIndex(columnNames[i]);
        }
        Log.d("DBDB","Uri: " + uri.toString() + " Count: " + cursor.getCount());
        Log.d("DBDB", sb.toString());
        while(cursor.moveToNext()) {
            sb.setLength(0);
            for(int i = 0; i < size; i++) {
                if(i > 0) sb.append(" | ");
                sb.append(cursor.getString(columnIndexes[i]));
            }
            Log.d("DBDB", sb.toString());
        }
    }

	public List<AlbumModel> getAlbums() {
        List<AlbumModel> albums = new ArrayList<AlbumModel>();
		Map<String, AlbumModel> map = new HashMap<String, AlbumModel>();

        String[] projection = new String[]{
                ImageColumns.DATA,
                ImageColumns.BUCKET_ID,
                ImageColumns.BUCKET_DISPLAY_NAME,
        };

        /** Query Images table to get all images **/
		Cursor cursor = null ;

        try {
            cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, projection, null, null, ImageColumns.BUCKET_ID);

            if (cursor == null || !cursor.moveToNext())
                return new ArrayList<AlbumModel>();

            int dataColumnIndex = cursor.getColumnIndex(ImageColumns.DATA);
            int bucketIdColumnIndex = cursor.getColumnIndex(ImageColumns.BUCKET_ID);
            int bucketColumnIndex = cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME);

            AlbumModel combinedAlbum = new AlbumModel(combinedAlbumName, cursor.getCount());
            combinedAlbum.setAlbumSelected(true);
            albums.add(combinedAlbum);

            int previousBucketId = -1;
            AlbumModel previousAlbum = null;
            while(cursor.moveToNext()) {
                int bucketId = cursor.getInt(bucketIdColumnIndex);
                if(previousBucketId == bucketId) {
                    previousAlbum.incrementImageCount();
                } else {
                    String thumbnailPath = cursor.getString(dataColumnIndex);
                    String bucketName = cursor.getString(bucketColumnIndex);
                    previousAlbum = new AlbumModel(bucketName, 1, thumbnailPath);
                    previousBucketId = bucketId;
                    albums.add(previousAlbum);
                }
            }
        } finally {
            if(null != cursor) cursor.close();
        }
        /** Set thumb for all album as that of first album **/
        if(albums.size() > 1)
            albums.get(0).setThumbNailPath(albums.get(1).getThumbNailPath());
        return albums;
	}

    public List<String> getAlbumPhotos(String name) {

		String[] projection = new String[] {
				ImageColumns.BUCKET_DISPLAY_NAME,
				ImageColumns.DATA,
				ImageColumns.DATE_ADDED
		};

		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI,
				projection, PhotoSelectorConstants.WHERE_CLAUSE,
				new String[] { name }, ImageColumns.DATE_ADDED);

		return getPhotos(cursor);
	}

	public List<String> getAllPhotos() {
        String[] projections = new String[] {
                ImageColumns.DATA,
                ImageColumns.DATE_ADDED
        };

        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI,
                projections, null, null, ImageColumns.DATE_ADDED);

		return getPhotos(cursor);
	}

	private List<String> getPhotos(Cursor cursor) {
		if (cursor == null || !cursor.moveToNext())
			return new ArrayList<String>();

		List<String> photos = new ArrayList<String>();
		int dataColumnIndex = cursor.getColumnIndex(ImageColumns.DATA);
		cursor.moveToLast();
		do {
			photos.add(cursor.getString(dataColumnIndex));
		} while (cursor.moveToPrevious());
		return photos;
	}
}
