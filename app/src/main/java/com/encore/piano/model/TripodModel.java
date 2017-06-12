package com.encore.piano.model;

public class TripodModel extends BaseModel {

	private String Code;
	private String Description;
	
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
		
	@Override
	public String toString() {
		
		return getCode() + " - " + getDescription();

	}
	
	public enum Tripod
	{
		Code("code"),
		Description("description");
		
		public String Value;
		
		private Tripod(String v)
		{
			Value = v;
		}
	}
	
}
