package com.shubhendu.photoselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;


@SuppressLint("HandlerLeak")
public class PhotoSelectorDomain {

	private ImageController imageController;

	public interface PhotoListener {
		void onPhotoLoaded(List<PhotoModel> photos);
	}
	public interface AlbumListener {
		void onAlbumLoaded(List<AlbumModel> albums);
	}

	public PhotoSelectorDomain(Context context) {
		this.imageController = new ImageController(context);
	}

	public void updateAlbum(final AlbumListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onAlbumLoaded((List<AlbumModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<AlbumModel> albums = imageController.getAlbums();
				Message msg = new Message();
				msg.obj = albums;
				handler.sendMessage(msg);
			}
		}).start();
	}

    public void updatePhotos(final PhotoListener listener) {
		updatePhotos(null, -1, listener);
    }

	public void updatePhotos(final String albumName, final int albumId, final PhotoListener listener) {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};

		new Thread(new Runnable() {
			@Override
			public void run() {

				List<PhotoModel> photos = (null == albumName) ?
						imageController.getAllPhotos() :
						imageController.getAlbumPhotos(albumName, albumId);
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

}
