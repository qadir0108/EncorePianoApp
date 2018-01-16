package com.encore.piano.model;
import android.app.ProgressDialog;

public class ProgressModel {

	private ProgressDialog dialog;
	private String taskName;
	private int notificationId;
	private int itemsCount;
	private int step;
	private String message;
	private String title;
	
	public int getItemsCount() {
		return itemsCount;
	}
	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public ProgressDialog getDialog() {
		return dialog;
	}
	public void setDialog(ProgressDialog dialog) {
		this.dialog = dialog;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	
}
