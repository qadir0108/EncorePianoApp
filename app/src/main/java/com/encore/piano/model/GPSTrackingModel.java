package com.encore.piano.model;

public class GPSTrackingModel extends BaseModel {

	
	private String Latitude;
	private String Longitude;
	private String Timestamp;
	private boolean Synced;
	private int Id;
	
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
	public boolean getSynced() {
		return Synced;
	}
	public void setSynced(int synced) {
		Synced = synced == 0 ? false : true;		
	}	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}


	public enum GPSTrackingModelEnum
	{
		Latitude("Latitude"),
		Longitude("Longitude"),
		Timestamp("Timestamp");
		
		public String Value;
		
		private GPSTrackingModelEnum(String v){
			Value = v;
		}
	}
	
}
