package com.encore.piano.activities;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.asynctasks.SyncConsignment;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.model.ConsignmentModel;

public class DeliveryKind extends Activity {

	private RadioButton emptyChk;
	private RadioButton fullChk;
	private RadioButton customerRadio;
	private RadioButton wharfRadio;
	private RadioButton dehireRadio;
	private String consignmentId;
	private Button okBtn;
	private Button selectTime;
	private boolean arrivalTimeSelected = true;
	private boolean deptTimeSelected;
	private RadioButton depotRadio;
	ConsignmentModel consignmentModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deliverykind);

		emptyChk = (RadioButton) findViewById(R.id.rd_empty);
		fullChk = (RadioButton) findViewById(R.id.rd_full);
		customerRadio = (RadioButton) findViewById(R.id.rd_customer);
		wharfRadio = (RadioButton) findViewById(R.id.rd_wharf);
		dehireRadio = (RadioButton) findViewById(R.id.rd_dehire);
		depotRadio = (RadioButton) findViewById(R.id.rd_depot);
		okBtn = (Button) findViewById(R.id.btn_done);

		selectTime = (Button) findViewById(R.id.btn_selecttime);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			consignmentId = extras.getString(ServiceUtility.CONSIGNMENT_INTENT_KEY);
		}

		consignmentModel = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);

		if (consignmentModel.isSaved())
		{
			emptyChk.setEnabled(false);
			fullChk.setEnabled(false);
			customerRadio.setEnabled(false);
			wharfRadio.setEnabled(false);
			dehireRadio.setEnabled(false);
			depotRadio.setEnabled(false);

			selectTime.setText(consignmentModel.getArrivalTime());

			String pod = consignmentModel.getTripStatus();

			if (pod.startsWith("E"))
				emptyChk.setChecked(true);
			else if (pod.startsWith("F"))
				fullChk.setChecked(true);

			if (pod.endsWith("C"))
				customerRadio.setChecked(true);
			else if (pod.endsWith("W"))
				wharfRadio.setChecked(true);
			else if (pod.equals("DH"))
				dehireRadio.setChecked(true);
			else if (pod.equals("DE"))
				depotRadio.setChecked(true);

		}
		else
		{
			String formatedDate = ServiceUtility.DateFormatStr.format(new Date());
			selectTime.setText(formatedDate);
		}

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				String str = "B";// dummy code

				if (consignmentModel.getServiceType().equals(ServiceUtility.SERVICE_TYPE_IMPORT))
				{
					if (fullChk.isChecked() && depotRadio.isChecked()) // must be from wharf
						str = "F";
					else if (fullChk.isChecked() && customerRadio.isChecked()) // must be from depot, wharf
						str = "D";
					else if (emptyChk.isChecked() && depotRadio.isChecked()) // must be from customer
						str = "M";
					else if (emptyChk.isChecked() && dehireRadio.isChecked()) // must be from depot, customer
						str = "Z";
				}
				else if (consignmentModel.getServiceType().equals(ServiceUtility.SERVICE_TYPE_EXPORT))
				{
					if (emptyChk.isChecked() && depotRadio.isChecked()) // must be from empty park
						str = "Y";
					else if (emptyChk.isChecked() && customerRadio.isChecked()) // must be from depot, empty park
						str = "D";
					else if (fullChk.isChecked() && depotRadio.isChecked()) // must be from customer
						str = "X";
					else if (fullChk.isChecked() && wharfRadio.isChecked()) // must be from depot, customer
						str = "Z";
				}

				String arrivalStr = null;
				if (arrivalTimeSelected)
					arrivalStr = (String) selectTime.getText();

				ServiceUtility.consignmentService.SaveArrivalTime(consignmentId, consignmentModel.getTripStatus(), arrivalStr);

				if (consignmentModel.getServiceType().equals(ServiceUtility.SERVICE_TYPE_IMPORT) && str.equals("D")) // full at customer
				{
					Intent i = new Intent(DeliveryKind.this, CustomerCaptureSignature.class);
					i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_USERNAME, "unknown");

					i.putExtra(ServiceUtility.CUSTOMER_TRIP_STATUS, str);
					i.putExtra(ServiceUtility.CUSTOMER_POD_STATUS, "R"); // pass successful recvd status, exception set from exception screen

					ConsignmentModel consignmentModel = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);

					i.putExtra(ServiceUtility.CUSTOMER_ADDRESS, consignmentModel.getDeliveryAddress());
					i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_FILENAME, consignmentModel.getId());
					i.putExtra(ServiceUtility.ARRIVAL_TIME, arrivalStr);
					startActivityForResult(i, ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE);

				}
				else if (!depotRadio.isChecked()) // There should be no Departure Time screen for Depot. 
				{
					// set status when no sign reqd and driver set arrival time etc
					ServiceUtility.consignmentService.SetTripStatus(consignmentId, TripStatusEnum.Started.Value);

					// send arrival time to server 
					new SyncConsignment(DeliveryKind.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);

					Intent i = new Intent(DeliveryKind.this, DepartureTime.class);
					i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, consignmentId);
					i.putExtra(ServiceUtility.CUSTOMER_TRIP_STATUS, str);
					i.putExtra(ServiceUtility.ARRIVAL_TIME, arrivalStr);
					startActivityForResult(i, 0);
				}
				else
				{
					// in case of depot just save arrival time as departure time
					ServiceUtility.consignmentService.SaveDepartureTime(consignmentId, arrivalStr);

					// send arrival and dept time to server
					new SyncConsignment(DeliveryKind.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);
					finish();
				}

			}
		});

		final Intent i1 = new Intent(this, DateTimePicker.class);

		selectTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				startActivityForResult(i1, 1);
			}
		});

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
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK)
		{
			arrivalTimeSelected = true;
			selectTime.setText(data.getStringExtra("dtime"));
		}

		if (requestCode == ServiceUtility.CONSIGNEMNT_STATUS_MANAGEMENT_REQUEST_CODE && resultCode == RESULT_OK)
		{
			setResult(RESULT_OK);
			finish();
		}

		if (requestCode == 0)
		{
			setResult(RESULT_OK);
			finish();
		}
	}

}
