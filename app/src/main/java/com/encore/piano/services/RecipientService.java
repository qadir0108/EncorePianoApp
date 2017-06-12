package com.encore.piano.services;

import android.content.Context;
import com.encore.piano.exceptions.*;
import com.encore.piano.model.BaseModel.ServerResponse;
import com.encore.piano.model.RecipientModel;
import com.encore.piano.model.RecipientModel.RecipientModelEnum;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class RecipientService extends BaseService {

	public ArrayList<RecipientModel> Recipients = new ArrayList<RecipientModel>();
	
	JSONArray array = null;
	public RecipientService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		super(context);
		initialize();
	}

	@Override
	public URL getServiceUrl() {
		
		String url = ServiceUrls.GetRecipientUrl(context).replace("authtokenvalue", ServiceUtility.loginService.LoginModel.getAuthToken());
		
		return getURLFromString(url);
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException {
		
		for(int i = 0; i < array.length(); i++)
			Recipients.add(DecodeContent(array.getJSONObject(i)));
		
		//Recipients = StaticData.GetRecipients();
	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		
		
		ServerResponse object = getFromServer(getServiceUrl(), "Users");
		
		array = object.getJsonArray();
		setErrorMessage(object.getErrorMessage());

	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipientModel DecodeContent(JSONObject object) {
		RecipientModel model = new RecipientModel();
			
		model.setName(setStringValueFromJSON(RecipientModelEnum.Name.Value, object));
		
		return model;
	}

}
