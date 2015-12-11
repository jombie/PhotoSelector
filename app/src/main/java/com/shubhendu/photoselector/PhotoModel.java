package com.shubhendu.photoselector;

import java.io.Serializable;

public class PhotoModel implements Serializable {

	private String imgPath;
	private boolean isChecked;

	public PhotoModel(String imgPath) {
        this(imgPath, false);
	}

	public PhotoModel(String imgPath, boolean isChecked) {
		this.imgPath = imgPath;
		this.isChecked = isChecked;
	}

    public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
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
