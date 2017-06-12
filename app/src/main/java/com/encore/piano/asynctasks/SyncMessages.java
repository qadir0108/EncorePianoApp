package com.encore.piano.asynctasks;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.encore.piano.R;
import com.encore.piano.activities.Conversation;
import com.encore.piano.services.MessageService;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.MessageSendingUnsuccessfull;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.util.CommonUtility;

public class SyncMessages extends AsyncTask<AsyncParams, Void, String>
{

	Context context;
	int notificationId;

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(AsyncParams... params) {
		
		context = params[0].getContext();

		try {
			if(ServiceUtility.messageService == null)
				ServiceUtility.messageService = new MessageService(context);
			
			ServiceUtility.messageService.setTaskName("Synchronizing Messages");
			//ServiceUtility.MessageService.RegisterProgressUpdateListener(Message.this);
			ServiceUtility.messageService.CheckForNewMessages();
			ServiceUtility.messageService.SendUnsentMessages();
		} catch (UrlConnectionException e) {
			return "UrlConnectionException";
		} catch (JSONException e) {
			return "JSONException";
		} catch (JSONNullableException e) {
			return "JSONNullableException" ;
		} catch (NotConnectedException e) {
			return "NotConnectedException" ;
		} catch (NetworkStatePermissionException e) {
			return "NetworkStatePermissionException";
		} catch (DatabaseInsertException e) {
			return "DatabaseInsertException";
		} catch (EmptyStringException e) {
			return "EmptyStringException";
		} catch (MessageSendingUnsuccessfull e) {
			return "MessageSendingUnsuccessfull";
		}
		
		return "Sucess";
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		if(result != null && !result.equals("")){
			if(result.equals(ServiceUtility.AUTH_TOKEN_INVALID)){
				ServiceUtility.loginService.LogOff();
				Intent i = new Intent(context, com.encore.piano.activities.Login.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				context.startActivity(i);
				}
			else
				{
				// Success
                    // update notification
                    UpdateNotification();

//						Intent i = new Intent(context, com.com.encore.piano.activities.Conversation.class);
//						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						context.startActivity(i);

				}
		}
		
		super.onPostExecute(result);
	}

	private void UpdateNotification() {
		
		int icon = R.drawable.ic_menu_compose;
    	String title = "New Message!";
        String message =  "New message is available";
    	Intent notificationIntent = new Intent(context, Conversation.class);
         
        CommonUtility.showNotification(context, notificationIntent, notificationId, icon, title, message);
        
	}

}