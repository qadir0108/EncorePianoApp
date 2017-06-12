package com.encore.piano.model;

public class ConfirmationModel extends BaseModel {

	private int Id; 
	private String Condition;
	private boolean Confirmed;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	public boolean isConfirmed() {
		return Confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		Confirmed = confirmed;
	}
	
	
	public enum ConfirmationModelEnum{
		
		Id("ID"),
		Condition("Name"),
		Confirmed("Checked");
		
		public String Value;
		
		private ConfirmationModelEnum(String v){
			Value = v;
		}
		
	}
	
	
}
