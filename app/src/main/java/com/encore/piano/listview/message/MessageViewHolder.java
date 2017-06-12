package com.encore.piano.listview.message;


import android.widget.TextView;

public class MessageViewHolder extends ViewHolder{

	TextView subject;
	TextView text;
	TextView time;
	TextView messageId;
	
	
	public MessageViewHolder(TextView subject, TextView text, TextView time, TextView messageId)
	{
		this.subject = subject;
		this.text = text;
		this.time = time;
		this.messageId = messageId;
		
//		MessageModel message = new MessageModel();
//		message.setSubject(subject.getText().toString());
//		message.setMessageText(text.getText().toString());
//		message.setTimestamp(time.getText().toString());
//		message.setID(messageId.getText().toString());
//		this.Message = message; 

	}
}
