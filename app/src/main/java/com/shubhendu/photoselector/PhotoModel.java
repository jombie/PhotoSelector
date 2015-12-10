package com.shubhendu.photoselector;

import java.io.Serializable;

public class PhotoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	int imgOrder = -1;
    int imgPosition;
	private String imgPath;
	private boolean isInitialized;

    public PhotoModel(int imgOrder, int imgPosition, String imgPath, boolean isInitialized) {
        this.imgOrder = imgOrder;
        this.imgPosition = imgPosition;
        this.imgPath = imgPath;
        this.isInitialized = isInitialized;
    }

    public PhotoModel(String imgPath, boolean isInitialized) {
		super();
		this.imgPath = imgPath;
		this.isInitialized = isInitialized;
	}

	public PhotoModel(String imgPath) {
		this.imgPath = imgPath;
	}

	public PhotoModel() {
	}

    public int getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(int imgOrder) {
        this.imgOrder = imgOrder;
    }

    public int getImgPosition() {
        return imgPosition;
    }

    public void setImgPosition(int imgPosition) {
        this.imgPosition = imgPosition;
    }

    public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		System.out.println("checked " + isInitialized + " for " + imgPath);
		this.isInitialized = isInitialized;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imgPath == null) ? 0 : imgPath.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PhotoModel)) {
			return false;
		}
		PhotoModel other = (PhotoModel) obj;
		if (imgPath == null) {
			if (other.imgPath != null) {
				return false;
			}
		} else if (!imgPath.equals(other.imgPath)) {
			return false;
		}
		return true;
	}


}
