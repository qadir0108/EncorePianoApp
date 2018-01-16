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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;

import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.MessageEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.LogModel.LogModelEnum;

public class ErrorLogService extends BaseService {

	Context context;
	public ErrorLogService(Context context) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException{
		super(context);
		this.context = context;
	}
	
	public boolean sendErrorLog(String log) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException, ClientProtocolException, IOException{

        String url = ServiceUrls.getSendLogUrl(context);
        JSONStringer jsonData = new JSONStringer().object()
                .key(LogModelEnum.log.Value).value(log)
                .endObject();
        return post(url, jsonData.toString());
	}
	
	@Override
	public URL getServiceUrl() {
		String url = ServiceUrls.getSendLogUrl(context);
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
