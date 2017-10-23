package com.encore.piano.listview.confirmation;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encore.piano.server.Service;
import com.encore.piano.model.ConfirmationModel;

public abstract class ConfirmationAdapterBase extends BaseAdapter {

	Context context;
	int viewId;
	
	public ConfirmationAdapterBase(Context context, int viewId){
		this.context = context;
		this.viewId= viewId; 
	}
	
	@Override
	public int getCount() {
		return Service.confirmationService.Confirmations.size();
		
	}

	@Override
	public ConfirmationModel getItem(int position) {
		return Service.confirmationService.Confirmations.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		return Service.confirmationService.Confirmations.get(position).getId();
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
		
		viewHolder.Confirmation = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);
		
		return view;
	}
	
	protected abstract ViewHolder CreateHolder(View view);
	protected abstract void BindHolder(ViewHolder holder);

}
