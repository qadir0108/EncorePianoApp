package com.encore.piano.model;


public class DataSyncModel extends BaseModel {
	
	private String ConsignmentReference;
	private String ColCode;
	private String DeliveryCode;
	private String CustomerReference;
	private String TripStatus;
	private String PodStatus;	
	private String AuthToken;
	private String CustomerUsername;
	private String DateSigned;
	private String[] images;	
	private String SignatureImagePath;
	private String ConsignmentId;
	private boolean Signed;
	private String SignedBy;
	private String arrivalTime;
	private String departureTime;
	private boolean saved;
	
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
		return TripStatus;
	}
	public void setTripStatus(String tripStatus) {
		TripStatus = tripStatus;
	}
	public String getPodStatus() {
		return PodStatus;
	}
	public void setPodStatus(String podStatus) {
		PodStatus = podStatus;
	}
	public String getAuthToken() {
		return AuthToken;
	}
	public void setAuthToken(String authToken) {
		AuthToken = authToken;
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
	public String getConsignmentId() {
		return ConsignmentId;
	}
	public void setConsignmentId(String consignmentId) {
		ConsignmentId = consignmentId;
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

	public enum DataSyncModelEnum
	{
		ConsignmentReference("ConsignmentReference"),
		ColCode("ColCode"),
		DeliveryCode("DeliveryCode"),
		CustomerReference("CustomerReference"),
		TripStatus("TripStatus"),
		PodStatus("PodStatus"),
		AuthToken("AuthToken"),
		CustomerUsername("CustomerUsername"),
		DateSigned("DateSigned"),
		Images("Images"),
		ConsignmentID("ConsignmentID"),
		Signed("Signed"),
		SignedBy("SignedBy"),
		SignatureImage("SignatureImage"),
		ArrivalTime("ArrivalTime"),
		DepartureTime("DepartureTime"),
		RunSheetID("RunSheetID"),
		MessageID("MessageID"),
		Acknowledged("Acknowledged"),
		Saved("Saved")
		;
		
		public String Value;
		
		private DataSyncModelEnum(String v){
			Value = v;
		}
	}
	
	
	public enum DataSyncImageModelEnum
	{		
		ConsignmentID("ConsignmentID"),
		ImageId("ID"),
		AuthToken("AuthToken");
		
		public String Value;
		
		private DataSyncImageModelEnum(String v){
			Value = v;
		}
	}
	
	
}
