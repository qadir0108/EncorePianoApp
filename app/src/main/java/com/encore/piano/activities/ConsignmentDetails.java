package com.encore.piano.activities;

import java.util.Date;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.business.PreferenceUtility;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.model.ConsignmentModel;

public class ConsignmentDetails extends AppCompatActivity implements
		OnClickListener {

	TextView ConsNumber;
	TextView ColCode;
	TextView DelCode;
	TextView JobTypeValue;
	TextView Items;
	TextView TripStatus;
	TextView Instructions;

	Button viewItemsButton;
	Button changeStatusButton;

	ActionBar actionBar;

	// GoogleMap map = null;

	ConsignmentModel consignmentModel = null;
	String consignmentId;
	private Button startButton;
	private Button infoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consignmentdetail);

		//actionBar = getActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//actionBar.setDisplayHomeAsUpEnabled(true);

		ConsNumber = (TextView) findViewById(R.id.idValue);
		ColCode = (TextView) findViewById(R.id.colCodeValue);
		DelCode = (TextView) findViewById(R.id.delCodeValue);
		JobTypeValue = (TextView) findViewById(R.id.tvJobTypeValue);
		Items = (TextView) findViewById(R.id.itemsValue);
		TripStatus = (TextView) findViewById(R.id.tripStatusValue);
		Instructions = (TextView) findViewById(R.id.instructionsValue);
		viewItemsButton = (Button) findViewById(R.id.viewItemsButton);
		changeStatusButton = (Button) findViewById(R.id.signButton);
		startButton = (Button) findViewById(R.id.startButton);
		infoButton = (Button) findViewById(R.id.infoButton);

		infoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				Intent i = new Intent(ConsignmentDetails.this, ConsignmentInfo.class);
				i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, consignmentId);
				startActivity(i);

			}
		});

		viewItemsButton.setOnClickListener(this);
		changeStatusButton.setOnClickListener(this);
		startButton.setOnClickListener(this);

		InitializeWidgets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.topmenu_consignment, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId()) {
		case R.id.backtorunsheet:
			ConsignmentDetails.this.finish();
			break;
		//			case R.id.changestatussinglemenuitem:
		//				onChangeStatusOptionsClicked();
		//				break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// @Override
	// protected void onStop() {
	// new UpdateCurrentLocation().cancel(true);
	// super.onStop();
	// }
	//
	// @Override
	// protected void onStart() {
	// if(canceled == true){
	// canceled = false;
	// new
	// UpdateCurrentLocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
	// (Void[])null);
	// }
	// super.onStart();
	// }

	@Override
	public void onClick(View v)
	{

		switch (v.getId()) {
		case R.id.viewItemsButton:
			Intent i = new Intent(ConsignmentDetails.this,
					Items.class);
			i.putExtra(ServiceUtility.ITEM_INTENT_KEY,
					consignmentModel.getId());
			startActivity(i);
			break;
		case R.id.signButton:
			onChangeStatusOptionsClicked();
			break;
		case R.id.startButton:

			if (consignmentModel.getTripStatus() == null)
			{
				//ServiceUtility.FirebaseMessageService.SetTripStatus(consignmentModel.getId(), "startedfrombutton");
				String formatedDate = ServiceUtility.DateFormatStr.format(new Date());
				ServiceUtility.consignmentService.SaveArrivalTime(consignmentId, formatedDate, ""); // save arrival time on pressing start button
				startButton.setEnabled(false);
			}

			break;
		default:
			break;
		}
	}

	private void onChangeStatusOptionsClicked()
	{
		PreferenceUtility.GetPreferences(this);

		if (consignmentModel.getOrderType().equals(ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY))
		{
			// status now set automatically on settting arrival time
			if (consignmentModel.getTripStatus() != TripStatusEnum.Started.Value) // if already started then just go to departure time
			{
				Intent i = new Intent(ConsignmentDetails.this, DepartureTime.class);
				i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, consignmentId);
				startActivityForResult(i, 0);
			}
			else
			{
				Intent i = new Intent(ConsignmentDetails.this, DeliveryKind.class);
				i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, consignmentId);
				startActivityForResult(i, ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE);
			}
		}
		else
		{

			Intent i = new Intent(ConsignmentDetails.this, CustomerCaptureSignature.class);
			i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_USERNAME, "unknown");

			i.putExtra(ServiceUtility.CUSTOMER_TRIP_STATUS, "D");
			i.putExtra(ServiceUtility.CUSTOMER_POD_STATUS, "R"); //  succesful recvd

			i.putExtra(ServiceUtility.CUSTOMER_ADDRESS, consignmentModel.getDeliveryAddress());
			i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_FILENAME, consignmentModel.getId());
			startActivityForResult(i, ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE);

		}
		/*	Intent i2 = new Intent(ConsignmentDetails.this,
						com.androidpodui.activities.ConsignmentStatusManagement.class);
				i2.putExtra(ServiceUtility.ITEM_INTENT_KEY, consignmentModel.getId());
				startActivityForResult(i2,
						ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE);
		*/
	}

	private void InitializeWidgets()
	{

		PreferenceUtility.GetPreferences(this);

		consignmentId = getIntent().getExtras().getString(
				ServiceUtility.CONSIGNMENT_INTENT_KEY);

		consignmentModel = ServiceUtility.consignmentService
				.GetConsignmentById(consignmentId);

		ConsNumber.setText(consignmentModel.getConsignmentNumber());
		ColCode.setText(consignmentModel.getPickupAddress());
		DelCode.setText(consignmentModel.getDeliveryAddress());
        JobTypeValue.setText(consignmentModel.getOrderType());

		if (consignmentModel.isSaved())
			changeStatusButton.setVisibility(View.GONE);

		String JobTypeStr = "";
		if (consignmentModel.getOrderType().equals(null) || consignmentModel.getOrderType().equals(""))
			JobTypeStr = ServiceUtility.JOB_TYPE_NORMAL_DELIVERY_STR;
		else
		{
			if (consignmentModel.getOrderType().equals(ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY))
				JobTypeStr = ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY_STR;
			else
				JobTypeStr = ServiceUtility.JOB_TYPE_NORMAL_DELIVERY_STR;
		}
		JobTypeValue.setText(JobTypeStr);

		// hide for normal delivery
		if (consignmentModel.getOrderType() != ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY)
			startButton.setVisibility(View.INVISIBLE);

		if (consignmentModel.getTripStatus() == TripStatusEnum.Started.Value) //if set then hide
			startButton.setEnabled(false);

		Items.setText(String.valueOf(consignmentModel.getNumberOfItems())
				+ " piano(s)");
		TripStatus.setText(consignmentModel.getTripStatus());
		Instructions.setText(consignmentModel.getSpecialInstructions());

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE && resultCode == RESULT_OK)
		{
			InitializeWidgets();
		}

		setResult(resultCode);
		finish();

		super.onActivityResult(requestCode, resultCode, data);
	}
}
