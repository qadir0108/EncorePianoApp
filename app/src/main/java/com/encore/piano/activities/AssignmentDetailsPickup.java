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

public class AssignmentDetailsPickup extends AppCompatActivity {
	private ActionBar actionBar;
	private TextView tvPickupDate;
	private TextView tvPickupAddress;
	private TextView tvPickupPhone;
	private TextView tvPickupAlternateContact;
	private TextView tvPickupAlternatePhone;
	private TextView tvPickupStairs;
	private TextView tvPickupTurns;
	private TextView tvPickupInstructions;
	private Button btnOK;
	private String consignmentId;
	private AssignmentModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_detail_pickup);

		tvPickupDate = (TextView) findViewById(R.id.tvPickupDate);
		tvPickupAddress = (TextView) findViewById(R.id.tvPickupAddress);
		tvPickupPhone = (TextView) findViewById(R.id.tvPickupPhone);
		tvPickupAlternateContact = (TextView) findViewById(R.id.tvPickupAlternateContact);
		tvPickupAlternatePhone = (TextView) findViewById(R.id.tvPickupAlternatePhone);
		tvPickupStairs = (TextView) findViewById(R.id.tvPickupStairs);
		tvPickupTurns = (TextView) findViewById(R.id.tvPickupTurns);
		tvPickupInstructions = (TextView) findViewById(R.id.tvPickupInstructions);
		btnOK = (Button) findViewById(R.id.btnOK);

		consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
		model = Service.assignmentService.getAll(consignmentId);

		tvPickupDate.setText(model.getPickupDate());
		tvPickupAddress.setText(model.getPickupAddress());
		tvPickupPhone.setText(model.getPickupPhoneNumber());
		tvPickupAlternateContact.setText(model.getPickupAlternateContact());
		tvPickupAlternatePhone.setText(model.getPickupAlternatePhone());
		tvPickupStairs.setText(model.getPickupNumberStairs());
		tvPickupTurns.setText(model.getPickupNumberTurns());
		tvPickupInstructions.setText(model.getPickupInstructions());

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
				AssignmentDetailsPickup.this.finish();
				Intent i = new Intent(AssignmentDetailsPickup.this, Assignment.class);
				startActivity(i);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
