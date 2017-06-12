package com.encore.piano.service;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.encore.piano.R;
import com.encore.piano.services.LoginService;
import com.encore.piano.services.MessageService;
import com.encore.piano.business.PreferenceUtility;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.exceptions.*;

import org.json.JSONException;

public class MessagingService extends Service{

	
	PendingIntent PIntent;
	Notification notification;
//	
//	Messenger clientMessanger = null;
//	Messenger localMessenger = new Messenger(new InternalHandler());
	
	String AuthToken = "";
	boolean check = false;
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		
		
		check = true;		
		
			try {
				if(ServiceUtility.loginService == null)
					ServiceUtility.loginService = new LoginService(getApplicationContext());
				
				if(ServiceUtility.loginService.CheckLoginStatus())
					AuthToken = ServiceUtility.loginService.LoginModel.getAuthToken();
				else
					throw new LoginException();
				
			} catch (UrlConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONNullableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NetworkStatePermissionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LoginException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(!AuthToken.equals("")){
			PreferenceUtility.GetPreferences(getApplicationContext());
			CheckForMessages();		
			return super.onStartCommand(intent, flags, startId);			
		}
		else
			this.stopSelf();
		
		return -1;
	}
	
//	@Override
//	public IBinder onBind(Intent intent) {
//		check = true;
//		return localMessenger.getBinder();
//	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		
		
		return super.onUnbind(intent);
	}
	
	private void CheckForMessages()
	{		
		if(ServiceUtility.loginService.CheckLoginStatus())
			new FetchMessagesInLoop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		else
			this.stopSelf();
	}
	
	class FetchMessagesInLoop extends AsyncTask<Void, Void, Object>
	{

		
		@Override
		protected Object doInBackground(Void... params) {
			
			try {
				if(check){
					Thread.sleep(30000 + new Random().nextInt(100));
					if(ServiceUtility.messageService == null)
						ServiceUtility.messageService = new MessageService(getApplicationContext());
					
					ServiceUtility.messageService.CheckForNewMessages();
					ServiceUtility.messageService.SendUnsentMessages();
				}
			} catch (UrlConnectionException e) {
				return e;
			} catch (JSONException e) {
				return e;
			} catch (JSONNullableException e) {
				return e;
			} catch (NotConnectedException e) {
				return e;
			} catch (NetworkStatePermissionException e) {
				return e;
			} catch (DatabaseInsertException e) {
				return e;
			} 
			catch (InterruptedException e) {
				return e;
			} catch (EmptyStringException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessageSendingUnsuccessfull e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if(check){
				Log.d("MESSAGE SERVICE", "CHECK");
				if(result instanceof Exception){
				
				}
				else{	
					if(ServiceUtility.messageService.NumberOfNewMessages != 0)
						CreateServiceNotification();
				}
				new FetchMessagesInLoop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
			}
			super.onPostExecute(result);
		}
	}
	
	
	public void CreateServiceNotification(){
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("AndroidPOD Message")
		        .setAutoCancel(true)
		        .setDefaults(Notification.DEFAULT_ALL)
		        .setContentText("New messages received.");
		
		Intent resultIntent = new Intent(this, com.encore.piano.activities.Message.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);		

		PIntent = PendingIntent.getActivity(getApplicationContext(), ServiceUtility.NOTIFICATION_REQUESTCODE, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		mBuilder.setContentIntent(PIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = mBuilder.build();
		
		mNotificationManager.notify(ServiceUtility.NOTIFICATION_ID, notification);
		
	}


	@Override
	public void onDestroy() {
		check = false;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

//	public void SendMessageToClient(Message message)
//	{
//		try {
//			clientMessanger.send(message);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	class InternalHandler extends Handler
//	{
//		@Override
//		public void handleMessage(Message msg) {
//		
//			if(clientMessanger == null && msg.what != Messages.REGISTER_CLIENTMESSENGER)
//				return;
//			
//			switch(msg.what)
//			{
//				case Messages.REGISTER_CLIENTMESSENGER:
//					clientMessanger = msg.replyTo;
//					Message m = Message.obtain(null, Messages.CLIENTMESSENGER_REGISTERED);					
//					SendMessageToClient(m);
//					break;			
//				case Messages.START_MESSAGE_FETCHING:
//					AuthToken = msg.getData().getString(Messages.AUTH_TOKEN);
//					StartMessageCheckLooping();
//					break;
//				default:
//					break;
//			}
//			super.handleMessage(msg);
//		}
//	}
}
