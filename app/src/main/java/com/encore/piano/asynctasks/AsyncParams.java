package com.encore.piano.asynctasks;


import android.content.Context;

public class AsyncParams {

	private String Id;
	private Context context;
	private int notificationId;
	private String result;
	
	public AsyncParams(Context context, String Id, int notificationId,
			 String result) {
		super();
		
		this.context = context;
		this.Id = Id;
		this.notificationId = notificationId;
		this.result = result;
	}
	
	public int getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
	
}
