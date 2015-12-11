package com.shubhendu.photoselector;

public class AlbumModel {

    private int albumId;
	private String albumName;
	private int albumTotalImages;
	private String thumbNailPath;
	private boolean isAlbumSelected;

	public AlbumModel(String albumName, int albumId) {
		this.albumName = albumName;
        this.albumId = albumId;
	}

	public AlbumModel(String albumName, int albumId, int albumTotalImages, String thumbNailPath) {
		this.albumName = albumName;
        this.albumId = albumId;
		this.albumTotalImages = albumTotalImages;
		this.thumbNailPath = thumbNailPath;
        this.isAlbumSelected = false;
	}
	
	public String getAlbumName() {
		return albumName;
	}

	public int getAlbumTotalImages() {
		return albumTotalImages;
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

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AlbumModel that = (AlbumModel) o;

		return albumName.equals(that.albumName);

	}

	@Override
	public int hashCode() {
		return albumName.hashCode();
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
