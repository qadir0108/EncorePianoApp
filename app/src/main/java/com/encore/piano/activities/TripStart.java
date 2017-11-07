package com.encore.piano.activities;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

import com.encore.piano.R;
import com.encore.piano.asynctasks.SyncStart;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.data.StringConstants;
import com.encore.piano.server.Service;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.StringUtility;

public class TripStart extends AppCompatActivity implements OnClickListener {

	private Button btnDepartureTime;
	private Button btnEstimatedTime;
	private Button btnCancel;
	private Button btnOk;
	private String consignmentId;
	AssignmentModel model;

    final Calendar c1 = Calendar.getInstance();
    final Calendar c2 = Calendar.getInstance();
    private int mHour1, mMinute1;
    private int mHour2, mMinute2;

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_start);

        btnDepartureTime = (Button) findViewById(R.id.btnDepartureTime);
        btnEstimatedTime = (Button) findViewById(R.id.btnEstimatedTime);
        btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOk = (Button) findViewById(R.id.btnOk);
        btnDepartureTime.setOnClickListener(this);
        btnEstimatedTime.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
        model = Service.assignmentService.getAll(consignmentId);

        if(model.getTripStatus() == TripStatusEnum.NotStarted.Value) {
            c2.add(Calendar.MINUTE, mMinute1 + 30);
        } else {
            if(StringUtility.isNotNullOrEmpty(model.getDepartureTime()))
                c1.setTime(DateTimeUtility.toDateTime(model.getDepartureTime()));

            if(StringUtility.isNotNullOrEmpty(model.getEstimatedTime()))
                c2.setTime(DateTimeUtility.toDateTime(model.getEstimatedTime()));
        }

        mHour1 = c1.get(Calendar.HOUR_OF_DAY);
        mMinute1 = c1.get(Calendar.MINUTE);
        mHour2 = c2.get(Calendar.HOUR_OF_DAY);
        mMinute2 = c2.get(Calendar.MINUTE);
        btnDepartureTime.setText(DateTimeUtility.formatTimeToShow(c1.getTime()));
        btnEstimatedTime.setText(DateTimeUtility.formatTimeToShow(c2.getTime()));
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDepartureTime:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour1 = hourOfDay;
                                mMinute1 = minute;
                                c1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c1.set(Calendar.MINUTE, minute);
                                btnDepartureTime.setText(DateTimeUtility.formatTimeToShow(c1.getTime()));
                            }
                        }, mHour1, mMinute1, false);
                timePickerDialog.show();
                break;
            case R.id.btnEstimatedTime:
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour2 = hourOfDay;
                                mMinute2 = minute;
                                c2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c2.set(Calendar.MINUTE, minute);
                                btnEstimatedTime.setText(DateTimeUtility.formatTimeToShow(c2.getTime()));
                            }
                        }, mHour2, mMinute2, false);
                timePickerDialog2.show();
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnOk:
                String departureTime = DateTimeUtility.formatDateTime(c1.getTime());
                String estimatedTime = DateTimeUtility.formatDateTime(c2.getTime());
                Service.assignmentService.startTrip(consignmentId, departureTime, estimatedTime);
                new SyncStart(TripStart.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
