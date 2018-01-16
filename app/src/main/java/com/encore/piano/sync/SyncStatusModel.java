package com.encore.piano.sync;


import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.DateTimeUtility;

import org.json.JSONException;
import org.json.JSONStringer;

public class SyncStatusModel extends BaseModel {

    private String Id;
    private String tripStatus;
    private String departureTime;
    private String arrivalTime;
    private String statusTime;

	public SyncStatusModel fromModelForStart(AssignmentModel model) {
        setAuthToken(Service.loginService.LoginModel.getAuthToken());
        setId(model.getId());
        setTripStatus(model.getTripStatus());
        setDepartureTime(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getDepartureTime())));
        setArrivalTime(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getEstimatedTime())));
        return this;
    }

    public String getJsonForStart() throws JSONException {
        JSONStringer stringer = new JSONStringer().object()
                .key(SyncStatusEnum.AuthToken.Value).value(getAuthToken())
                .key(SyncStatusEnum.Id.Value).value(getId())
                .key(SyncStatusEnum.tripStatus.Value).value(getTripStatus())
                .key(SyncStatusEnum.departureTime.Value).value(getDepartureTime())
                .key(SyncStatusEnum.estimatedTime.Value).value(getArrivalTime())
                .endObject();
        return stringer.toString();
    }

	public SyncStatusModel fromModelForStatus(AssignmentModel model) {
		setAuthToken(Service.loginService.LoginModel.getAuthToken());
		setId(model.getId());
		setTripStatus(model.getTripStatus());
		setStatusTime(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getCompletionTime())));
		return this;
	}

	public String getJsonForStatus() throws JSONException {
		JSONStringer stringer = new JSONStringer().object()
				.key(SyncStatusEnum.AuthToken.Value).value(getAuthToken())
				.key(SyncStatusEnum.Id.Value).value(getId())
				.key(SyncStatusEnum.tripStatus.Value).value(getTripStatus())
				.key(SyncStatusEnum.statusTime.Value).value(getStatusTime())
				.endObject();
		return stringer.toString();
	}


	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}
}
