package com.encore.piano.server;

import android.content.Context;
import com.encore.piano.exceptions.*;
import com.encore.piano.model.ConfirmationModel;
import com.encore.piano.model.ConfirmationModel.ConfirmationModelEnum;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class ConfirmationService extends BaseService {

	public ArrayList<ConfirmationModel> Confirmations = new ArrayList<ConfirmationModel>();
	
	JSONArray array = new JSONArray();
	public ConfirmationService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		super(context);
		
		initialize();
	}

	@Override
	public URL getServiceUrl() {
		String temp = ServiceUrls.GetConfirmationConditionsUrl(context);
		temp = temp.replace("authtokenvalue", Service.loginService.LoginModel.getAuthToken());
		return getURLFromString(temp);
	}

	@Override
	public void setContent() throws JSONException {
		for(int i = 0; i < array.length(); i++){
			Confirmations.add(decodeContent(array.getJSONObject(i)));
		}
		
		//Confirmations = StaticData.GetConfirmations();
	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		
		array = getJSONData(getServiceUrl()).getJSONArray("CheckListItems");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConfirmationModel decodeContent(JSONObject object) {
		ConfirmationModel model = new ConfirmationModel();
		
		model.setCondition(setStringValueFromJSON(ConfirmationModelEnum.Condition.Value, object));
		model.setId(setIntValueFromJSON(ConfirmationModelEnum.Id.Value, object));
		model.setConfirmed(setBooleanValueFromJSON(ConfirmationModelEnum.Confirmed.Value, object));
		
		return model;
	}

}
