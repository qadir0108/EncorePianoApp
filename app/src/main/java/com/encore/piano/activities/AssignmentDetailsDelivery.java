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
import com.encore.piano.data.StringConstants;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.server.Service;

public class AssignmentDetailsDelivery extends AppCompatActivity {
	private ActionBar actionBar;
	private TextView tvDeliveryDate;
	private TextView tvDeliveryAddress;
	private TextView tvDeliveryPhone;
	private TextView tvDeliveryAlternateContact;
	private TextView tvDeliveryAlternatePhone;
	private TextView tvDeliveryStairs;
	private TextView tvDeliveryTurns;
	private TextView tvDeliveryInstructions;
	private Button btnOK;
	private String consignmentId;
	private AssignmentModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_detail_delivery);

		tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
		tvDeliveryAddress = (TextView) findViewById(R.id.tvOrderType);
		tvDeliveryPhone = (TextView) findViewById(R.id.tvDeliveryAddress);
		tvDeliveryAlternateContact = (TextView) findViewById(R.id.tvEstimatedArrivalTime);
		tvDeliveryAlternatePhone = (TextView) findViewById(R.id.tvStartTime);
		tvDeliveryStairs = (TextView) findViewById(R.id.tvTripStatus);
		tvDeliveryTurns = (TextView) findViewById(R.id.tvDeliveryTurns);
		tvDeliveryInstructions = (TextView) findViewById(R.id.tvDeliveryInstructions);
		btnOK = (Button) findViewById(R.id.btnOK);

		consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);

		model = Service.assignmentService.getAll(consignmentId);

		tvDeliveryDate.setText(model.getDeliveryDate());
		tvDeliveryAddress.setText(model.getDeliveryAddress());
		tvDeliveryPhone.setText(model.getDeliveryPhoneNumber());
		tvDeliveryAlternateContact.setText(model.getDeliveryAlternateContact());
		tvDeliveryAlternatePhone.setText(model.getDeliveryAlternatePhone());
		tvDeliveryStairs.setText(model.getDeliveryNumberStairs());
		tvDeliveryTurns.setText(model.getDeliveryNumberTurns());
		tvDeliveryInstructions.setText(model.getDeliveryInstructions());

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
		getMenuInflater().inflate(R.menu.assignment, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.backtoassignments:
				AssignmentDetailsDelivery.this.finish();
				Intent i = new Intent(AssignmentDetailsDelivery.this, Assignment.class);
				startActivity(i);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
