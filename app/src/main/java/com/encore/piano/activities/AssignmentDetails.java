package com.encore.piano.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.data.NumberConstants;
import com.encore.piano.data.StringConstants;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.server.Service;
import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.util.Alerter;

public class AssignmentDetails extends AppCompatActivity implements
		OnClickListener {

	TextView tvAssignmentNumber;
	TextView tvDrivers;
	TextView tvPickupAddress;
	TextView tvDeliveryAddress;
	TextView tvOrderType;
	TextView tvUnitsCount;
	TextView tvCallerName;
	TextView tvCallerPhoneNumber;
	TextView tvCustomer;

	Button btnPickupDetails;
	Button btnDeliveryDetails;
	Button btnUnits;
	Button btnStart;
	Button btnProgress;

	ActionBar actionBar;

	// GoogleMap map = null;
	AssignmentModel model = null;
	String consignmentId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_detail);

		tvAssignmentNumber = (TextView) findViewById(R.id.tvAssignmentNumber);
		tvDrivers = (TextView) findViewById(R.id.tvDrivers);
		tvPickupAddress = (TextView) findViewById(R.id.tvPickupAddress);
		tvDeliveryAddress = (TextView) findViewById(R.id.tvDeliveryAddress);
		tvOrderType = (TextView) findViewById(R.id.tvOrderType);
		tvUnitsCount = (TextView) findViewById(R.id.tvUnitsCount);
		tvCallerName = (TextView) findViewById(R.id.tvCallerName);
		tvCallerPhoneNumber = (TextView) findViewById(R.id.tvCallerPhoneNumber);
		tvCustomer = (TextView) findViewById(R.id.tvCustomer);
		btnPickupDetails = (Button) findViewById(R.id.btnPickupDetails);
		btnDeliveryDetails = (Button) findViewById(R.id.btnDeliveryDetails);
		btnUnits = (Button) findViewById(R.id.btnUnits);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnProgress = (Button) findViewById(R.id.btnProgress);

		btnPickupDetails.setOnClickListener(this);
		btnDeliveryDetails.setOnClickListener(this);
		btnUnits.setOnClickListener(this);
		btnStart.setOnClickListener(this);
		btnProgress.setOnClickListener(this);

		InitializeWidgets();
	}

	private void InitializeWidgets()
	{
		PreferenceUtility.GetPreferences(this);
		consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
		model = Service.assignmentService.getAll(consignmentId);

		tvAssignmentNumber.setText(model.getConsignmentNumber());
		tvDrivers.setText(model.getDriverName());
		tvOrderType.setText(model.getOrderType());
		tvCallerName.setText(model.getCallerName());
		tvCallerPhoneNumber.setText(model.getCallerPhoneNumber());
		tvPickupAddress.setText(model.getPickupAddress());
		tvDeliveryAddress.setText(model.getDeliveryAddress());
		tvCustomer.setText(model.getCustomerCode() + " - " + model.getCustomerName());
		tvUnitsCount.setText(model.getNumberOfItems() + " piano(s)");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.assignment, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.backtoassignments:
			AssignmentDetails.this.finish();
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
			case R.id.btnPickupDetails:
				Intent i = new Intent(AssignmentDetails.this, AssignmentDetailsPickup.class);
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, model.getId());
				startActivity(i);
				break;
			case R.id.btnDeliveryDetails:
				i = new Intent(AssignmentDetails.this, AssignmentDetailsDelivery.class);
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, model.getId());
				startActivity(i);
				break;
			case R.id.btnUnits:
				i = new Intent(AssignmentDetails.this, AssignmentUnits.class);
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, model.getId());
				startActivity(i);
			break;
			case R.id.btnStart:
				i = new Intent(AssignmentDetails.this, TripStart.class);
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, model.getId());
				startActivityForResult(i, NumberConstants.REQUEST_CODE_START_TRIP );
				break;
			case R.id.btnProgress:
				i = new Intent(AssignmentDetails.this, AssignmentProgress.class);
				i.putExtra(StringConstants.INTENT_KEY_ASSIGNMENT_ID, consignmentId);
				startActivity(i);
			break;
			case R.id.btnComplete:
				break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == NumberConstants.REQUEST_CODE_START_TRIP && resultCode == RESULT_OK)
		{
            Alerter.success(this, "Trip started successfully.");
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
}
