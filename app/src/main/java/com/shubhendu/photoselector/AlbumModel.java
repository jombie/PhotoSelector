package com.shubhendu.photoselector;

public class AlbumModel {

	private String albumName;
	private int albumTotalImages;
	private String thumbNailPath;
	private boolean isAlbumSelected;

	public AlbumModel() {
		super();
	}
	
	public AlbumModel(String albumName) {
		this.albumName = albumName;
	}

	public AlbumModel(String albumName, int albumTotalImages, String recentPhotoPath) {
		super();
		this.albumName = albumName;
		this.albumTotalImages = albumTotalImages;
		this.thumbNailPath = recentPhotoPath;
        this.isAlbumSelected = false;
	}
	
	public AlbumModel(String albumName, int albumTotalImages, String recentPhotoPath, boolean isCheck) {
		super();
		this.albumName = albumName;
		this.albumTotalImages = albumTotalImages;
		this.thumbNailPath = recentPhotoPath;
		this.isAlbumSelected = isCheck;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public int getAlbumTotalImages() {
		return albumTotalImages;
	}

	public void setAlbumTotalImages(int albumTotalImages) {
		this.albumTotalImages = albumTotalImages;
	}

	public String getThumbNailPath() {
		return thumbNailPath;
	}

	public void setThumbNailPath(String thumbNailPath) {
		this.thumbNailPath = thumbNailPath;
	}

	public boolean isAlbumSelected() {
		return isAlbumSelected;
	}

	public void setAlbumSelected(boolean isCheck) {
		this.isAlbumSelected = isCheck;
	}

	public void incrementImageCount() {
		albumTotalImages++;
	}

	@Override
	public String toString() {
		return "AlbumModel{" +
				"albumName='" + albumName + '\'' +
				", albumTotalImages=" + albumTotalImages +
				", thumbNailPath='" + thumbNailPath + '\'' +
				", isAlbumSelected=" + isAlbumSelected +
				'}';
	}
}
