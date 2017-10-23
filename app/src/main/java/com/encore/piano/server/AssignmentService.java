package com.encore.piano.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.encore.piano.db.AssignmentDb;
import com.encore.piano.db.ImageDb;
import com.encore.piano.db.UnitDb;
import com.encore.piano.enums.AssignmentEnum;
import com.encore.piano.enums.PianoEnum;
import com.encore.piano.enums.PianoStatusEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel.ServerResponse;
import com.encore.piano.model.UnitModel;
import com.encore.piano.util.DateTimeUtility;

public class AssignmentService extends BaseService {

	Context context;
	String Id;

	public ArrayList<AssignmentModel> FetchedConsignments = new ArrayList<AssignmentModel>();
	ArrayList<UnitModel> Items = new ArrayList<UnitModel>();
	public String RunSheetID = EMPTY_STRING;

	JSONArray array = new JSONArray();

	public int numberOfImportedConsigments = 0;
	public int numberOfImportedItems = 0;

	public AssignmentService(Context context) throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException {
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
                .replace("[authtokenvalue]", Service.loginService.LoginModel.getAuthToken());

        if(Id != null && !Id.isEmpty())
            url = ServiceUrls.getConsignmentUrl(context)
                    .replace("[authtokenvalue]", Service.loginService.LoginModel.getAuthToken())
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
		ArrayList<AssignmentModel> assignments = new ArrayList<AssignmentModel>();
        ArrayList<UnitModel> pianos = new ArrayList<UnitModel>();

		for (int i = 0; i < array.length(); i++)
		{
			JSONObject consignmentJsonObject = array.getJSONObject(i);
			AssignmentModel model = getAssignment(consignmentJsonObject);
            model.setCreatedAt(DateTimeUtility.getCurrentTimeStamp());
            model.setTripStatus(TripStatusEnum.NotStarted.name());
			model.setUnread(true);
			assignments.add(model);

            JSONArray pianoArray = consignmentJsonObject.getJSONArray("Pianos");
            for (int j = 0; j < pianoArray.length(); j++) {
                JSONObject pianoObject = pianoArray.getJSONObject(j);
                UnitModel piano = getPiano(pianoObject);
                piano.setCreatedAt(DateTimeUtility.getCurrentTimeStamp());
                piano.setPianoStatus(PianoStatusEnum.Booked.name());
                pianos.add(piano);
            }
		}

		int importresult = AssignmentDb.writeAll(context, assignments);

		if (importresult == -1)
			throw new DatabaseInsertException();
		else {
            int importresult2 = UnitDb.writeAll(context, pianos);
            numberOfImportedConsigments = importresult;
            numberOfImportedItems = importresult2;
        }

	}

    @Override
    public AssignmentModel decodeContent(JSONObject object)
    {
        AssignmentModel model = new AssignmentModel();

        model.setId(setStringValueFromJSON(AssignmentEnum.Id.Value, object));
        model.setConsignmentNumber(setStringValueFromJSON(AssignmentEnum.ConsignmentNumber.Value, object));
        model.setVehicleCode(setStringValueFromJSON(AssignmentEnum.VehicleCode.Value, object));
        model.setVehicleName(setStringValueFromJSON(AssignmentEnum.VehicleName.Value, object));
        model.setDriverCode(setStringValueFromJSON(AssignmentEnum.DriverCode.Value, object));
        model.setDriverName(setStringValueFromJSON(AssignmentEnum.DriverName.Value, object));

        model.setOrderId(setStringValueFromJSON(AssignmentEnum.OrderId.Value, object));
        model.setOrderNumber(setStringValueFromJSON(AssignmentEnum.OrderNumber.Value, object));
        model.setOrderType(setStringValueFromJSON(AssignmentEnum.OrderType.Value, object));
        model.setOrderedAt(setStringValueFromJSON(AssignmentEnum.OrderedAt.Value, object));
        model.setCallerName(setStringValueFromJSON(AssignmentEnum.CallerName.Value, object));
        model.setCallerPhoneNumber(setStringValueFromJSON(AssignmentEnum.CallerPhoneNumber.Value, object));
        model.setCallerPhoneNumberAlt(setStringValueFromJSON(AssignmentEnum.CallerPhoneNumberAlt.Value, object));
        model.setCallerPhoneNumberAlt(setStringValueFromJSON(AssignmentEnum.CallerEmail.Value, object));

        model.setPickupDate(setStringValueFromJSON(AssignmentEnum.PickupDate.Value, object));
        model.setPickupAddress(setStringValueFromJSON(AssignmentEnum.PickupAddress.Value, object));
        model.setPickupPhoneNumber(setStringValueFromJSON(AssignmentEnum.PickupPhoneNumber.Value, object));
        model.setPickupAlternateContact(setStringValueFromJSON(AssignmentEnum.PickupAlternateContact.Value, object));
        model.setPickupAlternatePhone(setStringValueFromJSON(AssignmentEnum.PickupAlternatePhone.Value, object));
        model.setPickupNumberStairs(setStringValueFromJSON(AssignmentEnum.PickupNumberStairs.Value, object));
        model.setPickupNumberTurns(setStringValueFromJSON(AssignmentEnum.PickupNumberTurns.Value, object));
        model.setPickupInstructions(setStringValueFromJSON(AssignmentEnum.PickupInstructions.Value, object));

        model.setDeliveryDate(setStringValueFromJSON(AssignmentEnum.DeliveryDate.Value, object));
        model.setDeliveryAddress(setStringValueFromJSON(AssignmentEnum.DeliveryAddress.Value, object));
        model.setDeliveryPhoneNumber(setStringValueFromJSON(AssignmentEnum.DeliveryPhoneNumber.Value, object));
        model.setDeliveryAlternateContact(setStringValueFromJSON(AssignmentEnum.DeliveryAlternateContact.Value, object));
        model.setDeliveryAlternatePhone(setStringValueFromJSON(AssignmentEnum.DeliveryAlternatePhone.Value, object));
        model.setDeliveryNumberStairs(setStringValueFromJSON(AssignmentEnum.DeliveryNumberStairs.Value, object));
        model.setDeliveryNumberTurns(setStringValueFromJSON(AssignmentEnum.DeliveryNumberTurns.Value, object));
        model.setDeliveryInstructions(setStringValueFromJSON(AssignmentEnum.DeliveryInstructions.Value, object));

        model.setCustomerCode(setStringValueFromJSON(AssignmentEnum.CustomerCode.Value, object));
        model.setCustomerName(setStringValueFromJSON(AssignmentEnum.CustomerName.Value, object));
        model.setNumberOfItems(setIntValueFromJSON(AssignmentEnum.NumberOfItems.Value, object));
        return model;
    }

    public AssignmentModel getAssignment(JSONObject object)
    {
        AssignmentModel model = new AssignmentModel();


        model.setId(setStringValueFromJSON(AssignmentEnum.Id.Value, object));
        model.setConsignmentNumber(setStringValueFromJSON(AssignmentEnum.ConsignmentNumber.Value, object));
        model.setVehicleCode(setStringValueFromJSON(AssignmentEnum.VehicleCode.Value, object));
        model.setVehicleName(setStringValueFromJSON(AssignmentEnum.VehicleName.Value, object));
        model.setDriverCode(setStringValueFromJSON(AssignmentEnum.DriverCode.Value, object));
        model.setDriverName(setStringValueFromJSON(AssignmentEnum.DriverName.Value, object));

        model.setOrderId(setStringValueFromJSON(AssignmentEnum.OrderId.Value, object));
        model.setOrderNumber(setStringValueFromJSON(AssignmentEnum.OrderNumber.Value, object));
        model.setOrderType(setStringValueFromJSON(AssignmentEnum.OrderType.Value, object));
        model.setOrderedAt(setStringValueFromJSON(AssignmentEnum.OrderedAt.Value, object));
        model.setCallerName(setStringValueFromJSON(AssignmentEnum.CallerName.Value, object));
        model.setCallerPhoneNumber(setStringValueFromJSON(AssignmentEnum.CallerPhoneNumber.Value, object));
        model.setCallerPhoneNumberAlt(setStringValueFromJSON(AssignmentEnum.CallerPhoneNumberAlt.Value, object));
        model.setCallerEmail(setStringValueFromJSON(AssignmentEnum.CallerEmail.Value, object));

        model.setPickupDate(setStringValueFromJSON(AssignmentEnum.PickupDate.Value, object));
        model.setPickupAddress(setStringValueFromJSON(AssignmentEnum.PickupAddress.Value, object));
        model.setPickupPhoneNumber(setStringValueFromJSON(AssignmentEnum.PickupPhoneNumber.Value, object));
        model.setPickupAlternateContact(setStringValueFromJSON(AssignmentEnum.PickupAlternateContact.Value, object));
        model.setPickupAlternatePhone(setStringValueFromJSON(AssignmentEnum.PickupAlternatePhone.Value, object));
        model.setPickupNumberStairs(setStringValueFromJSON(AssignmentEnum.PickupNumberStairs.Value, object));
        model.setPickupNumberTurns(setStringValueFromJSON(AssignmentEnum.PickupNumberTurns.Value, object));
        model.setPickupInstructions(setStringValueFromJSON(AssignmentEnum.PickupInstructions.Value, object));

        model.setDeliveryDate(setStringValueFromJSON(AssignmentEnum.DeliveryDate.Value, object));
        model.setDeliveryAddress(setStringValueFromJSON(AssignmentEnum.DeliveryAddress.Value, object));
        model.setDeliveryPhoneNumber(setStringValueFromJSON(AssignmentEnum.DeliveryPhoneNumber.Value, object));
        model.setDeliveryAlternateContact(setStringValueFromJSON(AssignmentEnum.DeliveryAlternateContact.Value, object));
        model.setDeliveryAlternatePhone(setStringValueFromJSON(AssignmentEnum.DeliveryAlternatePhone.Value, object));
        model.setDeliveryNumberStairs(setStringValueFromJSON(AssignmentEnum.DeliveryNumberStairs.Value, object));
        model.setDeliveryNumberTurns(setStringValueFromJSON(AssignmentEnum.DeliveryNumberTurns.Value, object));
        model.setDeliveryInstructions(setStringValueFromJSON(AssignmentEnum.DeliveryInstructions.Value, object));

        model.setCustomerCode(setStringValueFromJSON(AssignmentEnum.CustomerCode.Value, object));
        model.setCustomerName(setStringValueFromJSON(AssignmentEnum.CustomerName.Value, object));
        model.setNumberOfItems(setIntValueFromJSON(AssignmentEnum.NumberOfItems.Value, object));
        return model;
    }

    public UnitModel getPiano(JSONObject object)
    {
        UnitModel model = new UnitModel();

        model.setId(setStringValueFromJSON(PianoEnum.Id.Value, object));
        model.setAssignmentId(setStringValueFromJSON(PianoEnum.ConsignmentId.Value, object));
        model.setCategory(setStringValueFromJSON(PianoEnum.Category.Value, object));
        model.setType(setStringValueFromJSON(PianoEnum.Type.Value, object));
        model.setSize(setStringValueFromJSON(PianoEnum.Size.Value, object));
        model.setMake(setStringValueFromJSON(PianoEnum.Make.Value, object));
        model.setModel(setStringValueFromJSON(PianoEnum.Model.Value, object));
        model.setFinish(setStringValueFromJSON(PianoEnum.Finish.Value, object));
        model.setSerialNumber(setStringValueFromJSON(PianoEnum.SerialNumber.Value, object));
        model.setBench(setIntValueFromJSON(PianoEnum.IsBench.Value, object) == 1);
        model.setPlayer(setIntValueFromJSON(PianoEnum.IsPlayer.Value, object) == 1);
        model.setBoxed(setIntValueFromJSON(PianoEnum.IsBoxed.Value, object) == 1);

        return model;
    }

    public void ReloadConsignments() throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException, DatabaseInsertException
    {
        FetchedConsignments.clear();
        initialize();
    }

	public ArrayList<AssignmentModel> getAll(boolean showCompleted)
	{
		return AssignmentDb.getAll(context, showCompleted, false, "");
	}

	public AssignmentModel getAll(String id)
	{
		return AssignmentDb.getAll(context, false, false, id).get(0);
	}

    public boolean startTrip(String consignmentId, String departureTime, String estimatedTime)
    {
        if (AssignmentDb.startTrip(context, consignmentId, departureTime, estimatedTime) != 1)
            return false;
        return true;
    }

	public void writeCustomerSignatureAndStatus(String customerName, String customerSinaturePath, String consignmentId) throws DatabaseUpdateException
	{
		// Customer Size and Sign
		if (AssignmentDb.writeCustomerSignature(context, customerName, customerSinaturePath, consignmentId) != 1)
			throw new DatabaseUpdateException();

	}

    public boolean setTripStatus(String consignmentId, String tripStatus)
    {
        if (AssignmentDb.setTripStatus(context, consignmentId, tripStatus) != 1)
            return false;
        return true;
    }

	public int GetImagesCount(String consignmentId)
	{
		return ImageDb.getImageCountForAssignment(context, consignmentId);
	}

	public void deleteAssignments(String consignmentId) throws IOException
	{
		ArrayList<AssignmentModel> consToDelete = AssignmentDb.getAll(context, false, false, consignmentId);
		for (AssignmentModel assignmentModel : consToDelete)
		{
			if (assignmentModel.getReceiverSignaturePath() != null && !assignmentModel.getReceiverSignaturePath().equals(""))
			{
				File f = new File(assignmentModel.getReceiverSignaturePath());
				if (f.exists())
					f.delete();
			}

			ImageDb.deleteImagesForAssignments(context, assignmentModel.getId());
			UnitDb.delete(context, assignmentModel.getId());
		}

		AssignmentDb.delete(context, consToDelete);
	}

}
