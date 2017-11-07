package com.encore.piano.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.encore.piano.data.StringConstants;
import com.encore.piano.server.Service;
import com.encore.piano.interfaces.CallBackHandler;
import com.encore.piano.R;
import com.encore.piano.server.AssignmentService;
import com.encore.piano.server.SyncService;
import com.encore.piano.server.GPSTrackingService;
import com.encore.piano.server.GpxService;
import com.encore.piano.server.LoginService;
import com.encore.piano.server.SignatureService;
import com.encore.piano.logic.TaskQueue;
import com.encore.piano.logic.TaskQueue.EnumTask;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.EmptyAuthTokenException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.interfaces.ProgressUpdateListener;
import com.encore.piano.model.ProgressUpdateModel;
import com.encore.piano.util.Alerter;
import com.encore.piano.util.CommonUtility;
import com.encore.piano.util.FileUtility;
import com.encore.piano.util.PermissionsUtility;

import static com.encore.piano.util.PermissionsUtility.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class StartScreen extends AppCompatActivity implements OnClickListener, ProgressUpdateListener {//, ServiceConnection{

	LinearLayout layoutProgress;
	TextView txtTitle = null;
	TextView txtText = null;
	ProgressBar progressBar = null;

	Button btnReload;
	Button btnSync;
	Button btnLogOff;

	Intent serviceIntentGPS;

	String myPath,
			fileName;

	boolean signatureSend,
			consignmentsFetched;
	private ListView listView;

	public List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startscreen);

        PermissionsUtility.checkAndRequestPermissions(this);

		layoutProgress = (LinearLayout) findViewById(R.id.layoutProgress);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtText = (TextView) findViewById(R.id.txtText);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		btnReload = (Button) findViewById(R.id.btnReload);
		btnSync = (Button) findViewById(R.id.btnSync);
		btnLogOff = (Button) findViewById(R.id.btnLogoff);
		listView = (ListView) findViewById(R.id.listView1);

		btnReload.setOnClickListener(this);
		btnSync.setOnClickListener(this);
		btnLogOff.setOnClickListener(this);

		serviceIntentGPS = new Intent(this, com.encore.piano.service.GPSTrackingService.class);

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("name", "Assignments");
		items.add(hm);

		HashMap<String, String> hm2 = new HashMap<String, String>();
		hm2.put("name", "Warehouse");
		items.add(hm2);

		//	ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

		SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.startscreen_row, new String[] { "name" }, new int[] { R.id.item_text });

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng)
			{
				if (position == 0)
				{
					Intent i = new Intent(StartScreen.this, Assignment.class);
					startActivity(i);
				}
                else if (position == 1)
				{
					Intent i3 = new Intent(StartScreen.this, Warehouse.class);
					startActivity(i3);
				}

			}
		});

		if (Service.loginService == null)
			try
			{
				Service.loginService = new LoginService(this);
			} catch (UrlConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONNullableException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NetworkStatePermissionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (Service.loginService.checkLoginStatus())
		{
			serviceIntentGPS.putExtra(StringConstants.AUTH_TOKEN, Service.loginService.LoginModel.getAuthToken());

			myPath = getIntent().getStringExtra("filePath");
			fileName = getIntent().getStringExtra("fileName");

			processQueue();

			//			if(fileName != null && filePath != null)
			//				new SendSignature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);

			//new FetchAndStoreConsignments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);

			//	Intent intent = new Intent();
			//	intent.setAction("POD_MESSAGE_C");
			//	intent.putExtra(CommonUtility.EXTRA_NOTIFICATION_ID, 123435);
			//	sendBroadcast(intent);

			//			if(!IsMyMessagingServiceRunning())
			//				startService(serviceIntentMessaging);
			//			
			//if(!IsMyGpsServiceRunning())
			startService(serviceIntentGPS);

		}
		else
		{
			Intent i = new Intent(getApplicationContext(), Login.class);
			startActivity(i);
			this.finish();
		}

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        //getMenuInflater().inflate(R.menu.loginService, menu);
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    listView.setEnabled(true);
                    FileUtility.prepareDirectories();
                } else {
                    Alerter.error(StartScreen.this, "Permissions are required to run the application!");
                    listView.setEnabled(false);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

	public void processQueue()
	{
		if (TaskQueue.getQueue().peek() != null)
		{
//			if (TaskQueue.getQueue().peek() == EnumTask.SendSignature)
//			{
//				if (fileName != null && filePath != null)
//				{
//					new SendSignature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
//					// Remove Signature Task and add Load Assignment
//					TaskQueue.getQueue().remove();
//					TaskQueue.getQueue().add(EnumTask.LoadConsignments);
//				}
//
//			}

			if (TaskQueue.getQueue().peek() == EnumTask.LoadConsignments)
			{
				new FetchAndStoreConsignments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
				// Remove Consignments Task and add Sync Message
				TaskQueue.getQueue().remove();
				//TaskQueue.getQueue().add(EnumTask.SyncMessages);

			}

//			// On LOG-OFF
//			if (TaskQueue.getQueue().peek() == EnumTask.SyncConsignments)
//			{
//				new SyncData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
//				// Remove Sync Task
//				TaskQueue.getQueue().remove();
//
//			}
//

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
		{
            case R.id.map:
			    onShowMapClicked();
			break;
            case R.id.btnSync:
			    onSyncOptionClicked();
			break;
            case R.id.btnLogoff:
			    onLogoffOptionClicked();
			break;

            default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onRunSheetClicked()
	{
		//this.finish();

		Intent i = new Intent(this, Assignment.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}

	public void onShowMapClicked()
	{
		//this.finish();

		Intent i = new Intent(this, com.encore.piano.activities.Map.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}

	public boolean IsMyGpsServiceRunning()
	{
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		for (RunningServiceInfo service : activityManager.getRunningServices(40))
		{
			if ("com.encore.piano.service.GPSTrackingService".equals(service.service.getClassName()))
				return true;
		}

		return false;
	}

	@Override
	protected void onDestroy()
	{
		Log.d("ACTIVITY_DESTROY", "StartScreen destroy method");
		//unbindService(this);

		stopService(serviceIntentGPS);

		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.btnReload:
			onConsignmentsReloadOptionClicked();
			break;
		case R.id.btnSync:
			onSyncOptionClicked();
			break;
		case R.id.btnLogoff:
			onLogoffOptionClicked();
			break;

		default:
			break;
		}

	}

	private void onConsignmentsReloadOptionClicked()
	{
		new FetchAndStoreConsignments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
	}

	public void onLogoffOptionClicked()
	{
		try
		{
			SyncService dsp = new SyncService(StartScreen.this);

			//			if (!dsp.AreAllConsignementsSynced()) {
			//				TaskQueue.getQueue().add(EnumTask.SyncConsignments);
			//				processQueue();
			//			}

			//			TaskQueue.getQueue().add(EnumTask.SyncMessages);
			//			processQueue();

			boolean m2 = stopService(serviceIntentGPS);

			SyncData sync = new SyncData();
			sync.handler = new LogOffHandler();
			sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);

			//Kill process
			//android.os.Process.killProcess(android.os.Process.myPid());

			//			} else
			//				Toast.makeText(
			//						StartScreen.this,
			//						"Save and synchronize all consignments before logging off.",
			//						Toast.LENGTH_LONG).show();

		} catch (UrlConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONNullableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkStatePermissionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onSyncOptionClicked()
	{
		//new SyncData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		TaskQueue.getQueue().add(EnumTask.LoadConsignments);
		processQueue();

	}

	SyncService dsp = null;
	boolean synced = false;

	@Override
	public void OnProgressUpdateListener(final ProgressUpdateModel model)
	{

		runOnUiThread(new Runnable() {

			@Override
			public void run()
			{
				if (model != null)
				{

					int stepPercentage = model.getStep() / model.getItemsCount() * 100;

					progressBar.setMax(model.getItemsCount());
					progressBar.setProgress(model.getStep());
					txtTitle.setText(model.getTaskName());
					txtText.setText(model.getTitle() + " - " + model.getMessage());

					if (stepPercentage == 100)
					{
						try
						{

							if (Service.signatureService != null)
								Service.signatureService.UnRegisterProgressUpdateListener();

							Thread.sleep(2000);

							processQueue();

						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			}
		});

	}

	private void ShowMessage(final String title, String message)
	{

		if (layoutProgress.getVisibility() != View.VISIBLE)
		{
			layoutProgress.setVisibility(View.VISIBLE);
		}

		txtTitle.setText(title);
		txtText.setText(message);

		progressBar.getProgressDrawable().setColorFilter(Color.RED, Mode.SRC_IN);
		CommonUtility.fadeOutView(StartScreen.this, layoutProgress);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (KeyEvent.KEYCODE_BACK == keyCode)
		{
			this.moveTaskToBack(true);
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);

	}

	class SendSignature extends AsyncTask<Void, Void, String>
	{
		String message = "";

		@Override
		protected void onPreExecute()
		{
			layoutProgress.setVisibility(View.VISIBLE);
			progressBar.getProgressDrawable().clearColorFilter();

			txtTitle.setText("Signing");
			txtText.setText("Initializing signing...");
			progressBar.setIndeterminate(false);
			progressBar.setMax(4);
			progressBar.setProgress(1);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params)
		{
			try
			{
				Service.signatureService = new SignatureService(StartScreen.this);
				Service.signatureService.SignatureAbsolutePath = myPath;
				Service.signatureService.ImageId = fileName;
				Service.signatureService.RegisterProgressUpdateListener(StartScreen.this);
				Service.signatureService.setTaskName("Sending Signature");
				message = Service.signatureService.SendSignature();

			} catch (ClientProtocolException e)
			{
				return "ClientProtocolException";
			} catch (JSONException e)
			{
				return "JSONException";
			} catch (IOException e)
			{

				return "No internet found";
			} catch (UrlConnectionException e)
			{
				return "UrlConnectionException";
			} catch (JSONNullableException e)
			{
				return "JSONNullableException";
			} catch (NotConnectedException e)
			{
				return "NotConnectedException";
			} catch (NetworkStatePermissionException e)
			{
				return "NetworkStatePermissionException";
			} catch (EmptyStringException e)
			{
				return "EmptyStringException";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			signatureSend = true;

			if (result != null && !result.equals(""))
				ShowMessage("Error in Sending Signature", result);
			else
			{
				try
				{
					if (consignmentsFetched)
					{
						progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.SRC_IN);
						CommonUtility.fadeOutView(StartScreen.this, layoutProgress);
					}

					Service.loginService.ActivateLoginData();
				} catch (EmptyAuthTokenException e)
				{
					ShowMessage("Error in Sending Signature", "doLogin session not saved. Error occured.");
				}

			}
		}

	}

	class FetchAndStoreConsignments extends AsyncTask<Void, Void, String>
	{

		boolean loaded = false;

		@Override
		protected void onPreExecute()
		{
			layoutProgress.setVisibility(View.VISIBLE);

			txtTitle.setText("Loading");
			txtText.setText("Consignments and map route.");
			progressBar.setIndeterminate(false);
			progressBar.setMax(5);
			progressBar.setProgress(1);

			progressBar.getProgressDrawable().clearColorFilter();

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params)
		{
			try
			{

				updateProgressBar(5, 1, "Loading", "Fetching assignments...");
				Service.assignmentService = new AssignmentService(StartScreen.this);
				Service.assignmentService.LoadConsignments();
				if (!Service.assignmentService.getErrorMessage().equals(""))
					return Service.assignmentService.getErrorMessage();

//				updateProgressBar(5, 2, "Loading", "Fetching items...");
//				Service.UnitService = new UnitService(StartScreen.this);
//				Service.UnitService.LoadItems();
//				if (!Service.UnitService.getErrorMessage().equals(""))
//					return Service.UnitService.getErrorMessage();

//				updateProgressBar(5, 3, "Loading", "Fetching Trip/Pod statuses...");
//				Service.tripService = new TripService(StartScreen.this);
//				Service.PodStatusProvider = new PodProvider(StartScreen.this);

				/*UpdateFetchingProgressDialog(5, 4, "Loading", "Fetching GPS data...");
				Service.GpxService = new GpxService(StartScreen.this);
				Service.GpxService.Initialize();*/

				if (Service.gpsTrackingService == null)
					Service.gpsTrackingService = new GPSTrackingService(StartScreen.this);

				updateProgressBar(5, 4, "Completed", "Data loading completed.");

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
				return "NotConnected";
			} catch (NetworkStatePermissionException e)
			{
				return "Networkexc";
			} catch (DatabaseInsertException e)
			{
				return "DatabaseInsertexc";
			}

			return null;
		}

		@Override
		protected void onPostExecute(String message)
		{
			consignmentsFetched = true;

			if (message != null && !message.equals(""))
			{
				if (message.equals(Service.AUTH_TOKEN_INVALID))
				{
					Service.loginService.LogOff();
					Intent i = new Intent(StartScreen.this, com.encore.piano.activities.Login.class);
					startActivity(i);
					StartScreen.this.finish();
				}
				ShowMessage("Error in Assignment Reload", message);
			}
			else
			{
				updateProgressBar(5, 5, "Finished", "Imported " + Service.assignmentService.numberOfImportedAssignments + " new assignments" +
						" and " + Service.assignmentService.numberOfImportedItems + " new units.");

                progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.SRC_IN);
                CommonUtility.fadeOutView(StartScreen.this, layoutProgress);
			}

			loaded = true;

			super.onPostExecute(message);

		}
	}

	private void updateProgressBar(final int count, final int step, final String title, final String message)
	{
		final String task = "Reloading Assignments";
		runOnUiThread(new Runnable() {

			@Override
			public void run()
			{

				int stepPercentage = step / count * 100;

				progressBar.setMax(count);
				progressBar.setProgress(step);
				txtTitle.setText(task);
				txtText.setText(title + " - " + message);

				if (stepPercentage == 100)
				{
					if (TaskQueue.getQueue().peek() == EnumTask.SyncConsignments)
					{

						// Remove Sync Task
						TaskQueue.getQueue().remove();
						StartScreen.this.finish();

					}

				}

			}
		});

	}

	class SyncData extends AsyncTask<Void, Integer, String>
	{
		public CallBackHandler handler;

		@Override
		protected void onPreExecute()
		{
			layoutProgress.setVisibility(View.VISIBLE);
			progressBar.getProgressDrawable().clearColorFilter();

			txtTitle.setText("Data synchronization");
			txtText.setText("Initializing synchronization.");
			progressBar.setIndeterminate(false);
			progressBar.setMax(5);
			progressBar.setProgress(0);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params)
		{

			try
			{
				dsp = new SyncService(StartScreen.this);
				dsp.RegisterProgressUpdateListener(StartScreen.this);
				dsp.setTaskName("Synchronizing Data");
				dsp.syncAll();
			} catch (ClientProtocolException e)
			{
				return "ClientProtocolException";
			} catch (IOException e)
			{
				return "IOException";
			} catch (JSONNullableException e)
			{
				return "JSONNullableException";
			} catch (UrlConnectionException e)
			{
				return "UrlConnectionException";
			} catch (JSONException e)
			{
				return "JSONException";
			} catch (NotConnectedException e)
			{
				return "NotConnectedException";
			} catch (NetworkStatePermissionException e)
			{
				return "NetworkStatePermissionException";
			} catch (EmptyStringException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{

			synced = true;
			dsp.UnregisterProgressUpdateListener();

			if (result != null && !result.equals(""))
			{
				ShowMessage("Error in Sync Data", result);
			}

			if (handler != null)
				handler.callBack(result);

			super.onPostExecute(result);
		}
	}

	class LogOffHandler implements CallBackHandler
	{

		@Override
		public void callBack(Object response)
		{
			try
			{
				if (Service.assignmentService == null)

					Service.assignmentService = new AssignmentService(StartScreen.this);

				//Service.FirebaseMessageService.DeleteFinishedConsignments();

				if (Service.gpxService == null)
					Service.gpxService = new GpxService(StartScreen.this);

				Service.gpxService.DeleteGpxTracks();

				Service.loginService.LogOff();

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run()
					{
						StartScreen.this.finish();
					}
				}, 2000);

			} catch (UrlConnectionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONNullableException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NetworkStatePermissionException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
//			} catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (DatabaseInsertException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
