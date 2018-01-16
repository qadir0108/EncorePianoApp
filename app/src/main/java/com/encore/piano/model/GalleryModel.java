package com.encore.piano.model;

public class GalleryModel {

	private String Id;
	private String UnitId;
	private String ImagePath;
	private String TakenAt;
	private String TakenLocation;
    private boolean synced;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getTakenAt() {
        return TakenAt;
    }

    public void setTakenAt(String takenAt) {
        TakenAt = takenAt;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getTakenLocation() {
        return TakenLocation;
    }

    public void setTakenLocation(String takenLocation) {
        TakenLocation = takenLocation;
    }
}
