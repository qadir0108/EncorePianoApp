package com.encore.piano.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;

import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.MessageEnum;
import com.encore.piano.sync.SyncAssignmentEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.BaseModel;

public class AcknowledgmentService extends BaseService {

	Context context;
	
	public String RunsheetId = EMPTY_STRING;
	public String MessageId = EMPTY_STRING;
	
	JSONArray array = new JSONArray();
	
	public AcknowledgmentService(Context context) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException{
		super(context);
		
		this.context = context;
		
	}
	
	public boolean AcknowledgeConsignment(String RunsheetId) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException, ClientProtocolException, IOException{
		
		this.RunsheetId = RunsheetId;
		boolean isAcknowledged = true;
		
		HttpPost postRequest = new HttpPost(ServiceUrls.GetRunsheetAckUrl(context));
		
		postRequest.setHeader("Content-Type", "application/json");

		JSONStringer jsonData = new JSONStringer();
		
		jsonData.object();
		jsonData.key(SyncAssignmentEnum.AuthToken.Value).value(Service.loginService.LoginModel.getAuthToken());
		jsonData.key(SyncAssignmentEnum.RunSheetID.Value).value(this.RunsheetId);
		jsonData.key(SyncAssignmentEnum.Acknowledged.Value).value(isAcknowledged);
		jsonData.endObject();
			
		StringEntity loginEntity = new StringEntity(jsonData.toString());
		postRequest.setEntity(loginEntity);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);
		HttpEntity responseEntity = httpResponse.getEntity();
		
		if(responseEntity != null)
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
			
			String temp = "";			
			StringBuilder responseStringBuilder = new StringBuilder();
			
			while((temp = br.readLine()) != null)
			{
				responseStringBuilder.append(temp);
			}
			
			String response = responseStringBuilder.toString();
			
			boolean success = setBooleanValueFromJSON(JsonResponseEnum.IsSucess.Value, getJSONData(response).getJSONObject(MessageEnum.Message.Value));
			
			if(success)
				return true;
			else
				return false;
			
		}
		
		return false;
		
	}

	@Override
	public URL getServiceUrl() {
		String url = ServiceUrls.GetRunsheetAckUrl(context);
		return getURLFromString(url);

	}

	@Override
	public <T extends BaseModel> T decodeContent(JSONObject object) {
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
	
}
