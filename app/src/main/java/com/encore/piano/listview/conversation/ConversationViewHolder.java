package com.encore.piano.listview.conversation;

import android.widget.TextView;

public class ConversationViewHolder extends ViewHolder{

	TextView tvSender;
	TextView tvLastMessageSubject;
	TextView tvMessageCount;
	
	public ConversationViewHolder(TextView tvSender, TextView tvLastMessageSubject, TextView tvMessageCount)
	{
		this.tvSender = tvSender;
		this.tvLastMessageSubject = tvLastMessageSubject;
		this.tvMessageCount = tvMessageCount;
	}
}
