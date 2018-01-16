package com.encore.piano.server;

import android.content.Context;

import com.encore.piano.db.AssignmentDb;
import com.encore.piano.db.UnitDb;
import com.encore.piano.exceptions.*;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.UnitModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class UnitService extends BaseService {

	ArrayList<UnitModel> Items = new ArrayList<UnitModel>();
	public int NumberOfItems = 0;

	Context context;
	String consignmentId;

	JSONArray array = new JSONArray();

	public UnitService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		super(context);
		this.context = context;
	}

	public void LoadItems() throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException, DatabaseInsertException {
		
		ArrayList<AssignmentModel> consignmentIds = AssignmentDb.getAll(context, false, false, "");
		for (AssignmentModel assignmentModel : consignmentIds) {
			this.consignmentId = assignmentModel.getId();
			initialize();
		}
		
	}

	public ArrayList<UnitModel> getUnitsByOrderId(String consignmentId) {
		return UnitDb.getUnitsByOrderId(context, consignmentId);
	}

	public UnitModel getUnitsByUnitId(String unitId) {
		ArrayList<UnitModel> models = UnitDb.getUnitsByUnitId(context, unitId);
		if(models.size() > 0)
			return models.get(0);
		else
			return null;
	}

    public UnitModel getUnitsBySerialNumber(String serialNumber) {
        ArrayList<UnitModel> models = UnitDb.getUnitsBySerialNumber(context, serialNumber);
        if(models.size() > 0)
            return models.get(0);
        else
            return null;
    }

    public void setLoaded(UnitModel model) throws DatabaseUpdateException {
        UnitDb.setUnitLoaded(context, model);
    }

	public void setDelivered(UnitModel model) throws DatabaseUpdateException {
		UnitDb.setUnitDelivered(context, model);
	}

    public void saveUnit(UnitModel model) throws DatabaseUpdateException {
        UnitDb.saveUnit(context, model);
    }

    @Override
    public URL getServiceUrl() {
        String url = ServiceUrls.GetItemUrl(context).replace("authtokenvalue", Service.loginService.LoginModel.getAuthToken())
                .replace("consignmentidvalue", this.consignmentId);

        return getURLFromString(url);
    }

    @Override
    public void setContent() throws JSONException, DatabaseInsertException {

//        ArrayList<UnitModel> Items = new ArrayList<UnitModel>();
//        for (int i = 0; i < array.length(); i++) {
//            Items.add(decodeContent(array.getJSONObject(i)));
//        }
//
//        if(Items.size() > 0){
//            int noa = Database.writeAll(context, consignmentId, Items);
//            if (noa == -1)
//                throw new DatabaseInsertException();
//            else
//                NumberOfItems += noa;
//        }

    }

    @Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		
//		ServerResponse object = getFromServer(getServiceUrl(), "ConsignmentItems");
//		array = object.getJsonArray();
//		setErrorMessage(object.getErrorMessage());
	}

	@SuppressWarnings("unchecked")
	@Override
	public UnitModel decodeContent(JSONObject object) {

        UnitModel model = new UnitModel();
//		model.setUnitId(setStringValueFromJSON(PianoEnum.Id.Value, object));
//		model.setImage(setStringValueFromJSON(PianoEnum.AssignmentId.Value, object));
//		model.setCategory(setStringValueFromJSON(PianoEnum.Category.Value, object));
//		model.setType(setStringValueFromJSON(PianoEnum.Type.Value, object));
//		model.setSize(setStringValueFromJSON(PianoEnum.Size.Value, object));
//		model.setFinish(setStringValueFromJSON(PianoEnum.Finish.Value, object));
//		model.setMake(setStringValueFromJSON(PianoEnum.Make.Value, object));
//		model.setModel(setStringValueFromJSON(PianoEnum.Model.Value, object));
//		model.setSerialNumber(setStringValueFromJSON(PianoEnum.SerialNumber.Value, object));
//		model.setPlayer(setBooleanValueFromJSON(PianoEnum.IsPlayer.Value, object));
//		model.setBench(setBooleanValueFromJSON(PianoEnum.IsBench.Value, object));
//		model.setBoxed(setBooleanValueFromJSON(PianoEnum.IsBoxed.Value, object));
		return model;
	}
}
