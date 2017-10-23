package com.encore.piano.activities;

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
import com.encore.piano.server.Service;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.StringUtility;

public class AssignmentProgress extends AppCompatActivity {
	private TextView tvOrderType;
	private TextView tvDeliveryAddress;
	private TextView tvEstimatedArrivalTime;
	private TextView tvStartTime;
	private TextView tvTripStatus;
	private Button btnOK;
	private String consignmentId;
	private AssignmentModel assignmentModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_progress);

		tvOrderType = (TextView) findViewById(R.id.tvOrderType);
		tvDeliveryAddress = (TextView) findViewById(R.id.tvDeliveryAddress);
		tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEstimatedArrivalTime = (TextView) findViewById(R.id.tvEstimatedArrivalTime);
        tvTripStatus = (TextView) findViewById(R.id.tvTripStatus);
		btnOK = (Button) findViewById(R.id.btnOK);

		consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
		assignmentModel = Service.assignmentService.getAll(consignmentId);

		tvOrderType.setText(assignmentModel.getOrderType());
		tvDeliveryAddress.setText(assignmentModel.getDeliveryAddress());
        tvTripStatus.setText(assignmentModel.getTripStatus());
        if(StringUtility.isNotNullOrEmpty(assignmentModel.getDepartureTime())) {
			tvStartTime.setText(DateTimeUtility.formatDateTimeToShow(DateTimeUtility.toDateTime(assignmentModel.getDepartureTime())));
		}
        if(StringUtility.isNotNullOrEmpty(assignmentModel.getEstimatedTime())) {
            tvEstimatedArrivalTime.setText(DateTimeUtility.formatDateTimeToShow(DateTimeUtility.toDateTime(assignmentModel.getEstimatedTime())));
        }
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
			AssignmentProgress.this.finish();
			Intent i = new Intent(AssignmentProgress.this, Assignment.class);
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
