package com.encore.piano.listview.message;

import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.encore.piano.R;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;

public class MessageAdapter extends MessageAdapterBase {

	TextView Title;
	TextView Text;
	TextView Time;
	TextView MessageId;
	
	public MessageAdapter(Context context, String UserName) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException  {
		super(context, R.layout.messagelistitem_my,R.layout.messagelistitem_his , UserName);
	}

	@Override
	protected ViewHolder CreateHolder(View view) {
		Title = (TextView)view.findViewById(R.id.tvTitle);
		Text = (TextView)view.findViewById(R.id.tvText);
		Time = (TextView)view.findViewById(R.id.tvTime);
		MessageId = (TextView)view.findViewById(R.id.messageId);
		
		MessageViewHolder holder = new MessageViewHolder(Title, Text, Time, MessageId);
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		MessageViewHolder h = (MessageViewHolder)holder;	
		
		h.subject.setText(h.Message.getSubject());
		h.text.setText(h.Message.getMessageText());
		h.time.setText(h.Message.getTimestamp());	
		h.messageId.setText(String.valueOf(h.Message.getID()));
	}
}
