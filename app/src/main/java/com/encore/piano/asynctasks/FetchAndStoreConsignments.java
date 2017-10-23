package com.encore.piano.asynctasks;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.encore.piano.R;
import com.encore.piano.activities.Assignment;
import com.encore.piano.server.AssignmentService;
import com.encore.piano.server.Service;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.util.CommonUtility;

public class FetchAndStoreConsignments extends AsyncTask<AsyncParams, Void, String>
{

	Context context;
    int notificationId;
    String Id;

	@Override
	protected void onPreExecute()
	{

		super.onPreExecute();
	}

	@Override
	protected String doInBackground(AsyncParams... params)
	{
		try
		{
			context = params[0].getContext();
            notificationId = params[0].getNotificationId();
            Id = params[0].getId();
            Service.assignmentService = new AssignmentService(context);
            Service.assignmentService.LoadConsignments(Id);

			/*UpdateFetchingProgressDialog(5, 4, "Loading", "Fetching GPS data...");
			Service.GpxService = new GpxService(StartScreen.this);
			Service.GpxService.Initialize();*/

			//			if(Service.GPSTrackingService == null)
			//				 Service.GPSTrackingService = new GPSTrackingService(ConsignmentBroadcastReceiver.context);

		} catch (UrlConnectionException e)
		{
			return "Urlexc";
		} catch (JSONException e)
		{
			return "JSONexc";
		} catch (JSONNullableException e)
		{
			return "JSONNULLexc";
		} catch (NotConnectedException e)
		{
			return "NotConnexc";
		} catch (NetworkStatePermissionException e)
		{
			return "Networkexc";
		} catch (DatabaseInsertException e)
		{
			return "DatabaseInsertexc";
		}

		return "Sucess";
	}

	@Override
	protected void onPostExecute(String result)
	{

		if (result != null && !result.equals(""))
		{
			if (result.equals(Service.AUTH_TOKEN_INVALID))
			{
				Service.loginService.LogOff();
				Intent i = new Intent(context, com.encore.piano.activities.Login.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
			else
			{
				// update notification
				UpdateNotification();

				// send broadcast to update UI
				SendUpdateUIBroadCast();
				
			}
		}

		super.onPostExecute(result);

	}

	private void UpdateNotification()
	{

		int icon = R.drawable.ic_menu_copy;
		String title = "New Assignment!";
		String message = "New/Updated Assignment is available";
		Intent notificationIntent = new Intent(context, Assignment.class);

		CommonUtility.showNotification(context, notificationIntent, notificationId, icon, title, message);

	}

	private void SendUpdateUIBroadCast() {
		
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(CommonUtility.ACTION_UPDATE_CONSIGNMENTS);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		context.sendBroadcast(broadcastIntent);
		
	}


}
