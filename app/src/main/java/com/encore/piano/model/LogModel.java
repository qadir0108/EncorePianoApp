package com.encore.piano.model;

public class LogModel extends BaseModel {
	
	public enum LogModelEnum
	{
		CompanyCode("CompanyCode"),
		CompanyName("CompanyName"),
		UserName("UserName"),
		VehicleCode("FCMToken"),
		Log("Log"),
		;
		
		public String Value;
		
		private LogModelEnum(String v){
			Value = v;
		}
	}
	
	
}
