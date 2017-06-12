package com.encore.piano.activities;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.asynctasks.SyncConsignment;
import com.encore.piano.model.ConsignmentModel;

public class DepartureTime extends Activity {

	private Button selectTime1;
	private Button okBtn;
	private String consignmentId;
	ConsignmentModel consignmentModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deliverytime);

		selectTime1 = (Button) findViewById(R.id.btn_selecttime1);
		okBtn = (Button) findViewById(R.id.btn_done);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			consignmentId = extras.getString(ServiceUtility.CONSIGNMENT_INTENT_KEY);
		}

		String formatedDate = ServiceUtility.DateFormatStr.format(new Date());
		selectTime1.setText(formatedDate);

		final Intent i = new Intent(this, DateTimePicker.class);

		selectTime1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				startActivityForResult(i, 2);
			}
		});

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				setResult(RESULT_OK);
				finish();

				String departureStr = (String) selectTime1.getText();

				ServiceUtility.consignmentService.SaveDepartureTime(consignmentId, departureStr);
				new SyncConsignment(DepartureTime.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2 && resultCode == RESULT_OK)
		{
			selectTime1.setText(data.getStringExtra("dtime"));
		}
	}

}
