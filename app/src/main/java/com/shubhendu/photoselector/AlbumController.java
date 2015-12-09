package com.shubhendu.photoselector;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlbumController {

	private ContentResolver resolver;
    private String combinedAlbumName;

	public AlbumController(Context context) {
		resolver = context.getContentResolver();
        combinedAlbumName = context.getResources().getString(R.string.combined_album_name);
	}

	public List<AlbumModel> getAlbums() {

        List<AlbumModel> albums = new ArrayList<AlbumModel>();
		Map<String, AlbumModel> map = new HashMap<String, AlbumModel>();

        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                ImageColumns.DATA
        };

		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

		if (cursor == null || !cursor.moveToNext())
			return new ArrayList<AlbumModel>();

        int bucketColumn = cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME);
        int dataColumn = cursor.getColumnIndex(ImageColumns.DATA);

		cursor.moveToLast();
        String thumbNailPath = cursor.getString(dataColumn);
		AlbumModel combinedAlbum = new AlbumModel(combinedAlbumName, 0, thumbNailPath, true);
		albums.add(combinedAlbum);
		do {
            combinedAlbum.incrementImageCount();
			String albumName = cursor.getString(bucketColumn);
            AlbumModel currentAlbum = map.get(albumName);
            if (null == currentAlbum) {
                AlbumModel album = new AlbumModel(albumName, 1, cursor.getString(dataColumn));
                map.put(albumName, album);
                albums.add(album);
            } else {
                currentAlbum.incrementImageCount();
            }
		} while (cursor.moveToPrevious());
		return albums;
	}

	public List<PhotoModel> getAlbum(String name) {
		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] { ImageColumns.BUCKET_DISPLAY_NAME,
						ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.SIZE }, "bucket_display_name = ?",
				new String[] { name }, ImageColumns.DATE_ADDED);
		if (cursor == null || !cursor.moveToNext())
			return new ArrayList<PhotoModel>();
		List<PhotoModel> photos = new ArrayList<PhotoModel>();
		cursor.moveToLast();
		do {
			if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
				PhotoModel photoModel = new PhotoModel();
				photoModel.setOriginalPath(cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				photos.add(photoModel);
			}
		} while (cursor.moveToPrevious());
		return photos;
	}

	public List<PhotoModel> getCurrent() {
        String[] projections = new String[] {
                ImageColumns.DATA,
                ImageColumns.DATE_ADDED
        };

        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, projections, null, null, ImageColumns.DATE_ADDED);

		if (cursor == null || !cursor.moveToNext())
			return new ArrayList<PhotoModel>();

        List<PhotoModel> photos = new ArrayList<PhotoModel>();
        int dataColumnIndex = cursor.getColumnIndex(ImageColumns.DATA);
		cursor.moveToLast();
		do {
            PhotoModel photoModel = new PhotoModel();
            photoModel.setOriginalPath(cursor.getString(dataColumnIndex));
            photos.add(photoModel);
		} while (cursor.moveToPrevious());
		return photos;
	}
}
