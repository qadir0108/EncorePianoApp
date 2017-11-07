package com.encore.piano.server;

import android.content.Context;

import com.encore.piano.db.AuthDb;
import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.LoginModelEnum;
import com.encore.piano.exceptions.*;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.LoginModel;
import com.encore.piano.util.DateTimeUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LoginService extends BaseService {

	public LoginModel LoginModel = new LoginModel();

	public LoginService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
	}
	
	public boolean checkLoginStatus()
	{
		boolean savedLoginData = false;
		try {
			LoginModel savedLoginModel = AuthDb.getLoginData(context);
			LoginModel.setAuthToken(savedLoginModel.getAuthToken());			
			LoginModel.setUserId(savedLoginModel.getUserId());
			LoginModel.setUserName(savedLoginModel.getUserName());
			LoginModel.setFCMToken(savedLoginModel.getFCMToken());
			savedLoginData = true;
		} catch (Exception e) {	
			savedLoginData = false;
		} 
		return savedLoginData;
	}
	
	public String doLogin(LoginModel loginModel) throws
	JSONException, 
	ClientProtocolException, 
	IOException, 
	EmptyStringException, 
	JSONNullableException, 
	LoginException
	{
		
		if(loginModel.getUserName().equals(EMPTY_STRING))
			throw new EmptyStringException();
		if(loginModel.getPassword().equals(EMPTY_STRING))
			throw new EmptyStringException();
//		if(loginModel.getFCMToken().equals(EMPTY_STRING))
//			throw new EmptyStringException();
		
		LoginModel = loginModel;

		HttpPost postRequest = new HttpPost(ServiceUrls.getLoginServiceUrl(context));
		
		postRequest.setHeader("Content-Type", "application/json");

		JSONStringer loginJson = new JSONStringer()
			.object()
				.key(LoginModelEnum.Username.Value).value(loginModel.getUserName())
				.key(LoginModelEnum.Password.Value).value(loginModel.getPassword())
				.key(LoginModelEnum.FCMToken.Value).value(loginModel.getFCMToken())
			.endObject();
			
		StringEntity loginEntity = new StringEntity(loginJson.toString());
		postRequest.setEntity(loginEntity);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);
		
		if(httpResponse.getStatusLine().getStatusCode() != 200)
			throw new LoginException();
		
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
			
			if(response.equals(EMPTY_STRING))
				throw new EmptyStringException();
			else
			{
				JSONObject responseObject = getJSONData(response);
				boolean success = setBooleanValueFromJSON(JsonResponseEnum.IsSucess.Value, responseObject);
				
				if(success)	{		
					LoginModel.setAuthToken(setStringValueFromJSON(LoginModelEnum.AuthToken.Value, responseObject));
					LoginModel.setTime(DateTimeUtility.getMilis24Hours());
					AuthDb.write(context, LoginModel);
				}
				else
					return setStringValueFromJSON(JsonResponseEnum.ErrorMessage.Value, responseObject);
			}
		}
		else
			throw new NullPointerException();
	
//		LoginModel.setAuthToken("123");
//		LoginModel.setTime(Service.getMilis24Hours());
//		Database.write(context, LoginModel);
		return EMPTY_STRING;
	}

	@Override
	public URL getServiceUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent() throws JSONException {
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

	public void LogOff() {
		AuthDb.logOff(context);
	}

	public void ActivateLoginData() throws EmptyAuthTokenException {
		AuthDb.activateLoginData(context);
	}

}
