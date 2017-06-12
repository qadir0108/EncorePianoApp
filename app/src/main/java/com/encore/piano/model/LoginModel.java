package com.encore.piano.model;

public class LoginModel extends BaseModel {

	private String UserName;
	private String Password;
    private String AuthToken;
    private String FCMToken;
	private long Time;
	private int UserId;
	
	public String getAuthToken() {
		return AuthToken;
	}
	public void setAuthToken(String authToken) {
		AuthToken = authToken;
	}
	
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getFCMToken() {
		return FCMToken;
	}
	public void setFCMToken(String FCMToken) {
		this.FCMToken = FCMToken;
	}
	public long getTime() {
		return Time;
	}
	public void setTime(long time) {
		Time = time;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}

}
