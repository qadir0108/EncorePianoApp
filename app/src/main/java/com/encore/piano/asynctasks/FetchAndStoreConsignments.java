package com.encore.piano.asynctasks;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.encore.piano.R;
import com.encore.piano.activities.Consignment;
import com.encore.piano.services.ConsignmentService;
import com.encore.piano.services.ServiceUtility;
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
            ServiceUtility.consignmentService = new ConsignmentService(context);
            ServiceUtility.consignmentService.LoadConsignments(Id);

			/*UpdateFetchingProgressDialog(5, 4, "Loading", "Fetching GPS data...");
			ServiceUtility.GpxService = new GpxService(StartScreen.this);
			ServiceUtility.GpxService.Initialize();*/

			//			if(ServiceUtility.GPSTrackingService == null)
			//				 ServiceUtility.GPSTrackingService = new GPSTrackingService(ConsignmentBroadcastReceiver.context);

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
			if (result.equals(ServiceUtility.AUTH_TOKEN_INVALID))
			{
				ServiceUtility.loginService.LogOff();
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
		String title = "New Consignment!";
		String message = "New/Updated Consignment is available";
		Intent notificationIntent = new Intent(context, Consignment.class);

		CommonUtility.showNotification(context, notificationIntent, notificationId, icon, title, message);

	}

	private void SendUpdateUIBroadCast() {
		
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(CommonUtility.ACTION_UPDATE_CONSIGNMENTS);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		context.sendBroadcast(broadcastIntent);
		
	}


}
