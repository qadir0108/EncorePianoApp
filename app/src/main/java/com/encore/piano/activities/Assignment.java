package com.encore.piano.activities;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.encore.piano.R;
import com.encore.piano.afragments.AssignmentFragment;
import com.encore.piano.afragments.GoogleMapFragment;
import com.encore.piano.listview.assignment.AssignmentAdapter;
import com.encore.piano.server.Service;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.GPSTrackingModel;
import com.encore.piano.server.GPSTrackingService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import static com.encore.piano.util.CommonUtility.EXTRA_MESSAGE;
import static com.encore.piano.util.CommonUtility.FROM_NOTIFICATION;

public class Assignment extends AppCompatActivity implements ActionBar.TabListener, GoogleMapFragment.OnGoogleMapFragmentListener, OnMapReadyCallback {
	ListView consignmentListView;
	AssignmentAdapter adapter;

	Button showCompletedButton,
			showAllButton;

    public static ArrayList<String> consignmentIds = new ArrayList<String>();

	private ViewPager mViewPager;
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	private boolean isBound;
	PolylineOptions recordedPOptions = null;
	MarkerOptions userLocation = null;
	GoogleMap map = null;
	SupportMapFragment supportMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignments_pager);

		if (getIntent().getExtras() != null)
			if (getIntent().getExtras().containsKey(FROM_NOTIFICATION))
			{
				String RunsheetId = getIntent().getExtras().getString(EXTRA_MESSAGE);

                // KQ
				//new AckConsignment(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, RunsheetId);

				getIntent().removeExtra(EXTRA_MESSAGE);

			}

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();

		// Specify that the Home/Up button should not be enabled, since there is no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position)
			{
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++)
		{

			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
							.setText(mAppSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.main:
			onMainClicked();
			break;
		case R.id.map:
			onShowMapClicked();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onMainClicked()
	{
		this.finish();
		Intent i = new Intent(this, StartScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}

	public void onShowMapClicked()
	{
		this.finish();
		Intent i = new Intent(this, Map.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//		if (requestCode == Service.REQUEST_CODE_ASSIGNEMNT_DETAILS && resultCode == RESULT_OK)
		//	mViewPager.findViewWithTag(mViewPager.getCurrentItem());
		//		mAppSectionsPagerAdapter.notifyDataSetChanged();

        // KQ
//		if (requestCode == Service.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE_MULTI && resultCode == RESULT_OK)
//		{
//			if (consignmentIds != null)
//			{
//				for (String unitId : consignmentIds)
//				{
//					new SyncConsignment(this).executeOnExecutor(
//							AsyncTask.THREAD_POOL_EXECUTOR, unitId);
//				}
//			}
//		}
//
//		// reloads data, even if user click back button after save arrival time
//			if (resultCode == RESULT_OK)
//		mAppSectionsPagerAdapter.notifyDataSetChanged();

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(ActionBar.Tab arg0, FragmentTransaction arg1)
	{
		mViewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}

	private void DrawUserLocationOnMap() throws UrlConnectionException, JSONException, JSONNullableException, NotConnectedException, NetworkStatePermissionException
	{
		supportMap = GoogleMapFragment.newInstance();
		supportMap.getMapAsync(this);

		// RECORDED GPS DATA WITH GREEN LINE
		if (recordedPOptions == null)
			recordedPOptions = new PolylineOptions();
		Log.d("DrawUserLocationOnMap", "1");

		// ADDING SAVED USER GPS COORDINATES FROM DATABASE AND DRAWING PATH
		if (Service.gpsTrackingService == null)
			Service.gpsTrackingService = new GPSTrackingService(this);

		ArrayList<GPSTrackingModel> list = Service.gpsTrackingService.GetGPSCoordinates(false);
		for (GPSTrackingModel recordedGps : list)
			recordedPOptions.add(new LatLng(Double.parseDouble(recordedGps.getLatitude()), Double.parseDouble(recordedGps.getLongitude())));

		recordedPOptions.color(Color.parseColor("#00FF00"));
		map.addPolyline(recordedPOptions);
		Log.d("DrawUserLocationOnMap", "2");

		// ADDING CURRENT LOCATION MARKER
		GPSTrackingModel lastPosition = Service.gpsTrackingService.GetLastCoordinate();
		if (lastPosition != null && lastPosition.getLatitude() != null
				&& !lastPosition.getLatitude().equals("")
				&& lastPosition.getLongitude() != null
				&& !lastPosition.getLongitude().equals(""))
		{

			LatLng lp = new LatLng(Double.parseDouble(lastPosition.getLatitude()), Double.parseDouble(lastPosition.getLongitude()));

			if (userLocation == null)
			{
				userLocation = new MarkerOptions();

				userLocation.draggable(false);

				userLocation.position(lp);
				userLocation.title("My current location");
				userLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation));
				map.addMarker(userLocation);
			}
			else
				userLocation.position(lp);

			float zoom = map.getCameraPosition().zoom;
			if (zoom < 14)
				zoom = 14;
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(lp, zoom));
			Log.d("DrawUserLocationOnMap", "3");
		}

		//new UpdateCurrentLocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
	}

	@Override
	public void onMapReady()
	{

		runOnUiThread(new Runnable() {

			@Override
			public void run()
			{
				UpdateMap();
			}
		});
	}

	private void UpdateMap()
	{

		//		map = ((SupportMapFragment) getSupportFragmentManager()
		//				.findFragmentById(R.id.map)).getMap();

        supportMap = GoogleMapFragment.newInstance();
		supportMap.getMapAsync(this);

		// GPS COORDINATES RECEIVED FROM GPX FILE
		PolylineOptions pOptions = new PolylineOptions();
		LatLng initialCameraPosition = null;

        // KQ
//		try
//		{
//			if (Service.gpxService == null)
//			{
//				Service.gpxService = new GpxService(this);
//				Service.gpxService.Initialize();
//			}
//
//			for (int i = 0; i < Service.gpxService.GpxTracks.size(); i++)
//			{
//
//				GpxTrackModel trackModel = Service.gpxService.GpxTracks.get(i);
//
//				if (i == 0)
//					initialCameraPosition = new LatLng(trackModel.getLatitude(), trackModel.getLongitude());
//
//				pOptions.add(new LatLng(trackModel.getLatitude(), trackModel.getLongitude()));
//
//				// ADDING DESTINATION MARKERS (LAST LOCATION IN TRACK SEGMENT)
//
//				if (i == (Service.gpxService.GpxTracks.size() - 1))
//				{
//					MarkerOptions mOptions = new MarkerOptions();
//					mOptions.draggable(false);
//
//					mOptions.position(new LatLng(trackModel.getLatitude(), trackModel.getLongitude()));
//					mOptions.title(trackModel.getSize());
//
//					map.addMarker(mOptions).showInfoWindow();
//				}
//				else if (!Service.gpxService.GpxTracks.get(i).getSize().equals(Service.gpxService.GpxTracks.get(i + 1).getSize()))
//				{
//					MarkerOptions mOptions = new MarkerOptions();
//					mOptions.draggable(false);
//
//					mOptions.position(new LatLng(trackModel.getLatitude(), trackModel.getLongitude()));
//					mOptions.title(trackModel.getSize());
//
//					map.addMarker(mOptions).showInfoWindow();
//				}
//
//			}
//			pOptions.color(Color.parseColor("#FF0000"));
//			map.addPolyline(pOptions);
//
//			if (initialCameraPosition != null)
//				map.animateCamera(CameraUpdateFactory.newLatLngZoom(initialCameraPosition, 13));
//
//			DrawUserLocationOnMap();
//
//		} catch (UrlConnectionException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONNullableException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotConnectedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NetworkStatePermissionException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DatabaseInsertException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        runOnUiThread(new Runnable() {

            @Override
            public void run()
            {
                UpdateMap();
            }
        });
    }

	class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i)
		{
			Fragment fragment = new AssignmentFragment();
			Bundle args = new Bundle();
			args.putInt(AssignmentFragment.ARG_SECTION_NUMBER, i + 1);
			fragment.setArguments(args);
			return fragment;

		}

		// reqd for datasetchanged to work
		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

		@Override
		public int getCount()
		{
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			if (position == 0)
				return "To Do";
			else if (position == 1)
				return "Completed";
			else
				return "";
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			container.setTag(position);
			return super.instantiateItem(container, position);
		}

	}
}
