package com.encore.piano.listview.conversation;

import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.encore.piano.R;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;

public class ConversationAdapter extends ConversationAdapterBase {

	TextView tvSender;
	TextView tvLastMessageSubject;
	TextView tvMessageCount;
	
	public ConversationAdapter(Context context, String UserName) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException  {
		super(context, R.layout.conversationlistitem, UserName);
	}

	@Override
	protected ViewHolder CreateHolder(View view) {
		tvSender = (TextView)view.findViewById(R.id.tvSender);
		tvLastMessageSubject = (TextView)view.findViewById(R.id.tvLastMessageSubject);
		tvMessageCount = (TextView)view.findViewById(R.id.tvMessageCount);
		
		ConversationViewHolder holder = new ConversationViewHolder(tvSender, tvLastMessageSubject, tvMessageCount);
		return holder;
	}

	@Override
	protected void BindHolder(ViewHolder holder) {
		
		ConversationViewHolder h = (ConversationViewHolder)holder;	
		
		h.tvSender.setText(h.Message.getSender());
		h.tvLastMessageSubject.setText(h.Message.getSubject());
		h.tvMessageCount.setText(h.Message.getID());	
	}
}
