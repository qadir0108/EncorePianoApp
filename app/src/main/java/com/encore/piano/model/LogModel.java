package com.encore.piano.model;

public class LogModel extends BaseModel {
	
	public enum LogModelEnum
	{
		log("log"),
		;
		
		public String Value;
		
		private LogModelEnum(String v){
			Value = v;
		}
	}
	
	
}
