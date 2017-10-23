package com.encore.piano.listview.unit;


import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.encore.piano.model.UnitModel;
import com.encore.piano.server.UnitService;
import com.encore.piano.server.Service;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;

public abstract class UnitAdapterBase extends BaseAdapter {

	Context context;
	int viewId;
	
	ArrayList<UnitModel> models = new ArrayList<UnitModel>();
	
	public UnitAdapterBase(Context context, int viewId, String consignmentId){
		this.context = context;
		this.viewId= viewId;		

        try
        {
            models.clear();
            if (Service.unitService == null)
                Service.unitService = new UnitService(context);
            models = Service.unitService.getUnitsByAssignmentId(consignmentId);
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
	}
	
	@Override
	public int getCount() {		
		return models.size();
	}

	@Override
	public UnitModel getItem(int position) {
		return models.get(position);
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
		
		viewHolder.unit = getItem(position);
		viewHolder.position = position;
		BindHolder(viewHolder);
		
		return view;
	}
	
	protected abstract ViewHolder CreateHolder(View view);
	protected abstract void BindHolder(ViewHolder holder);

}
