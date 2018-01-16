package com.encore.piano.model;

public class LoginModel extends BaseModel {

	private String userName;
	private String password;
    private String authToken;
    private String fcmToken;
	private long time;
	private int userId;
	
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFCMToken() {
		return fcmToken;
	}
	public void setFCMToken(String FCMToken) {
		this.fcmToken = FCMToken;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
