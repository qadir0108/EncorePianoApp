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
import com.encore.piano.db.ImageDb;
import com.encore.piano.db.UnitDb;
import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.MessageEnum;
import com.encore.piano.sync.SyncAssignmentEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.GalleryModel;
import com.encore.piano.sync.SyncAssignmentModel;
import com.encore.piano.model.UnitModel;
import com.encore.piano.sync.SyncDeliverModel;
import com.encore.piano.sync.SyncImageModel;
import com.encore.piano.sync.SyncLoadModel;
import com.encore.piano.sync.SyncStatusModel;

public class SyncService extends BaseService {

	public SyncService(Context context)
			throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
	}

	public void syncStart(String assignmentId) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
	{
		AssignmentModel model = AssignmentDb.getAll(context, false, false, assignmentId).get(0);
        SyncStatusModel syncModel = new SyncStatusModel().fromModelForStart(model);
        String url = ServiceUrls.getSyncStartUrl(context).replace("[authtokenvalue]", syncModel.getAuthToken());

		if (post(url, syncModel.getJsonForStart()))
            AssignmentDb.setSynced(context, model.getId());
	}

    public void syncStatus(String assignmentId) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
    {
        AssignmentModel model = AssignmentDb.getAll(context, false, false, assignmentId).get(0);
        SyncStatusModel syncModel = new SyncStatusModel().fromModelForStatus(model);
        String url = ServiceUrls.getSyncStatusUrl(context).replace("[authtokenvalue]", syncModel.getAuthToken());

        if (post(url, syncModel.getJsonForStatus()))
            AssignmentDb.setSynced(context, model.getId());
    }

    public void syncLoad(String assignmentId, String unitId) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
    {
        UnitModel model = Service.unitService.getUnitsByUnitId(unitId);
        model.setAssignmentId(assignmentId);
        SyncLoadModel syncModel = new SyncLoadModel().fromModel(model);
        String url = ServiceUrls.getSyncLoadUrl(context).replace("[authtokenvalue]", syncModel.getAuthToken());

        if (post(url, syncModel.getJson()))
            UnitDb.setSyncedLoaded(context, model.getId());
    }

    public void syncDeliver(String assignmentId, String unitId) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
    {
        UnitModel model = Service.unitService.getUnitsByUnitId(unitId);
        model.setAssignmentId(assignmentId);
        SyncDeliverModel syncModel = new SyncDeliverModel().fromModel(model);
        String url = ServiceUrls.getSyncDeliverUrl(context).replace("[authtokenvalue]", syncModel.getAuthToken());

        if (post(url, syncModel.getJson()))
            UnitDb.setSyncedDelivered(context, model.getId());
    }

    public void syncImages(String assignmentId, String unitId, String takenLocation) throws JSONException, DatabaseUpdateException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException, NotConnectedException, UrlConnectionException, NetworkStatePermissionException {
        if (Service.galleryService == null)
            Service.galleryService = new GalleryService(context);

        ArrayList<GalleryModel> images = Service.galleryService.getImagesForUnit(unitId, takenLocation);

        for (GalleryModel model:images) {
            SyncImageModel syncModel = new SyncImageModel().fromModel(model);
            syncModel.setAssignmentId(assignmentId);
            String url = ServiceUrls.getSyncImageUrl(context).replace("[authtokenvalue]", syncModel.getAuthToken());
            if (post(url, syncModel.getJson()))
                ImageDb.setSynced(context, model.getId());
        }
    }

	public void syncAll() throws ClientProtocolException, IOException, JSONNullableException, EmptyStringException
	{
		updateProgress("Sync",5, 1, "Syncing consignments", "Preparing data for synchronization.");

		ArrayList<AssignmentModel> NonSyncedConsignments = new ArrayList<AssignmentModel>();
		ArrayList<SyncAssignmentModel> DataSyncItems = new ArrayList<SyncAssignmentModel>();

		NonSyncedConsignments = AssignmentDb.getAll(context, false, false, "");

		if (NonSyncedConsignments.size() == 0)
			updateProgress("Sync",4, 4, "Finished", "There is no unsynced consignments.");
		else
		{

			String RunSheetID = "";
			if (NonSyncedConsignments.size() > 0)
				if (NonSyncedConsignments.get(0).getId() != null)
					RunSheetID = NonSyncedConsignments.get(0).getId();

			int j = 0;
			for (AssignmentModel cm : NonSyncedConsignments)
			{
				DataSyncItems.add(createSyncStartModel(cm));
				//updateProgress(NonSyncedConsignments.size(), j++, "Syncing consignments", "Preparing data for synchronization.");
			}

			updateProgress("Sync",5, 2, "Syncing consignments", "Preparing data for synchronization.");

			int successfullSyncNumber = 0;
			for (int i = 0; i < DataSyncItems.size(); i++)
			{
				try
				{

					//updateProgress(DataSyncItems.size(), i, "Syncing consignments", "Sending saved consignments.");

					if (sync(DataSyncItems.get(i), DataSyncItems.size(), i))
					{
                        AssignmentDb.setSynced(context, DataSyncItems.get(i).getId());
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

			updateProgress("Sync",5, 3, "Syncing consignments", "Sending saved consignments.");

			//updateProgress(DataSyncItems.size(), DataSyncItems.size()+1, "Completed", String.valueOf(successfullSyncNumber) + " of " + DataSyncItems.size() + ".");
			updateProgress("Sync", 5, 4, "Completed", String.valueOf(successfullSyncNumber) + " of " + DataSyncItems.size() + ".");
			updateProgress("Sync",5, 5, "Completed", String.valueOf(successfullSyncNumber) + " of " + DataSyncItems.size() + ".");

		}

	}

    protected boolean FinalizeConsignmentSynchronization(String runSheetID, int status)
            throws JSONException, ClientProtocolException, IOException,
            JSONNullableException {
        HttpPost postRequest = new HttpPost(
                ServiceUrls.FinalizeConsignmentSynchonizationUrl(context));

        postRequest.setHeader("Content-Type", "application/json");

        JSONStringer completedJson = new JSONStringer();

        String authToken = Service.loginService.LoginModel
                .getAuthToken();

        completedJson.object();
//		completedJson.key(CompletedConsignmentsEnum.AuthToken.Value).value(
//				authToken);
//		completedJson.key(CompletedConsignmentsEnum.RunSheetID.Value).value(
//				runSheetID);
//		completedJson.key(CompletedConsignmentsEnum.Status.Value).value(status);
        completedJson.endObject();

        StringEntity loginEntity = new StringEntity(completedJson.toString());
        postRequest.setEntity(loginEntity);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(postRequest);
        HttpEntity responseEntity = httpResponse.getEntity();

        if (responseEntity != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseEntity.getContent()));

            String temp = "";
            StringBuilder responseStringBuilder = new StringBuilder();

            while ((temp = br.readLine()) != null) {
                responseStringBuilder.append(temp);
            }

            String response = responseStringBuilder.toString();

            boolean success = setBooleanValueFromJSON(
                    JsonResponseEnum.IsSucess.Value,
                    getJSONData(response).getJSONObject(
                            MessageEnum.Message.Value));

            if (!success)
                return false;
        }

        return true;
    }

    private boolean syncDeliver(SyncAssignmentModel model, int count, int step) throws JSONException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
    {
        updateProgress("Sync", count, step, "Syncing assignments", "Sending assignment data.");
        String url = ServiceUrls.getSyncStartUrl(context).replace("authtokenvalue", model.getAuthToken());
        JSONStringer requestJson = new JSONStringer().object()
                .key(SyncAssignmentEnum.AuthToken.Value).value(model.getAuthToken())
                .key(SyncAssignmentEnum.Id.Value).value(model.getId())
                .key(SyncAssignmentEnum.tripStatus.Value).value(model.getTripStatus())
                .key(SyncAssignmentEnum.departureTime.Value).value(model.getDepartureTime())
                .key(SyncAssignmentEnum.estimatedTime.Value).value(model.getArrivalTime())
                .endObject();
        return post(url, requestJson.toString());
    }


    private SyncAssignmentModel createSyncStartModel(AssignmentModel assignmentModel)
    {
        SyncAssignmentModel model = new SyncAssignmentModel();

//		ArrayList<GalleryModel> ConsignmentImages = Database.getImagesForUnit(context, assignmentModel.getUnitId());
//
//		String[] imagePaths = new String[ConsignmentImages.size()];
//		for (int i = 0; i < ConsignmentImages.size(); i++)
//			imagePaths[i] = ConsignmentImages.get(i).getImagePath();
//
//		model.setImage(assignmentModel.getUnitId());
//		model.setAuthToken(Service.LoginService.LoginModel.getAuthToken());
//		model.setColCode(assignmentModel.getColCode());
//		model.setConsignmentReference(assignmentModel.getConsignmentReference());
//		//		model.setCustomerReference(assignmentModel.getCustomerReference());
//		model.setCustomerUsername(assignmentModel.getReceiverName());
//		model.setDateSigned(assignmentModel.getDateSigned());
//		model.setDeliveryCode(assignmentModel.getDeliveryCode());
//		model.setImages(imagePaths);
//		model.setPodStatus(assignmentModel.getPodStatus());
//		model.setTripStatus(assignmentModel.getTripStatus());
//		model.setSignatureImagePath(assignmentModel.getReceiverSignature());
//		model.setSigned(assignmentModel.isSigned());
//		model.setSignedBy(assignmentModel.getReceiverName());
//		model.setEstimatedTime(assignmentModel.getEstimatedTime());
//		model.setDepartureTime(assignmentModel.getDepartureTime());
//		model.setSaved(assignmentModel.isSaved());
        return model;
    }

    private boolean sync(SyncAssignmentModel model, int count, int step) throws JSONException, ClientProtocolException, IOException, JSONNullableException, EmptyStringException
    {
        updateProgress("Sync", count, step, "Syncing assignments", "Sending assignment data.");
        String url = ServiceUrls.getSyncStartUrl(context);
        JSONStringer requestJson = new JSONStringer();
        requestJson.object();
        requestJson.key(SyncAssignmentEnum.AuthToken.Value).value(model.getAuthToken());
        requestJson.key(SyncAssignmentEnum.Id.Value).value(model.getId());
        requestJson.key(SyncAssignmentEnum.ColCode.Value).value(model.getColCode());
        requestJson.key(SyncAssignmentEnum.ConsignmentReference.Value).value(model.getConsignmentReference());
        requestJson.key(SyncAssignmentEnum.CustomerReference.Value).value(model.getCustomerReference());
        requestJson.key(SyncAssignmentEnum.CustomerUsername.Value).value(model.getCustomerUsername());
        requestJson.key(SyncAssignmentEnum.DateSigned.Value).value(model.getDateSigned());
        requestJson.key(SyncAssignmentEnum.DeliveryCode.Value).value(model.getDeliveryCode());
        requestJson.key(SyncAssignmentEnum.PodStatus.Value).value(model.getPodStatus());
        requestJson.key(SyncAssignmentEnum.tripStatus.Value).value(model.getTripStatus());
        requestJson.key(SyncAssignmentEnum.SignedBy.Value).value(model.getSignedBy());
        requestJson.key(SyncAssignmentEnum.Signed.Value).value(model.isSigned());
        requestJson.key(SyncAssignmentEnum.estimatedTime.Value).value(model.getArrivalTime());
        requestJson.key(SyncAssignmentEnum.departureTime.Value).value(model.getDepartureTime());
        requestJson.key(SyncAssignmentEnum.Saved.Value).value(model.isSaved());
        requestJson.endObject();

        boolean success = post(url, requestJson.toString());
        if (!success)
            return false;

        int i = 0;
        for (String imagePath : model.getImages())
        {
            updateProgress("Sync", count, step, "Syncing consignments", "Sending assignmentService images " + ++i + " of " + model.getImages().length + ".");

            int lastIndex = -1;
            if ((lastIndex = getLastIndexOfSlash(imagePath)) != -1)
            {
                String imageName = imagePath.substring(lastIndex, imagePath.length() - 4);
                String url2 = ServiceUrls.GetConsignmentPhotosServiceUrl(context)
                        .replace("consignmentidvalue", model.getId())
                        .replace("authtokenvalue", model.getAuthToken())
                        .replace("imageidvalue", imageName);
                if (!postImage(url2, imagePath))
                    return false;
            }
        }

        int lastIndex = -1;
        if ((lastIndex = getLastIndexOfSlash(model.getSignatureImagePath())) != -1)
        {

            updateProgress("Sync",1, 1, "Syncing consignments", "Sending customer signatureService.");
            String imageName = model.getSignatureImagePath().substring(lastIndex, model.getSignatureImagePath().length() - 4);
            String url3 = ServiceUrls.GetConsignmentSignatureUploadServiceUrl(context)
                    .replace("consignmentidvalue", model.getId())
                    .replace("imageidvalue", imageName)
                    .replace("authtokenvalue", model.getAuthToken());
            if (!postImage(url3, model.getSignatureImagePath()))
                return false;
        }
        return success;
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
}
