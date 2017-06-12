package com.encore.piano.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.encore.piano.db.Database;
import com.encore.piano.enums.ConsignmentEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.BaseModel.ServerResponse;
import com.encore.piano.model.ConsignmentModel;
import com.encore.piano.model.PianoModel;

public class ConsignmentService extends BaseService {

	Context context;
	String Id;

	public ArrayList<ConsignmentModel> FetchedConsignments = new ArrayList<ConsignmentModel>();
	ArrayList<PianoModel> Items = new ArrayList<PianoModel>();
	public String RunSheetID = EMPTY_STRING;

	JSONArray array = new JSONArray();

	public int numberOfImportedConsigments = 0;
	public int numberOfImportedItems = 0;

	public ConsignmentService(Context context) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException {
		super(context);
		this.context = context;
	}

	public void LoadConsignments() throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException
	{
		initialize();
	}

    public void LoadConsignments(String id) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException
    {
        Id = id;
        initialize();
    }

	@Override
	public URL getServiceUrl()
	{
		String url = ServiceUrls.getConsignmentsUrl(context)
                .replace("[authtokenvalue]", ServiceUtility.loginService.LoginModel.getAuthToken());

        if(Id != null && !Id.isEmpty())
            url = ServiceUrls.getConsignmentUrl(context)
                    .replace("[authtokenvalue]", ServiceUtility.loginService.LoginModel.getAuthToken())
                    .replace("[id]", Id);

        return getURLFromString(url);
	}

    @Override
    public void fetchContent() throws UrlConnectionException, JSONException,
            JSONNullableException, NotConnectedException,
            NetworkStatePermissionException
    {

        ServerResponse object = getFromServer(getServiceUrl(), "Result");
        array = object.getJsonArray();
        setErrorMessage(object.getErrorMessage());
    }

	@Override
	public void setContent() throws JSONException, DatabaseInsertException
	{
		ArrayList<ConsignmentModel> Consignments = new ArrayList<ConsignmentModel>();
        ArrayList<PianoModel> pianos = new ArrayList<PianoModel>();

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject consignmentJsonObject = array.getJSONObject(i);
			ConsignmentModel model = GetConsignment(consignmentJsonObject);
            model.setTripStatus(TripStatusEnum.NotStarted.Value);
			model.setUnread(true);
			Consignments.add(model);

            JSONArray pianoArray = consignmentJsonObject.getJSONArray("Pianos");
            for (int j = 0; j < pianoArray.length(); j++) {
                JSONObject pianoObject = pianoArray.getJSONObject(j);
                PianoModel piano = GetPiano(pianoObject);
                pianos.add(piano);
            }
		}

		int importresult = Database.WriteConsignments(context, Consignments);
		int importresult2 = Database.WriteItems(context, pianos);

		if (importresult == -1)
			throw new DatabaseInsertException();
		else {
            numberOfImportedConsigments = importresult;
            numberOfImportedItems = importresult2;
        }

	}

    @Override
    public ConsignmentModel DecodeContent(JSONObject object)
    {
        ConsignmentModel model = new ConsignmentModel();

        model.setId(setStringValueFromJSON(ConsignmentEnum.Id.Value, object));
        model.setConsignmentNumber(setStringValueFromJSON(ConsignmentEnum.ConsignmentNumber.Value, object));
        model.setStartWarehouseName(setStringValueFromJSON(ConsignmentEnum.StartWarehouseName.Value, object));
        model.setStartWarehouseAddress(setStringValueFromJSON(ConsignmentEnum.StartWarehouseAddress.Value, object));
        model.setVehicleCode(setStringValueFromJSON(ConsignmentEnum.VehicleCode.Value, object));
        model.setVehicleName(setStringValueFromJSON(ConsignmentEnum.VehicleName.Value, object));
        model.setDriverCode(setStringValueFromJSON(ConsignmentEnum.DriverCode.Value, object));
        model.setDriverName(setStringValueFromJSON(ConsignmentEnum.DriverName.Value, object));
        model.setOrderId(setStringValueFromJSON(ConsignmentEnum.OrderId.Value, object));
        model.setOrderNumber(setStringValueFromJSON(ConsignmentEnum.OrderNumber.Value, object));
        model.setOrderedAt(setStringValueFromJSON(ConsignmentEnum.OrderedAt.Value, object));
        model.setOrderType(setStringValueFromJSON(ConsignmentEnum.OrderType.Value, object));
        model.setCallerName(setStringValueFromJSON(ConsignmentEnum.CallerName.Value, object));
        model.setCallerPhoneNumber(setStringValueFromJSON(ConsignmentEnum.CallerPhoneNumber.Value, object));
        model.setSpecialInstructions(setStringValueFromJSON(ConsignmentEnum.SpecialInstructions.Value, object));
        model.setPickupAddress(setStringValueFromJSON(ConsignmentEnum.PickupAddress.Value, object));
        model.setDeliveryAddress(setStringValueFromJSON(ConsignmentEnum.DeliveryAddress.Value, object));
        model.setCustomerCode(setStringValueFromJSON(ConsignmentEnum.CustomerCode.Value, object));
        model.setCustomerName(setStringValueFromJSON(ConsignmentEnum.CustomerName.Value, object));
        model.setNumberOfItems(setIntValueFromJSON(ConsignmentEnum.NumberOfItems.Value, object));
        return model;
    }

    public ConsignmentModel GetConsignment(JSONObject object)
    {
        ConsignmentModel model = new ConsignmentModel();

        model.setId(setStringValueFromJSON(ConsignmentEnum.Id.Value, object));
        model.setConsignmentNumber(setStringValueFromJSON(ConsignmentEnum.ConsignmentNumber.Value, object));
        model.setStartWarehouseName(setStringValueFromJSON(ConsignmentEnum.StartWarehouseName.Value, object));
        model.setStartWarehouseAddress(setStringValueFromJSON(ConsignmentEnum.StartWarehouseAddress.Value, object));
        model.setVehicleCode(setStringValueFromJSON(ConsignmentEnum.VehicleCode.Value, object));
        model.setVehicleName(setStringValueFromJSON(ConsignmentEnum.VehicleName.Value, object));
        model.setDriverCode(setStringValueFromJSON(ConsignmentEnum.DriverCode.Value, object));
        model.setDriverName(setStringValueFromJSON(ConsignmentEnum.DriverName.Value, object));
        model.setOrderId(setStringValueFromJSON(ConsignmentEnum.OrderId.Value, object));
        model.setOrderNumber(setStringValueFromJSON(ConsignmentEnum.OrderNumber.Value, object));
        model.setOrderedAt(setStringValueFromJSON(ConsignmentEnum.OrderedAt.Value, object));
        model.setOrderType(setStringValueFromJSON(ConsignmentEnum.OrderType.Value, object));
        model.setCallerName(setStringValueFromJSON(ConsignmentEnum.CallerName.Value, object));
        model.setCallerPhoneNumber(setStringValueFromJSON(ConsignmentEnum.CallerPhoneNumber.Value, object));
        model.setSpecialInstructions(setStringValueFromJSON(ConsignmentEnum.SpecialInstructions.Value, object));
        model.setPickupAddress(setStringValueFromJSON(ConsignmentEnum.PickupAddress.Value, object));
        model.setDeliveryAddress(setStringValueFromJSON(ConsignmentEnum.DeliveryAddress.Value, object));
        model.setCustomerCode(setStringValueFromJSON(ConsignmentEnum.CustomerCode.Value, object));
        model.setCustomerName(setStringValueFromJSON(ConsignmentEnum.CustomerName.Value, object));
        model.setNumberOfItems(setIntValueFromJSON(ConsignmentEnum.NumberOfItems.Value, object));
        return model;
    }

    public PianoModel GetPiano(JSONObject object)
    {
        PianoModel model = new PianoModel();

        model.setId(setStringValueFromJSON(PianoEnum.Id.Value, object));
        model.setConsignmentId(setStringValueFromJSON(PianoEnum.ConsignmentId.Value, object));
        model.setType(setStringValueFromJSON(PianoEnum.Type.Value, object));
        model.setName(setStringValueFromJSON(PianoEnum.Name.Value, object));
        model.setColor(setStringValueFromJSON(PianoEnum.Color.Value, object));
        model.setMake(setStringValueFromJSON(PianoEnum.Make.Value, object));
        model.setModel(setStringValueFromJSON(PianoEnum.Model.Value, object));
        model.setSerialNumber(setStringValueFromJSON(PianoEnum.SerialNumber.Value, object));
        model.setStairs(setIntValueFromJSON(PianoEnum.IsStairs.Value, object) == 1);
        model.setBench(setIntValueFromJSON(PianoEnum.IsBench.Value, object) == 1);
        model.setBoxed(setIntValueFromJSON(PianoEnum.IsBoxed.Value, object) == 1);
        return model;
    }

    public void ReloadConsignments() throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException
    {
        FetchedConsignments.clear();
        initialize();
    }

	public ArrayList<ConsignmentModel> GetConsignments(boolean showCompleted)
	{
		return Database.GetConsignments(context, showCompleted, false, "");
	}

	public ConsignmentModel GetConsignmentById(String id)
	{
		return Database.GetConsignments(context, false, false, id).get(0);
	}

	public void WriteCustomerSignatureAndStatus(String customerName, String customerSinaturePath, String consignmentId, String tripStatus, String podStatus) throws DatabaseUpdateException
	{
		// Customer Name and Sign
		if (Database.WriteCustomerSignature(context, customerName, customerSinaturePath, consignmentId) != 1)
			throw new DatabaseUpdateException();

	}

	public void SaveConsignment(String consignmentId, String tripStatus, String podStatus, String[] imagesPaths) throws DatabaseUpdateException
	{
		if (Database.SaveConsignment(context, consignmentId, tripStatus) != 1)
			throw new DatabaseUpdateException();
	}

    public boolean SetTripStatus(String consignmentId, String startedStatus)
    {
        if (Database.SetTripStatus(context, consignmentId, startedStatus) != 1)
            return false;
        return true;
    }

	public boolean SaveArrivalTime(String consignmentId, String tripStatus, String arrival)
	{
		if (Database.SaveArrivalTime(context, consignmentId, tripStatus, arrival) != 1)
			return false;
		return true;
	}

	public boolean SaveDepartureTime(String consignmentId, String dept)
	{
		if (Database.SaveDepartureTime(context, consignmentId, dept) != 1)
			return false;
		return true;
	}

	public int GetImagesCount(String consignmentId)
	{
		return Database.GetImageCountForConsignment(context, consignmentId);
	}

	public void DeleteConsignments(String consignmentId) throws IOException
	{
		ArrayList<ConsignmentModel> consToDelete = Database.GetConsignments(context, false, false, consignmentId);

		for (ConsignmentModel consignmentModel : consToDelete)
		{
			if (consignmentModel.getReceiverSignaturePath() != null && !consignmentModel.getReceiverSignaturePath().equals(""))
			{
				File f = new File(consignmentModel.getReceiverSignaturePath());
				if (f.exists())
					f.delete();
			}

			Database.DeleteImagesForConsignments(context, consignmentModel.getId());
			Database.DeleteConsignmentItems(context, consignmentModel.getId());
		}

		Database.DeleteConsignments(context, consToDelete);
	}

}
