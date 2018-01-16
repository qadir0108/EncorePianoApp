package com.encore.piano.sync;


import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel;
import com.encore.piano.server.Service;
import com.encore.piano.util.DateTimeUtility;

import org.json.JSONException;
import org.json.JSONStringer;

public class SyncAssignmentModel extends BaseModel {

    private String Id;
    private String tripStatus;
    private String departureTime;
    private String arrivalTime;

	private String ConsignmentReference;
	private String ColCode;
	private String DeliveryCode;
	private String CustomerReference;
	private String PodStatus;
	private String CustomerUsername;
	private String DateSigned;
	private String[] images;	
	private String SignatureImagePath;
	private boolean Signed;
	private String SignedBy;
	private boolean saved;

	public SyncAssignmentModel fromModel(AssignmentModel model) {
        setAuthToken(Service.loginService.LoginModel.getAuthToken());
        setId(model.getId());
        setTripStatus(model.getTripStatus());
        setDepartureTime(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getDepartureTime())));
        setArrivalTime(DateTimeUtility.formatTimeStampToSend(DateTimeUtility.toDateTime(model.getEstimatedTime())));
        return this;
    }

    public String getJson() throws JSONException {
        JSONStringer stringer = new JSONStringer().object()
                .key(SyncAssignmentEnum.AuthToken.Value).value(getAuthToken())
                .key(SyncAssignmentEnum.Id.Value).value(getId())
                .key(SyncAssignmentEnum.tripStatus.Value).value(getTripStatus())
                .key(SyncAssignmentEnum.departureTime.Value).value(getDepartureTime())
                .key(SyncAssignmentEnum.estimatedTime.Value).value(getArrivalTime())
                .endObject();
        return stringer.toString();
    }

	public String getConsignmentReference() {
		return ConsignmentReference;
	}
	public void setConsignmentReference(String consignmentReference) {
		ConsignmentReference = consignmentReference;
	}
	public String getColCode() {
		return ColCode;
	}
	public void setColCode(String colCode) {
		ColCode = colCode;
	}
	public String getDeliveryCode() {
		return DeliveryCode;
	}
	public void setDeliveryCode(String deliveryCode) {
		DeliveryCode = deliveryCode;
	}
	public String getCustomerReference() {
		return CustomerReference;
	}
	public void setCustomerReference(String customerReference) {
		CustomerReference = customerReference;
	}
	public String getTripStatus() {
		return tripStatus;
	}
	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}
	public String getPodStatus() {
		return PodStatus;
	}
	public void setPodStatus(String podStatus) {
		PodStatus = podStatus;
	}

	public String getCustomerUsername() {
		return CustomerUsername;
	}
	public void setCustomerUsername(String customerUsername) {
		CustomerUsername = customerUsername;
	}
	public String getDateSigned() {
		return DateSigned;
	}
	public void setDateSigned(String dateSigned) {
		DateSigned = dateSigned;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] imagePaths) {
		this.images = imagePaths;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public boolean isSigned() {
		return Signed;
	}
	public void setSigned(boolean signed) {
		Signed = signed;
	}
	public String getSignedBy() {
		return SignedBy;
	}
	public void setSignedBy(String signedBy) {
		SignedBy = signedBy;
	}
	public String getSignatureImagePath() {
		return SignatureImagePath;
	}
	public void setSignatureImagePath(String signatureImagePath) {
		SignatureImagePath = signatureImagePath;
	}
	public void setArrivalTime(String arrivalTime)
	{
		this.arrivalTime = arrivalTime;
	}

	public void setDepartureTime(String deptTime)
	{
		this.departureTime = deptTime;

	}

	public String getArrivalTime()
	{
		return arrivalTime;
	}

	public String getDepartureTime()
	{
		return departureTime;
	}

	public boolean isSaved()
	{
		return saved;
	}

	public void setSaved(boolean saved)
	{
		this.saved = saved;
	}

}
