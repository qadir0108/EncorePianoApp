package com.encore.piano.server;

import android.content.Context;

import com.encore.piano.db.ImageDb;
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
	
	public ArrayList<GalleryModel> getImagesForUnit(String unitId)
	{
		return ImageDb.getImagesForUnit(context, unitId);
	}

	public ArrayList<GalleryModel> getImagesForUnit(String unitId, String takenLocation)
	{
		return ImageDb.getImagesForUnit(context, unitId, takenLocation);
	}

	public ArrayList<GalleryModel> getImagesForUnits(ArrayList<String> consignmentIds)
	{
		return ImageDb.getImagesForUnits(context, consignmentIds);
	}
	
	
	public void writeImage(GalleryModel model) throws DatabaseUpdateException
	{
		if(ImageDb.write(context, model) == -1)
			throw new DatabaseUpdateException();
	}

	public void setSynced(String unitId) throws DatabaseUpdateException
	{
		ImageDb.setSynced(context, unitId);
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
	public <T extends BaseModel> T decodeContent(JSONObject object) {
		// TODO Auto-generated method stub
		return null;
	}

}
