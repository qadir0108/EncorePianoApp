package com.encore.piano.model;

public class GpxTrackModel extends BaseModel{

	private String Name;
	private String ConsignmentId;
	private double Longitude;
	private double Latitude;
	private int Order;
	
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getConsignmentId() {
		return ConsignmentId;
	}
	public void setConsignmentId(String consignmentId) {
		ConsignmentId = consignmentId;
	}	
	public int getOrder() {
		return Order;
	}
	public void setOrder(int order) {
		Order = order;
	}


	public enum GpxEnum
	{
		Route("Route"),
		Lat("Lat"),
		Lon("Lon"),
		Name("Size"),
		Order("Order");
		
		public String Value;
		
		private GpxEnum(String v){
			Value = v;
		}
	}
	
}
