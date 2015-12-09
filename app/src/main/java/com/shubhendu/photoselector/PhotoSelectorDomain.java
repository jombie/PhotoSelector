package com.shubhendu.photoselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;


@SuppressLint("HandlerLeak")
public class PhotoSelectorDomain {

	private AlbumController albumController;
	public interface PhotoListener {
		void onPhotoLoaded(List<PhotoModel> photos);
	}
	public interface AlbumListener {
		void onAlbumLoaded(List<AlbumModel> albums);
	}

	public PhotoSelectorDomain(Context context) {
		albumController = new AlbumController(context);
	}

//	public void getReccent(final PhotoSelectorActivity.OnLocalReccentListener listener) {
//		final Handler handler = new Handler() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void handleMessage(Message msg) {
//				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
//			}
//		};
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				List<PhotoModel> photos = albumController.getCurrent();
//				Message msg = new Message();
//				msg.obj = photos;
//				handler.sendMessage(msg);
//			}
//		}).start();
//	}
//

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
				List<AlbumModel> albums = albumController.getAlbums();
				Message msg = new Message();
				msg.obj = albums;
				handler.sendMessage(msg);
			}
		}).start();
	}

//	public void getAlbum(final String name, final PhotoSelectorActivity.OnLocalReccentListener listener) {
//		final Handler handler = new Handler() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void handleMessage(Message msg) {
//				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
//			}
//		};
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				List<PhotoModel> photos = albumController.getAlbum(name);
//				Message msg = new Message();
//				msg.obj = photos;
//				handler.sendMessage(msg);
//			}
//		}).start();
//	}

}
