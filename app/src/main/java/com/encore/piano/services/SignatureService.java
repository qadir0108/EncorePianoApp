package com.encore.piano.services;

import java.io.BufferedReader;
import java.io.File;
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

import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.MessageEnum;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.interfaces.ProgressUpdateListener;
import com.encore.piano.model.BaseModel;
import com.encore.piano.model.ConfirmationModel;
import com.encore.piano.model.ConfirmationModel.ConfirmationModelEnum;
import com.encore.piano.model.ProgressUpdateModel;
import com.encore.piano.model.SignatureModel;
import com.encore.piano.model.SignatureModel.SignatureEnum;

public class SignatureService extends BaseService {

	public String SignatureAbsolutePath = "";
	public String ImageId = "";

	String taskName;

	ProgressUpdateListener OnProgressUpdate;
	ProgressUpdateModel updateModel = new ProgressUpdateModel();

	public SignatureService(Context context)
			throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void RegisterProgressUpdateListener(ProgressUpdateListener listener)
	{
		OnProgressUpdate = listener;
	}

	public void UnRegisterProgressUpdateListener()
	{
		OnProgressUpdate = null;
	}

	private void UpdateProgressListener(int count, int step, String title, String message)
	{
		updateModel.setTaskName(taskName);
		updateModel.setItemsCount(count);
		updateModel.setStep(step);
		updateModel.setTitle(title);
		updateModel.setMessage(message);

		if (OnProgressUpdate != null)
			OnProgressUpdate.OnProgressUpdateListener(updateModel);
	}

	public String SendSignature() throws JSONException, ClientProtocolException, IOException, EmptyStringException, JSONNullableException
	{

		UpdateProgressListener(4, 1, "Signing in", "Initializing signatureService data.");

		SignatureModel SignatureModel = new SignatureModel();
		SignatureModel.setLoginTime();
		SignatureModel.setUsername();
		SignatureModel.setGuid();
		SignatureModel.setAuthToken();

		HttpPost postRequest = new HttpPost(ServiceUrls.GetSignaturesUrl(context));

		postRequest.setHeader("Content-Type", "application/json");

		JSONStringer loginJson = new JSONStringer();
		loginJson.object();
		//loginJson.key(SignatureEnum.Signature.Value).value(SignatureModel.getSignature());
		loginJson.key(SignatureEnum.LoginTime.Value).value(SignatureModel.getLoginTime());
		loginJson.key(SignatureEnum.Guid.Value).value(SignatureModel.getGuid());
		loginJson.key(SignatureEnum.AuthToken.Value).value(SignatureModel.getAuthToken());

		loginJson.key(SignatureEnum.CheckListItems.Value);
		loginJson.array();

		for (ConfirmationModel confirmation : ServiceUtility.confirmationService.Confirmations)
		{
			loginJson.object();
			loginJson.key(ConfirmationModelEnum.Id.Value).value(confirmation.getId());
			loginJson.key(ConfirmationModelEnum.Condition.Value).value(confirmation.getCondition());
			loginJson.key(ConfirmationModelEnum.Confirmed.Value).value(confirmation.isConfirmed());
			loginJson.endObject();
		}

		loginJson.endArray();

		loginJson.endObject();

		StringEntity loginEntity = new StringEntity(loginJson.toString());
		postRequest.setEntity(loginEntity);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);

		UpdateProgressListener(4, 2, "Signing in", "Sending signatureService data.");

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

			if (response.equals(EMPTY_STRING))
				throw new EmptyStringException();
			else
			{
				JSONObject object = getJSONData(response);
				String messageJson = setStringValueFromJSON(MessageEnum.Message.Value, object);

				boolean success = setBooleanValueFromJSON(JsonResponseEnum.IsSucess.Value, getJSONData(messageJson));

				if (!success)
					return setStringValueFromJSON(JsonResponseEnum.ErrorMessage.Value, getJSONData(messageJson));
			}
		}
		else
			throw new NullPointerException();

		UpdateProgressListener(4, 3, "Signing in", "Sending signatureService.");

		String url = ServiceUrls.GetSignatureUploadServiceUrl(context)
				.replace("idimagevalue", ImageId)
				.replace("signatureidvalue", SignatureModel.getGuid())
				.replace("authtokenvalue", SignatureModel.getAuthToken());

		if (!UploadImage(url, SignatureAbsolutePath))
			return "Upload error.";

		UpdateProgressListener(4, 4, "Send Signature Completed", "Signing successfull");

		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
		}

		// delete signatureService image after upload
		File f = new File(SignatureAbsolutePath);
		if (f.exists())
			f.delete();

		return EMPTY_STRING;
	}

	@Override
	public URL getServiceUrl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent() throws JSONException
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
	public <T extends BaseModel> T DecodeContent(JSONObject object)
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
