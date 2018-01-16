package com.encore.piano.sync;


import com.encore.piano.model.BaseModel;
import com.encore.piano.model.GalleryModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.ImageUtility;

import org.json.JSONException;
import org.json.JSONStringer;

public class SyncImageModel extends BaseModel {

    private String Id;
    private String assignmentId;
    private String unitId;
    private String image;
    private String takenAt;
	private String takeLocation;

	public SyncImageModel fromModel(GalleryModel model) {
		setAuthToken(Service.loginService.LoginModel.getAuthToken());
		setId(model.getId());
		setUnitId(model.getUnitId());
        setImage(ImageUtility.encodeJpegToBase64(model.getImagePath()));
        setTakenAt(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getTakenAt())));
		setTakeLocation(model.getTakenLocation());
        return this;
	}

	public String getJson() throws JSONException {
		JSONStringer stringer = new JSONStringer().object()
				.key(SyncImageEnum.AuthToken.Value).value(getAuthToken())
                .key(SyncImageEnum.Id.Value).value(getId())
                .key(SyncImageEnum.assignmentId.Value).value(getAssignmentId())
                .key(SyncImageEnum.unitId.Value).value(getUnitId())
                .key(SyncImageEnum.image.Value).value(getImage())
                .key(SyncImageEnum.takenAt.Value).value(getTakenAt())
                .key(SyncImageEnum.takeLocation.Value).value(getTakeLocation())
				.endObject();
		return stringer.toString();
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

    public String getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(String takenAt) {
        this.takenAt = takenAt;
    }

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getTakeLocation() {
		return takeLocation;
	}

	public void setTakeLocation(String takeLocation) {
		this.takeLocation = takeLocation;
	}
}
