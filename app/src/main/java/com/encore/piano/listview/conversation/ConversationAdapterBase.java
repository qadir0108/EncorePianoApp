package com.encore.piano.listview.conversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encore.piano.services.MessageService;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.MessageModel;

import java.util.ArrayList;

import org.json.JSONException;

public abstract class ConversationAdapterBase extends BaseAdapter {

	Context context;
	int viewId;
	
	String UserName;
	
	ArrayList<MessageModel> Messages = new ArrayList<MessageModel>();
	
	public ConversationAdapterBase(Context context, int viewId,String UserName) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException{
		this.context = context;
		this.viewId= viewId;
		this.UserName = UserName;
				
		Messages.clear();
		
		if(ServiceUtility.messageService == null)
			ServiceUtility.messageService = new MessageService(context);
		
		Messages = ServiceUtility.messageService.GetMyConversations(UserName);
		
	}
	
	@Override
	public int getCount() {		
		return Messages.size();
		
	}

	@Override
	public MessageModel getItem(int position) {
		return Messages.get(position);		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder viewHolder;
		if(view == null)			
		{
			view = LayoutInflater.from(context).inflate(viewId, null);
			
			viewHolder = CreateHolder(view);
			view.setTag(viewHolder);			
		}
		else
			viewHolder = (ViewHolder)view.getTag();
		
		viewHolder.Message = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);
		
		return view;
	}
	
	protected abstract ViewHolder CreateHolder(View view);
	protected abstract void BindHolder(ViewHolder holder);

}
