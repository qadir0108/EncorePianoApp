package com.encore.piano.services;

import android.content.Context;

import com.encore.piano.db.Database;
import com.encore.piano.exceptions.*;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.GalleryModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class GalleryService extends BaseService {
	
	
	public GalleryService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
		
	}
	
	public ArrayList<GalleryModel> GetImagesForConsignment(String consignmentId)
	{
		return Database.GetImagesForConsignment(context, consignmentId);
	}

	public ArrayList<GalleryModel> GetImagesForConsignments(ArrayList<String> consignmentIds)
	{
		return Database.GetImagesForConsignments(context, consignmentIds);
	}
	
	
	public void WriteImageInfo(String consignmentId, String imagePath, String imageRef) throws DatabaseUpdateException
	{
		if(Database.WriteImageData(context, consignmentId, imagePath, imageRef) == -1)
			throw new DatabaseUpdateException();
	}
	
	
	@Override
	public URL getServiceUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BaseModel> T DecodeContent(JSONObject object) {
		// TODO Auto-generated method stub
		return null;
	}

}
