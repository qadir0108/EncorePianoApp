package com.encore.piano.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.encore.piano.R;
import com.encore.piano.data.NumberConstants;
import com.encore.piano.server.LoginService;
import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.server.Service;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.LoginException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.exceptions.ValueValidationException;
import com.encore.piano.interfaces.OnLocationUpdateListener;
import com.encore.piano.model.GPSTrackingModel;

public class GPSTrackingService extends android.app.Service {

	PendingIntent PIntent;
	Notification notification;
	LocationManager gpsLocation;
	boolean gpsEnabled;
	//	
	//	Messenger clientMessanger = null;
	//	Messenger localMessenger = new Messenger(new InternalHandler());

	String AuthToken = "";
	boolean check = false;

	OnLocationUpdateListener onLocationUpdatedListener;

	GPSBinder gpsBinder = new GPSBinder();

	public class GPSBinder extends Binder {
		public GPSTrackingService getService() {
			return GPSTrackingService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("GPS SERVICE", "BINDED " + intent.getPackage());
		return gpsBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("GPS SERVCE", "UNBINDED " + intent.getPackage());
		onLocationUpdatedListener = null;

		return super.onUnbind(intent);
	}

	public void RegisterListener(OnLocationUpdateListener listener) {
		onLocationUpdatedListener = listener;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		check = true;

		try {
			if (Service.loginService == null)
				Service.loginService = new LoginService(getApplicationContext());

			if (Service.loginService.checkLoginStatus())
				AuthToken = Service.loginService.LoginModel.getAuthToken();
			else
				throw new LoginException();

		} catch (UrlConnectionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JSONNullableException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} catch (NetworkStatePermissionException e) {
			e.printStackTrace();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		if (!AuthToken.equals("")) {
			gpsLocation = (LocationManager) getSystemService(LOCATION_SERVICE);
			PreferenceUtility.GetPreferences(getApplicationContext());
			try {
				RecordCoordinates();
			} catch (ValueValidationException e) {
				Log.e("GPSTracking Service", "Preference value validation error");
			}
			new SyncGpsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			return super.onStartCommand(intent, flags, startId);
		} else
			this.stopSelf();

		return android.app.Service.START_STICKY_COMPATIBILITY;
	}

	private void RecordCoordinates() throws ValueValidationException {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}

		long minTimeInSecs = PreferenceUtility.GetPreferences(this).getGpsFrequency(); //5;

		if (Service.loginService.checkLoginStatus())

			gpsLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeInSecs * 1000 + 17, 50, new LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLocationChanged(Location location) {

					Log.d("GPS_SERVICE", "ON LOCATION CHANGED");

					if (Service.gpsTrackingService == null)
						Service.gpsTrackingService = new com.encore.piano.server.GPSTrackingService(getApplicationContext());

					if (location == null) {
						Log.d("GPS SERVICE", "location null");
						return;
					}

					if (location != null) {
						GPSTrackingModel model = new GPSTrackingModel();
						model.setLatitude(String.valueOf(location.getLatitude()));
						model.setLongitude(String.valueOf(location.getLongitude()));

						Log.d("GPS_SERVICE", "SAVING CHANGED LOCATION");
						Service.gpsTrackingService.SaveGpsCoordinates(model);

						//sync with server as onlocationchanged called after 15 min by default
						new SyncGpsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);

						if (onLocationUpdatedListener != null) {
							Log.d("GPS_SERVICE", "LOCATION UPDATE LISTENER CALL");
							onLocationUpdatedListener.OnLocationUpdate(location);
						}
					}

				}
			});
		else
			this.stopSelf();
	}

	class SyncGpsData extends AsyncTask<Void, Void, Object>
	{

		@Override
		protected Object doInBackground(Void... params)
		{

			try
			{
				if (check)
				{
					Thread.sleep(1);
					if (Service.gpsTrackingService == null)
						Service.gpsTrackingService = new com.encore.piano.server.GPSTrackingService(getApplicationContext());

					Service.gpsTrackingService.SyncGpsData();
				}
			} catch (InterruptedException e)
			{
				return e;
			} catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result)
		{
			if (check)
				Log.d("GPS SERVICE", "SYNC");
			//		new SyncGpsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			super.onPostExecute(result);
		}
	}

	public void CreateServiceNotification()
	{

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle("GPS status")
						.setContentText("GPS is turned off!");

		Intent resultIntent = new Intent(this, com.encore.piano.activities.StartScreen.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PIntent = PendingIntent.getActivity(getApplicationContext(), NumberConstants.REQUEST_CODE_NOTIFICATION, resultIntent, PIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(PIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NumberConstants.NOTIFICATION_ID, mBuilder.build());
	}

	@Override
	public void onDestroy()
	{
		check = false;
		super.onDestroy();
	}
}
