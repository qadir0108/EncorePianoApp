package com.encore.piano.model;

public class FCMModel extends BaseModel {

	private String refreshedToken;
	private boolean registrationResult;
	
	public String getRefreshedToken() {
		return refreshedToken;
	}
	public void setRefreshedToken(String id) {
		refreshedToken = id;
	}
	public boolean getRegistrationResult() {
		return registrationResult;
	}
	public void setRegistrationResult(boolean registrationResult) {
		this.registrationResult = registrationResult;
	}
	
}
