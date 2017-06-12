package com.encore.piano.services;

import android.content.Context;

import com.encore.piano.db.Database;
import com.encore.piano.enums.JsonResponseEnum;
import com.encore.piano.enums.LoginModelEnum;
import com.encore.piano.exceptions.*;
import com.encore.piano.interfaces.ProgressUpdateListener;
import com.encore.piano.model.BaseModel.ServerResponse;
import com.encore.piano.model.MessageModel;
import com.encore.piano.model.MessageModel.MessageEnum;
import com.encore.piano.model.MessageModel.MessageFolderEnum;
import com.encore.piano.model.ProgressUpdateModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MessageService extends BaseService {

	public int NumberOfNewMessages;
	public String ErrorMessage = EMPTY_STRING;
	JSONArray array = null;
	Context context;
	String Folder = EMPTY_STRING;
	
	ProgressUpdateListener updateListener;
	
	String taskName;
	
	public MessageService(Context context) throws UrlConnectionException,
			JSONException, JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {
		super(context);
		this.context = context;
	}
	
	public void RegisterProgressUpdateListener(ProgressUpdateListener listener){
		updateListener = listener;
	}
	
	public void UnRegisterProgressUpdateListener(){
		updateListener = null;
	}
	
	public void UpdateProgressListener(int count, int step, String title, String message){
		ProgressUpdateModel model = new ProgressUpdateModel();
		model.setTaskName(taskName);
		model.setItemsCount(count);
		model.setStep(step);
		model.setMessage(message);
		model.setTitle(title);
		
		if(updateListener != null)
			updateListener.OnProgressUpdateListener(model);
	}
	
	public void CheckForNewMessages() throws 
		UrlConnectionException, 
		JSONException, 
		JSONNullableException, 
		NotConnectedException, 
		NetworkStatePermissionException, 
		DatabaseInsertException
		{
			UpdateProgressListener(3, 1, "Message synchronization", "Synchronizing inbox messages");
			Folder = MessageFolderEnum.Inbox.Value;
			initialize();
			
			UpdateProgressListener(3, 2, "Message synchronization", "Synchronizing sent messages");
			Folder = MessageFolderEnum.Sent.Value;
			initialize();
			
			UpdateProgressListener(3, 3, "Message synchronization", "Completed");
			
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}

	
	public MessageModel SendMessage(MessageModel model) throws 
	MessageSendingException, 
	JSONException, 
	ClientProtocolException, 
	IOException, JSONNullableException, EmptyStringException, MessageSendingUnsuccessfull
	{
		UpdateProgressListener(3, 1, "Message sending", "Preparing data");
		HttpPost postRequest = new HttpPost(ServiceUrls.GetMessageCommitUrl(context));
		
		postRequest.setHeader("Content-Type", "application/json");

		JSONStringer messageJson = new JSONStringer()
			.object()
				.key(LoginModelEnum.AuthToken.Value).value(ServiceUtility.loginService.LoginModel.getAuthToken())
				.key(MessageEnum.Recipient.Value).value(model.getRecipient())
				.key(MessageEnum.Subject.Value).value(model.getSubject())
				.key(MessageEnum.MessageText.Value).value(model.getMessageText())
			.endObject();
			
		StringEntity loginEntity = new StringEntity(messageJson.toString());
		postRequest.setEntity(loginEntity);
		
		UpdateProgressListener(3, 2, "Message sending", "Sending message");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(postRequest);
		
		if(httpResponse.getStatusLine().getStatusCode() != 200)
			throw new MessageSendingException();
		
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
				JSONObject object = getJSONData(response);
				boolean success = setBooleanValueFromJSON(
						JsonResponseEnum.IsSucess.Value,
						object.getJSONObject(com.encore.piano.enums.MessageEnum.Message.Value));
				
				if(success){
					UpdateProgressListener(3, 3, "Message sending", "Completed.");
				}
				else
					throw new MessageSendingUnsuccessfull();
			}
		}
		else
			throw new NullPointerException();
		
		model.setFolder(MessageFolderEnum.Sent.Value);
		return model;
	}
	
	public void SendUnsentMessages() throws JSONNullableException, EmptyStringException, MessageSendingUnsuccessfull
	{
		ArrayList<MessageModel> unsentMessages = Database.GetUnsentMessages(context);
		
		for (MessageModel messageModel : unsentMessages) {
			try {
				
				MessageModel sentModel = SendMessage(messageModel);
				Database.MarkMessageAsSent(context, sentModel.getID());
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (MessageSendingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public MessageModel GetMessageById(String messageId)
	{
		MessageModel m = Database.GetMessageById(context, messageId); 
		if(m == null)
			throw new NullPointerException();
		else
			return m;
	}

	public ArrayList<MessageModel> GetMyConversations(String UserName)
	{
		return Database.GetMyConversations(context,UserName);
	}
	
	public ArrayList<MessageModel> GetAllMessagesByUser(String UserId)
	{
		return Database.GetAllMessagesByUser(context,UserId);
	}
	
	public ArrayList<MessageModel> GetReceivedMessages()
	{
		return Database.GetReceivedMessages(context);
	}
	
	public ArrayList<MessageModel> GetUnsentMessages()
	{
		return Database.GetUnsentMessages(context);
	}
	
	public ArrayList<MessageModel> GetSentMessages()
	{
		return Database.GetSentMessages(context);
	}
	
//	public void MarkMessageAsRead(int messageId)
//	{
//		Database.MarkMessageAsRead(context, messageId);
//	}
	
	public void WriteMessage(MessageModel model) throws DatabaseInsertException
	{
		if(Database.WriteMessage(context, model) == -1)
			throw new DatabaseInsertException();
	}
	
	@Override
	public URL getServiceUrl() {
		String url = ServiceUrls.GetMessageFeedUrl(context).replace("authtokenvalue", ServiceUtility.loginService.LoginModel.getAuthToken())
												.replace("messagefolder", Folder);
		
		return getURLFromString(url);
	}

	@Override
	public synchronized void setContent() throws JSONException, DatabaseInsertException {
		
		ArrayList<MessageModel> messages = new ArrayList<MessageModel>();
		for(int i = 0; i < array.length(); i++){
			messages.add(DecodeContent(array.getJSONObject(i)));
		}
		
		//messages = StaticData.GetMessages();
		
		int noOfMessages = Database.WriteMessages(context, messages);
		NumberOfNewMessages = noOfMessages;
	}

	@Override
	public void fetchContent() throws UrlConnectionException, JSONException,
			JSONNullableException, NotConnectedException,
			NetworkStatePermissionException {

		ServerResponse result = getFromServer(getServiceUrl(), "Messages");
		ErrorMessage = result.getErrorMessage();
		array = result.getJsonArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public MessageModel DecodeContent(JSONObject object) {

		MessageModel model = new MessageModel();
		model.setID(setStringValueFromJSON(MessageEnum.ID.Value, object));
		model.setMessageText(setStringValueFromJSON(MessageEnum.MessageText.Value, object));
		model.setRecipient(setStringValueFromJSON(MessageEnum.Recipient.Value, object));
		model.setSender(setStringValueFromJSON(MessageEnum.Sender.Value, object));
		model.setFolder(Folder);
		model.setSubject(setStringValueFromJSON(MessageEnum.Subject.Value, object));
		model.setTimestamp(StringToDate(setStringValueFromJSON(MessageEnum.Timestamp.Value, object), "dd.MM.yyyy HH:mm"));
		
		return model;
	}

	public void setTaskName(String taskName) {
		// TODO Auto-generated method stub
		this.taskName = taskName;
	}

}
