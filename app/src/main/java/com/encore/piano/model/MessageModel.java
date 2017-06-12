package com.encore.piano.model;

import java.io.Serializable;

public class MessageModel extends BaseModel implements Serializable {

	private String ID;
	private String Timestamp;
	private String Subject;
	private String MessageText;
	private String Sender;
	private String Recipient;	
	private String Folder;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getMessageText() {
		return MessageText;
	}

	public void setMessageText(String messageText) {
		MessageText = messageText;
	}

	public String getSender() {
		return Sender;
	}

	public void setSender(String sender) {
		Sender = sender;
	}

	public String getRecipient() {
		return Recipient;
	}

	public void setRecipient(String recipient) {
		Recipient = recipient;
	}

	public String getFolder() {
		return Folder;
	}

	public void setFolder(String folder) {
		Folder = folder;
	}



	public enum MessageEnum{
		ID("ID"),
		Timestamp("TimeStamp"),
		Subject("Subject"),
		MessageText("MessageText"),
		Sender("Sender"),
		Recipient("Recepient");
		
		public String Value;
		
		private MessageEnum(String v){
			Value = v;
		}
		
	}
	
	public enum MessageFolderEnum
	{
		Inbox("Inbox"),
		Outbox("Outbox"),
		Sent("Sent");
		
		public String Value;
		
		private MessageFolderEnum(String v){
			Value = v;
		}
	}
	
	
}
