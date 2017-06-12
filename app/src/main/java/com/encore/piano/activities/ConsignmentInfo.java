package com.encore.piano.activities;

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
import com.encore.piano.model.ConsignmentModel;

public class ConsignmentInfo extends AppCompatActivity {
	private ActionBar actionBar;
	private TextView startTime;
	private TextView jobType;
	private TextView destination;
	private TextView arrival_time;
	private TextView departureTime;
	private TextView tripStatus;
	private Button btnOK;
	private String consignmentId;
	private ConsignmentModel consignmentModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consignmentinfo);

		//actionBar = getActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//actionBar.setDisplayHomeAsUpEnabled(true);

		startTime = (TextView) findViewById(R.id.startTime);
		jobType = (TextView) findViewById(R.id.jobType);
		destination = (TextView) findViewById(R.id.destination);
		arrival_time = (TextView) findViewById(R.id.arrival_time);
		departureTime = (TextView) findViewById(R.id.departureTime);
		tripStatus = (TextView) findViewById(R.id.tripStatus);
		btnOK = (Button) findViewById(R.id.btnOK);

		consignmentId = getIntent().getExtras().getString(ServiceUtility.CONSIGNMENT_INTENT_KEY);

		consignmentModel = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);

		startTime.setText(consignmentModel.getArrivalTime());
		jobType.setText(consignmentModel.getOrderType());
		destination.setText(consignmentModel.getDeliveryAddress());
		arrival_time.setText(consignmentModel.getArrivalTime());
		departureTime.setText(consignmentModel.getDepartureTime());
		tripStatus.setText(consignmentModel.getTripStatus());

		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
					finish();	
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
			ConsignmentInfo.this.finish();
			Intent i = new Intent(ConsignmentInfo.this, Consignment.class);
			startActivity(i);

			break;
		//			case R.id.changestatussinglemenuitem:
		//				onChangeStatusOptionsClicked();
		//				break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
