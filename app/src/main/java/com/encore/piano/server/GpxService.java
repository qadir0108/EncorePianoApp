package com.encore.piano.server;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.encore.piano.db.GpsDb;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.BaseModel.ServerResponse;
import com.encore.piano.model.GpxTrackModel;
import com.encore.piano.model.GpxTrackModel.GpxEnum;

public class GpxService extends BaseService {

	public ArrayList<GpxTrackModel> GpxTracks = new ArrayList<GpxTrackModel>();

	JSONArray array = new JSONArray();
	public GpxService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		super(context);

		//initialize();
	}
	
	public void Initialize() throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException
	{
		initialize();
	}

	@Override
	public URL getServiceUrl() {
		String url = ServiceUrls.GetGpxUrl(context)
				.replace("authtokenvalue", Service.loginService.LoginModel.getAuthToken())
				.replace("runsheetidvalue", Service.assignmentService.RunSheetID);
		return getURLFromString(url);
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException {
		
		for(int i = 0; i < array.length(); i++)
		{
			GpxTracks.add(decodeContent(array.getJSONObject(i)));
		}
		
		if(GpxTracks.size() > 0 && GpsDb.countGpxTracks(context, GpxTracks.get(0).getConsignmentId()) != GpxTracks.size())
			GpsDb.writeGpxTracks(context, GpxTracks);
	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		
			if( Service.assignmentService.RunSheetID.equals(""))
				throw new UrlConnectionException();
				
			ServerResponse object = getFromServer(getServiceUrl(), "Route");
			
			setErrorMessage(object.getErrorMessage());
			array = object.getJsonArray(); 
		}

	@Override
	public GpxTrackModel decodeContent(JSONObject object) {
		
		GpxTrackModel model = new GpxTrackModel();
		
		model.setLatitude(setDoubleValueFromJSON(GpxEnum.Lat.Value, object));
		model.setLongitude(setDoubleValueFromJSON(GpxEnum.Lon.Value, object));
		model.setName(setStringValueFromJSON(GpxEnum.Name.Value, object));
		model.setOrder(setIntValueFromJSON(GpxEnum.Order.Value, object));
		model.setConsignmentId(Service.assignmentService.RunSheetID);

		return model;
	}
	
	public void DeleteGpxTracks()
	{
		GpsDb.deleteGpxTracks(context);
	}
}
