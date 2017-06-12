package com.encore.piano.listview.item;


import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encore.piano.services.ItemService;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.PianoModel;

public abstract class ItemAdapterBase extends BaseAdapter {

	Context context;
	int viewId;
	
	ArrayList<PianoModel> Items = new ArrayList<PianoModel>();
	
	public ItemAdapterBase(Context context, int viewId, String consignmentId){
		this.context = context;
		this.viewId= viewId;		
		
		Items.clear();
		if (ServiceUtility.itemService == null)
			try
			{
				ServiceUtility.itemService = new ItemService(context);
			} catch (UrlConnectionException e)
			{
				e.printStackTrace();
			} catch (JSONException e)
			{
				e.printStackTrace();
			} catch (JSONNullableException e)
			{
				e.printStackTrace();
			} catch (NotConnectedException e)
			{
				e.printStackTrace();
			} catch (NetworkStatePermissionException e)
			{
				e.printStackTrace();
			} catch (DatabaseInsertException e)
			{
				e.printStackTrace();
			}

		Items = ServiceUtility.itemService.GetItemsForConsignment(consignmentId);
	}
	
	@Override
	public int getCount() {		
		return Items.size();
		
	}

	@Override
	public PianoModel getItem(int position) {
		return Items.get(position);		
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
		
		viewHolder.Item = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);
		
		return view;
	}
	
	protected abstract ViewHolder CreateHolder(View view);
	protected abstract void BindHolder(ViewHolder holder);

}
