package com.encore.piano.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

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

import com.encore.piano.db.AssignmentDb;
import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.MessageEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.interfaces.ProgressUpdateListener;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.DataSyncModel;
import com.encore.piano.model.DataSyncModel.DataSyncModelEnum;
import com.encore.piano.model.ProgressUpdateModel;

public class DataSynchronizationService extends BaseService {

	Context context;
	String VehicleCode;
	String taskName;

	public DataSynchronizationService(Context context)
			throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
		this.context = context;
		this.VehicleCode = Service.loginService.LoginModel.getFCMToken();
	}

	ProgressUpdateListener ProgressUpdateListener;

	public void RegisterProgressUpdateListener(ProgressUpdateListener listener)
	{
		ProgressUpdateListener = listener;
	}

	public void UnregisterProgressUpdateListener()
	{
		ProgressUpdateListener = null;
	}

	private void UpdateProgressListener(int count, int step, String title, String message)
	{

		ProgressUpdateModel updateModel = new ProgressUpdateModel();
		updateModel.setTaskName(taskName);
		updateModel.setItemsCount(count);
		updateModel.setStep(step);
		updateModel.setTitle(title);
		updateModel.setMessage(message);

		if (ProgressUpdateListener != null)
		{
			ProgressUpdateListener.OnProgressUpdateListener(updateModel);
		}
	}

	public void SynchronizeConsignment(String consignmentId) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
	{
		AssignmentModel cm = AssignmentDb.getAll(context, false, false, consignmentId).get(0);

		// now this method is called for partial status updates to server so we only check saved status now  
		//		if (!cm.isSaved())
		if (SendDataSyncContent(CreateDataSyncModel(cm), 1, 1))
			setSynced(cm.getId());

		if (AssignmentDb.isAllSyned(context))
			FinalizeConsignmentSynchronization(cm.getId(), 3);
	}

	public int GetNonSyncConsignmentCount()
	{
		return AssignmentDb.getAll(context, false, false, "").size();
	}

	public void SynchronizeConsignments() throws ClientProtocolException, IOException, JSONNullableException, EmptyStringException
	{
		UpdateProgressListener(5, 1, "Syncing consignments", "Preparing data for synchronization.");

		ArrayList<AssignmentModel> NonSyncedConsignments = new ArrayList<AssignmentModel>();
		ArrayList<DataSyncModel> DataSyncItems = new ArrayList<DataSyncModel>();

		NonSyncedConsignments = AssignmentDb.getAll(context, false, false, "");

		if (NonSyncedConsignments.size() == 0)
			UpdateProgressListener(4, 4, "Finished", "There is no unsynced consignments.");
		else
		{

			String RunSheetID = "";
			if (NonSyncedConsignments.size() > 0)
				if (NonSyncedConsignments.get(0).getId() != null)
					RunSheetID = NonSyncedConsignments.get(0).getId();

			int j = 0;
			for (AssignmentModel cm : NonSyncedConsignments)
			{
				DataSyncItems.add(CreateDataSyncModel(cm));
				//UpdateProgressListener(NonSyncedConsignments.size(), j++, "Syncing consignments", "Preparing data for synchronization.");
			}

			UpdateProgressListener(5, 2, "Syncing consignments", "Preparing data for synchronization.");

			int successfullSyncNumber = 0;
			for (int i = 0; i < DataSyncItems.size(); i++)
			{
				try
				{

					//UpdateProgressListener(DataSyncItems.size(), i, "Syncing consignments", "Sending saved consignments.");

					if (SendDataSyncContent(DataSyncItems.get(i), DataSyncItems.size(), i))
					{
						setSynced(DataSyncItems.get(i).getConsignmentId());
						successfullSyncNumber++;

						if (AssignmentDb.isAllSyned(context))
							FinalizeConsignmentSynchronization(RunSheetID, 3);
					}
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				} catch (JSONException e)
				{
					e.printStackTrace();
				} catch (DatabaseUpdateException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			UpdateProgressListener(5, 3, "Syncing consignments", "Sending saved consignments.");

			//UpdateProgressListener(DataSyncItems.size(), DataSyncItems.size()+1, "Completed", String.valueOf(successfullSyncNumber) + " of " + DataSyncItems.size() + ".");
			UpdateProgressListener(5, 4, "Completed", String.valueOf(successfullSyncNumber) + " of " + DataSyncItems.size() + ".");
			UpdateProgressListener(5, 5, "Completed", String.valueOf(successfullSyncNumber) + " of " + DataSyncItems.size() + ".");

		}

	}

	private DataSyncModel CreateDataSyncModel(AssignmentModel cm)
	{
		DataSyncModel model = new DataSyncModel();

//		ArrayList<GalleryModel> ConsignmentImages = Database.getImagesForAssignment(context, cm.getId());
//
//		String[] imagePaths = new String[ConsignmentImages.size()];
//		for (int i = 0; i < ConsignmentImages.size(); i++)
//			imagePaths[i] = ConsignmentImages.get(i).getImagePath();
//
//		model.setAssignmentId(cm.getId());
//		model.setAuthToken(Service.LoginService.LoginModel.getAuthToken());
//		model.setColCode(cm.getColCode());
//		model.setConsignmentReference(cm.getConsignmentReference());
//		//		model.setCustomerReference(cm.getCustomerReference());
//		model.setCustomerUsername(cm.getReceiverName());
//		model.setDateSigned(cm.getDateSigned());
//		model.setDeliveryCode(cm.getDeliveryCode());
//		model.setImages(imagePaths);
//		model.setPodStatus(cm.getPodStatus());
//		model.setTripStatus(cm.getTripStatus());
//		model.setSignatureImagePath(cm.getReceiverSignaturePath());
//		model.setSigned(cm.isSigned());
//		model.setSignedBy(cm.getReceiverName());
//		model.setEstimatedTime(cm.getEstimatedTime());
//		model.setDepartureTime(cm.getDepartureTime());
//		model.setSaved(cm.isSaved());
		return model;
	}

	private boolean SendDataSyncContent(DataSyncModel model, int count, int step) throws JSONException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
	{
		UpdateProgressListener(count, step, "Syncing consignments", "Sending assignmentService data.");
		HttpPost postRequest = new HttpPost(ServiceUrls.GetDataSynchornizationUrl(context));

		postRequest.setHeader("Content-Type", "application/json");

		JSONStringer loginJson = new JSONStringer();

		loginJson.object();
		loginJson.key(DataSyncModelEnum.AuthToken.Value).value(model.getAuthToken());
		loginJson.key(DataSyncModelEnum.ConsignmentID.Value).value(model.getConsignmentId());
		loginJson.key(DataSyncModelEnum.ColCode.Value).value(model.getColCode());
		loginJson.key(DataSyncModelEnum.ConsignmentReference.Value).value(model.getConsignmentReference());
		loginJson.key(DataSyncModelEnum.CustomerReference.Value).value(model.getCustomerReference());
		loginJson.key(DataSyncModelEnum.CustomerUsername.Value).value(model.getCustomerUsername());
		loginJson.key(DataSyncModelEnum.DateSigned.Value).value(model.getDateSigned());
		loginJson.key(DataSyncModelEnum.DeliveryCode.Value).value(model.getDeliveryCode());
		loginJson.key(DataSyncModelEnum.PodStatus.Value).value(model.getPodStatus());
		loginJson.key(DataSyncModelEnum.TripStatus.Value).value(model.getTripStatus());
		loginJson.key(DataSyncModelEnum.SignedBy.Value).value(model.getSignedBy());
		loginJson.key(DataSyncModelEnum.Signed.Value).value(model.isSigned());
		loginJson.key(DataSyncModelEnum.ArrivalTime.Value).value(model.getArrivalTime());
		loginJson.key(DataSyncModelEnum.DepartureTime.Value).value(model.getDepartureTime());
		loginJson.key(DataSyncModelEnum.Saved.Value).value(model.isSaved());
		loginJson.endObject();

		StringEntity loginEntity = new StringEntity(loginJson.toString());
		postRequest.setEntity(loginEntity);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);
		HttpEntity responseEntity = httpResponse.getEntity();

		if (responseEntity != null)
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()));

			String temp = "";
			StringBuilder responseStringBuilder = new StringBuilder();

			while ((temp = br.readLine()) != null)
			{
				responseStringBuilder.append(temp);
			}

			String response = responseStringBuilder.toString();

			boolean success = setBooleanValueFromJSON(JsonResponseEnum.IsSucess.Value, getJSONData(response).getJSONObject(MessageEnum.Message.Value));

			if (!success)
				return false;

			int i = 0;
			for (String imagePath : model.getImages())
			{
				UpdateProgressListener(count, step, "Syncing consignments", "Sending assignmentService images " + ++i + " of " + model.getImages().length + ".");

				int lastIndex = -1;
				if ((lastIndex = getLastIndexOfSlash(imagePath)) != -1)
				{
					String imageName = imagePath.substring(lastIndex, imagePath.length() - 4);
					String url = ServiceUrls.GetConsignmentPhotosServiceUrl(context)
							.replace("consignmentidvalue", model.getConsignmentId())
							.replace("authtokenvalue", model.getAuthToken())
							.replace("imageidvalue", imageName);
					if (!UploadImage(url, imagePath))
						return false;
				}
			}

			int lastIndex = -1;
			if ((lastIndex = getLastIndexOfSlash(model.getSignatureImagePath())) != -1)
			{

				UpdateProgressListener(1, 1, "Syncing consignments", "Sending customer signatureService.");
				String imageName = model.getSignatureImagePath().substring(lastIndex, model.getSignatureImagePath().length() - 4);
				String url = ServiceUrls.GetConsignmentSignatureUploadServiceUrl(context)
						.replace("consignmentidvalue", model.getConsignmentId())
						.replace("imageidvalue", imageName)
						.replace("authtokenvalue", model.getAuthToken());
				if (!UploadImage(url, model.getSignatureImagePath()))
					return false;
			}

			return success;
		}
		else
			return false;
	}

	private int getLastIndexOfSlash(String path)
	{
		try
		{
			return path.lastIndexOf("/") + 1;
		} catch (Exception ex)
		{
			return -1;
		}

	}

	private void setSynced(String consignmentId) throws DatabaseUpdateException
	{
		AssignmentDb.setSynced(context, consignmentId);
	}

	public boolean isAllSyned()
	{
		return AssignmentDb.isAllSyned(context);
	}

	@Override
	public URL getServiceUrl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent() throws JSONException, DatabaseInsertException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BaseModel> T decodeContent(JSONObject object)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setTaskName(String taskName)
	{
		// TODO Auto-generated method stub
		this.taskName = taskName;
	}

}
