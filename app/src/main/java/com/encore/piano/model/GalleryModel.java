package com.encore.piano.model;

public class GalleryModel {

	private String ImageReference;
	private String ImagePath;
	private String ConsignmentId;
	private String Id;
	
	
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getImageReference() {
		return ImageReference;
	}
	public void setImageReference(String reference) {
		ImageReference = reference;
	}
	public String getImagePath() {
		return ImagePath;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
	public String getConsignmentId() {
		return ConsignmentId;
	}
	public void setConsignmentId(String consignmentId) {
		ConsignmentId = consignmentId;
	}
	
	
}
