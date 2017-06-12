package com.encore.piano.activities;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;

public class DateTimePicker extends Activity {

	private Button okBtn;
	private DatePicker datePicker;
	private TimePicker timePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datetimepicker);
		
		okBtn = (Button) findViewById(R.id.btn_ok);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
			
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				String formatedDate = ServiceUtility.DateFormatStr.format(new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute()));

				Intent i = new Intent();
				i.putExtra("dtime", formatedDate);
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
	}

}
