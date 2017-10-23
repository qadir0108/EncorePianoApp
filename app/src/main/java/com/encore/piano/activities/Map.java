package com.encore.piano.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.encore.piano.R;
import com.encore.piano.db.AssignmentDb;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.server.Service;
import com.encore.piano.server.GPSTrackingService;
import com.encore.piano.server.GpxService;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.interfaces.OnLocationUpdateListener;
import com.encore.piano.model.GPSTrackingModel;
import com.encore.piano.model.GpxTrackModel;
import com.encore.piano.service.GPSTrackingService.GPSBinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends AppCompatActivity implements OnLocationUpdateListener, ServiceConnection, OnMapReadyCallback {

	GoogleMap map = null;
	PolylineOptions recordedPOptions = null;
	MarkerOptions userLocation = null;
	private Object isBound;
	public ArrayList<LatLng> polyz;
	private ArrayList<AssignmentModel> consignments;
	public ArrayList<ArrayList<LatLng>> allPaths = new ArrayList<ArrayList<LatLng>>();
	LatLng fromPosition = new LatLng(33.887383, -118.024426);
	LatLng toPosition = new LatLng(33.919307, -118.009348);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maplayout);

		((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		consignments = AssignmentDb.getAll(this, false, true, "");
		//	getConsignmentLocations(consignments);

		Log.d("LOGIN_ACTIVITY", "ON_CREATE METHOD");
		new GetDirection(this).execute();
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
		switch (item.getItemId())
		{
		case R.id.main:
			onMainClicked();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onMainClicked()
	{
		this.finish();

		Intent i = new Intent(this, com.encore.piano.activities.StartScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}

	public void onRunSheetClicked()
	{
		this.finish();

		Intent i = new Intent(this, Assignment.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);

	}

	@Override
	public void onMapReady(GoogleMap mp)
	{
        map = mp;
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

		//map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//		MapFragment mapFragment = (MapFragment) getFragmentManager()
//				.findFragmentById(R.id.map);
//		mapFragment.getMapAsync(this);

		DrawUserLocationOnMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		userLocation = new MarkerOptions();
		userLocation.position(fromPosition);
		userLocation.title("My current location");
		userLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation));
		map.addMarker(userLocation);
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 16));

		// GPS COORDINATES RECEIVED FROM GPX FILE
		PolylineOptions pOptions = new PolylineOptions();
		LatLng initialCameraPosition = null;

		try
		{
			if (Service.gpxService == null)
			{
				Service.gpxService = new GpxService(this);
				Service.gpxService.Initialize();
			}

			for (int i = 0; i < Service.gpxService.GpxTracks.size(); i++)
			{

				GpxTrackModel trackModel = Service.gpxService.GpxTracks.get(i);

				if (i == 0)
					initialCameraPosition = new LatLng(trackModel.getLatitude(), trackModel.getLongitude());

				pOptions.add(new LatLng(trackModel.getLatitude(), trackModel.getLongitude()));

				// ADDING DESTINATION MARKERS (LAST LOCATION IN TRACK SEGMENT)

				if (i == (Service.gpxService.GpxTracks.size() - 1))
				{
					MarkerOptions mOptions = new MarkerOptions();
					mOptions.draggable(false);

					mOptions.position(new LatLng(trackModel.getLatitude(), trackModel.getLongitude()));
					mOptions.title(trackModel.getName());

					map.addMarker(mOptions).showInfoWindow();
				}
				else if (!Service.gpxService.GpxTracks.get(i).getName().equals(Service.gpxService.GpxTracks.get(i + 1).getName()))
				{
					MarkerOptions mOptions = new MarkerOptions();
					mOptions.draggable(false);

					mOptions.position(new LatLng(trackModel.getLatitude(), trackModel.getLongitude()));
					mOptions.title(trackModel.getName());

					map.addMarker(mOptions).showInfoWindow();
				}

			}
			pOptions.color(Color.parseColor("#FF0000"));
			map.addPolyline(pOptions);

			if (initialCameraPosition != null)
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(initialCameraPosition, 13));

			DrawUserLocationOnMap();

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
		} catch (DatabaseInsertException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void DrawUserLocationOnMap()
	{

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
	public void OnLocationUpdate(Location l)
	{
		//		DrawUserLocationOnMap();

	}

	@Override
	protected void onStop()
	{
		//	if (isBound)
		//	unbindService(this);
		super.onStop();
	}

	@Override
	protected void onStart()
	{
		Intent i = new Intent(this,
				com.encore.piano.service.GPSTrackingService.class);
		isBound = getApplicationContext().bindService(i, this, android.app.Service.BIND_AUTO_CREATE);
		super.onStart();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		GPSBinder binder = (GPSBinder) service;
		com.encore.piano.service.GPSTrackingService ss = binder.getService();
		ss.RegisterListener(this);
		Log.d("CONSIGNMENT", "BINED TO GPS SERVICE");
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0)
	{
	}

	private void getConsignmentLocations(ArrayList<AssignmentModel> consignments)
	{
		for (AssignmentModel c : consignments)
			c.setPickupLocation(getLocationFromAddress(c.getDeliveryAddress()));
	}

	private LatLng getLocationFromAddress(String strAddress)
	{
		Geocoder coder = new Geocoder(this);
		List<Address> address;
		LatLng l = null;

		try
		{
			address = coder.getFromLocationName(strAddress, 5);

			if (address == null || address.size() == 0)
			{
				return null;
			}
			Address location = address.get(0);
			location.getLatitude();
			location.getLongitude();

			l = new LatLng(location.getLatitude(), location.getLongitude());

			//		p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
			//				(int) (location.getLongitude() * 1E6));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;

	}

	class GetDirection extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;
		Random rnd = new Random();
		LatLngBounds.Builder bc = new LatLngBounds.Builder();
		private Activity context;

		public GetDirection(Map map) {
			context = map;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			pDialog = new ProgressDialog(Map.this);
			pDialog.setMessage("Loading route. Please wait...");
			pDialog.setIndeterminate(false);
			//pDialog.setCancelable(false);
			pDialog.getWindow().setGravity(Gravity.BOTTOM);
			pDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			pDialog.show();
		}

		protected String doInBackground(String... args)
		{
			try
			{
				getConsignmentLocations(consignments);

				Intent i = getIntent();
				for (AssignmentModel c : consignments)
				{
					if (c.getPickupLocation() == null)
						continue;

					String stringUrl = "http://maps.googleapis.com/maps/services/directions/json?origin=" + fromPosition.latitude + "," + fromPosition.longitude + "&destination=" + c.getPickupLocation().latitude + "," + c.getPickupLocation().longitude + "&sensor=false";
					StringBuilder response = new StringBuilder();

					URL url = new URL(stringUrl);
					HttpURLConnection httpconn = (HttpURLConnection) url
							.openConnection();
					if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
					{
						BufferedReader input = new BufferedReader(
								new InputStreamReader(httpconn.getInputStream()),
								8192);
						String strLine = null;

						while ((strLine = input.readLine()) != null)
						{
							response.append(strLine);
						}
						input.close();
					}

					String jsonOutput = response.toString();

					JSONObject jsonObject = new JSONObject(jsonOutput);

					// routesArray contains ALL routes
					JSONArray routesArray = jsonObject.getJSONArray("routes");
					// Grab the first route
					JSONObject route = routesArray.getJSONObject(0);

					JSONObject poly = route.getJSONObject("overview_polyline");
					String polyline = poly.getString("points");
					polyz = decodePoly(polyline);
					drawPath(c, polyz);
					allPaths.add(polyz);

				}

			}

			catch (Exception e)
			{
				e.printStackTrace();
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			return null;

		}

		private void drawPath(AssignmentModel con, ArrayList<LatLng> g)
		{
			int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

			final MarkerOptions mark = new MarkerOptions();
			mark.position(g.get(g.size() - 1));
			//	mark.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
			mark.title(con.getDeliveryAddress());
			mark.snippet(con.getPickupAddress());
			//		map.addMarker(mark);

			//	Runnable addMarker = new AddMarker(map, mark);
			//	context.runOnUiThread(addMarker);
			context.runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					map.addMarker(mark);
				}
			});

			final PolylineOptions options = new PolylineOptions().width(10).color(color).geodesic(true);

			for (int i = 0; i < g.size(); i++)
			{
				LatLng src = g.get(i);
				//map.addPolyline(new PolylineOptions()
				//		.add(new LatLng(src.latitude, src.longitude),
				//				new LatLng(dest.latitude, dest.longitude))
				//		.width(10).color(color).geodesic(true));
				bc.include(src);

				options.add(src);
			}
			//			map.addPolyline(options);
			context.runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					map.addPolyline(options);

				}
			});
		}

		protected void onPostExecute(String file_url)
		{

			if (file_url != null)
			{
				map.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
				//		map.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 13));
			}
			pDialog.dismiss();

		}
	}

	/* Method to decode polyline points */
	private ArrayList<LatLng> decodePoly(String encoded)
	{

		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len)
		{
			int b, shift = 0, result = 0;
			do
			{
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do
			{
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}
}
