package com.encore.piano.services;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.encore.piano.business.StaticData;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.TripodModel;
import com.encore.piano.model.TripodModel.Tripod;

public class TripService extends BaseService {

	public ArrayList<TripodModel> TripStatuses = new ArrayList<TripodModel>();
	JSONArray array = null;
	
	public int numberOfImportedTrips = 0;
	
	public TripService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		
		super(context);

		initialize();
	}
	
	@Override
	public URL getServiceUrl() {
		return getURLFromString(ServiceUrls.GetTripUrl(context));
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException {
		
//		for(int i = 0; i < array.length(); i++){
//			TripStatuses.add(DecodeContent(array.getJSONObject(i)));
//		}
		
		TripStatuses = StaticData.GetTripStatuses();
		
		int importResult = -1;//Database.WriteTripStatuses(context, TripStatuses);
		
		if(importResult == -1)
			throw new DatabaseInsertException();
		else
			numberOfImportedTrips = importResult;
		
	}

	
	public ArrayList<TripodModel> GetTripArrayStatuses() {
		return null;//Database.GetArrayTripStatuses(context);
	}

	public ArrayList<TripodModel> GetExceptionStatuses()
	{
		ArrayList<TripodModel> ex = new ArrayList<TripodModel>();

		TripodModel t = new TripodModel();
		t.setCode("MS");
		t.setDescription("Missing Items");
		ex.add(t);

		TripodModel t1 = new TripodModel();
		t1.setCode("DM");
		t1.setDescription("Damaged Items");
		ex.add(t1);

		TripodModel t2 = new TripodModel();
		t2.setCode("IN");
		t2.setDescription("Incorrect Delivery");
		ex.add(t2);

		TripodModel t3 = new TripodModel();
		t3.setCode("NA");
		t3.setDescription("Person not available");
		ex.add(t3);

		return ex;
	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {

		//array = GetJSONArray(getServiceUrl());

	}

	@SuppressWarnings("unchecked")
	@Override
	public TripodModel DecodeContent(JSONObject object) {
		TripodModel model = new TripodModel();
		
		model.setCode(setStringValueFromJSON(Tripod.Code.Value, object));
		model.setDescription(setStringValueFromJSON(Tripod.Description.Value, object));
		
		return model;
	}

	

	

}
