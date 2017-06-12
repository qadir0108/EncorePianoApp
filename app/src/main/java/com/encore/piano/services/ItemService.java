package com.encore.piano.services;

import android.content.Context;

import com.encore.piano.db.Database;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.exceptions.*;
import com.encore.piano.model.BaseModel.ServerResponse;
import com.encore.piano.model.ConsignmentModel;
import com.encore.piano.model.PianoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class ItemService extends BaseService {

	ArrayList<PianoModel> Items = new ArrayList<PianoModel>();
	public int NumberOfItems = 0;

	Context context;
	String consignmentId;

	JSONArray array = new JSONArray();

	public ItemService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		super(context);
		this.context = context;
	}

	public void LoadItems() throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		
		ArrayList<ConsignmentModel> consignmentIds = Database.GetConsignments(context, false, false, "");
		
		for (ConsignmentModel consignmentModel : consignmentIds) {
			this.consignmentId = consignmentModel.getId();
			initialize();
		}
		
	}

	@Override
	public URL getServiceUrl() {
		String url = ServiceUrls.GetItemUrl(context).replace("authtokenvalue", ServiceUtility.loginService.LoginModel.getAuthToken())
									  .replace("consignmentidvalue", this.consignmentId);
		
		return getURLFromString(url);
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException {

		ArrayList<PianoModel> Items = new ArrayList<PianoModel>();
		for (int i = 0; i < array.length(); i++) {
			Items.add(DecodeContent(array.getJSONObject(i)));
		}

		if(Items.size() > 0){
			int noa = Database.WriteItems(context, consignmentId, Items);
			if (noa == -1)
				throw new DatabaseInsertException();
			else
				NumberOfItems += noa;
		}

	}

	public ArrayList<PianoModel> GetItemsForConsignment(String consignmentId) {
		return Database.GetItems(context, consignmentId);
	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		
		ServerResponse object = getFromServer(getServiceUrl(), "ConsignmentItems");

		array = object.getJsonArray();
		setErrorMessage(object.getErrorMessage());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PianoModel DecodeContent(JSONObject object) {

        PianoModel model = new PianoModel();

		model.setId(setStringValueFromJSON(PianoEnum.Id.Value, object));
		model.setConsignmentId(setStringValueFromJSON(PianoEnum.ConsignmentId.Value, object));
		model.setType(setStringValueFromJSON(PianoEnum.Type.Value, object));
		model.setName(setStringValueFromJSON(PianoEnum.Name.Value, object));
		model.setColor(setStringValueFromJSON(PianoEnum.Color.Value, object));
		model.setMake(setStringValueFromJSON(PianoEnum.Make.Value, object));
		model.setModel(setStringValueFromJSON(PianoEnum.Model.Value, object));
		model.setSerialNumber(setStringValueFromJSON(PianoEnum.SerialNumber.Value, object));
		model.setStairs(setBooleanValueFromJSON(PianoEnum.IsStairs.Value, object));
		model.setBench(setBooleanValueFromJSON(PianoEnum.IsBench.Value, object));
		model.setBoxed(setBooleanValueFromJSON(PianoEnum.IsBoxed.Value, object));

		return model;
	}

}
