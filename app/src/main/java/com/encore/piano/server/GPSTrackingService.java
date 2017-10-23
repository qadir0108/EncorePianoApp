package com.encore.piano.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;

import com.encore.piano.db.GpsDb;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.GPSTrackingModel;
import com.encore.piano.model.GPSTrackingModel.GPSTrackingModelEnum;

public class GPSTrackingService extends BaseService {

	Context context;

	public GPSTrackingService(Context context) {
		super(context);

		this.context = context;
		
//		GPSTrackingModel m = new GPSTrackingModel();
//		m.setLatitude("49.4189667");
//		m.setLongitude("8.6769517");
//		
//		GPSTrackingModel m1 = new GPSTrackingModel();
//		m1.setLatitude("49.4189736");
//		m1.setLongitude("8.6770549");
//		
//		GPSTrackingModel m2 = new GPSTrackingModel();
//		m2.setLatitude("49.4187911");
//		m2.setLongitude("8.6771795");
//		
//		GPSTrackingModel m3 = new GPSTrackingModel();
//		m3.setLatitude("49.4187062");
//		m3.setLongitude("8.6772392");
//		
//		GPSTrackingModel m4 = new GPSTrackingModel();
//		m4.setLatitude("49.4184994");
//		m4.setLongitude("8.6773844");
//		
//		saveGpsCoordinates(m);
//		saveGpsCoordinates(m1);
//		saveGpsCoordinates(m2);
//		saveGpsCoordinates(m3);
//		saveGpsCoordinates(m4);

	}

	public void SaveGpsCoordinates(GPSTrackingModel model) {
		GpsDb.saveGpsCoordinates(context, model);
	}

	public ArrayList<GPSTrackingModel> GetGPSCoordinates(boolean synced) {
		return GpsDb.getGPSCoordinates(context, synced);
	}

	public void MarkGpsCoordinateModelsAsSynced(ArrayList<Integer> GPSCoordinatesModelIds) {
		GpsDb.markGpsCoordinateModelsAsSynced(context, GPSCoordinatesModelIds);
	}

	public void SyncGpsData() throws JSONException, ClientProtocolException, IOException {		
		
		ArrayList<GPSTrackingModel> gpsList = GetGPSCoordinates(false);
		ArrayList<Integer> GPSCoordinatesModelIds = new ArrayList<Integer>();
		
		if(gpsList.size() > 0){

			HttpPost postRequest = new HttpPost(ServiceUrls.GetGpsSynchornizationUrl(context));

			postRequest.setHeader("Content-Type", "application/json");

			JSONStringer loginJson = new JSONStringer();

			loginJson.array();

			
			for (GPSTrackingModel gpsTrackingModel : gpsList) {
				loginJson.object();
				loginJson.key(GPSTrackingModelEnum.Latitude.Value).value(gpsTrackingModel.getLatitude());
				loginJson.key(GPSTrackingModelEnum.Longitude.Value).value(gpsTrackingModel.getLongitude());				
				loginJson.endObject();	
				
				GPSCoordinatesModelIds.add(gpsTrackingModel.getId());
			}
			
			loginJson.endArray();
			
//			StringEntity loginEntity = new StringEntity(loginJson.toString());
//			postRequest.setEntity(loginEntity);
//
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpResponse httpResponse = httpClient.execute(postRequest);
//						
//			if(httpResponse.getStatusLine().getStatusCode() == 200){
//
//				markGpsCoordinateModelsAsSynced(GPSCoordinatesModelIds);
//
//				HttpEntity responseEntity = httpResponse.getEntity();
//				responseEntity.consumeContent();
				
//				if(responseEntity != null)
//				{
//					BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
//
//					String temp = "";			
//					StringBuilder responseStringBuilder = new StringBuilder();
//
//					while((temp = br.readLine()) != null)
//					{
//						responseStringBuilder.append(temp);
//					}
//
//					String response = responseStringBuilder.toString();
//				}
//			}
		}		
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

	public GPSTrackingModel GetLastCoordinate() {
		return GpsDb.getLastRecordedCoordinate(context);
	}

}
