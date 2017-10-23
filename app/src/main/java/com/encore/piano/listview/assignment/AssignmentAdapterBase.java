package com.encore.piano.listview.assignment;


import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encore.piano.server.AssignmentService;
import com.encore.piano.server.Service;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.AssignmentModel;

public abstract class AssignmentAdapterBase extends BaseAdapter {

	Context context;
	
	int viewId;
	
	boolean showCompleted;
	
	ArrayList<AssignmentModel> Consignments = new ArrayList<AssignmentModel>();
	
	public AssignmentAdapterBase(Context context, int viewId) {
		this.context = context;
		this.viewId = viewId;
	}

	public AssignmentAdapterBase(Context context, int viewId, boolean showCompleted) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException{
		this.context = context;
		this.viewId= viewId;
		
		this.showCompleted = showCompleted;
		
		Consignments.clear();
		
		if(Service.assignmentService == null)
			Service.assignmentService = new AssignmentService(context);
		
		Consignments = Service.assignmentService.getAll(this.showCompleted);
			
	}
	
	@Override
	public int getCount() {		
		return Consignments.size();
		
	}

	@Override
	public AssignmentModel getItem(int position) {
		return Consignments.get(position);		
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
		
		viewHolder.assignment = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);
		
		return view;
	}
	
	protected abstract ViewHolder CreateHolder(View view);
	protected abstract void BindHolder(ViewHolder holder);

}
