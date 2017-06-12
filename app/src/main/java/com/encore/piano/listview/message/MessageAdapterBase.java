package com.encore.piano.listview.message;


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

public abstract class MessageAdapterBase extends BaseAdapter {

	Context context;
	int viewId_my,viewId_his;
	
	int consignmentId;
	ArrayList<MessageModel> Messages = new ArrayList<MessageModel>();
	
	String UserName;
	
	public MessageAdapterBase(Context context, int viewId_my,int viewId_his, String UserName) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException{
		this.context = context;
		this.viewId_my= viewId_my;
		this.viewId_his = viewId_his;
		this.UserName = UserName;
		
		Messages.clear();		
		
		if(ServiceUtility.messageService == null)
			ServiceUtility.messageService = new MessageService(context);
		
		Messages = ServiceUtility.messageService.GetAllMessagesByUser(UserName); //all
		
//		switch(activeMessageFolder)		
//		{
//			case 0:
//				Messages = ServiceUtility.MessageService.GetReceivedMessages(); //inbox
//				break;
//			case 1:
//				Messages = ServiceUtility.MessageService.GetUnsentMessages(); //outbox
//				break;
//			case 2:
//				Messages = ServiceUtility.MessageService.GetSentMessages(); //sent
//				break;
//			default:
//				break;
//		}
		
		
		
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
//		if(view == null)			
//		{
			MessageModel message = getItem(position);
			
			if(message.getSender().equals(UserName))
				view = LayoutInflater.from(context).inflate(viewId_his, null);
			else if(message.getRecipient().equals(UserName))
				view = LayoutInflater.from(context).inflate(viewId_my, null);
			
			viewHolder = CreateHolder(view);
			view.setTag(viewHolder);			
//		}
//		else
//			viewHolder = (ViewHolder)view.getTag();
		
		viewHolder.Message = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);
		
		return view;
	}
	
	protected abstract ViewHolder CreateHolder(View view);
	protected abstract void BindHolder(ViewHolder holder);

}
