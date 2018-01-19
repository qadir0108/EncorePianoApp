package com.encore.piano.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.data.StringConstants;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.print.LablePrinter;
import com.encore.piano.server.AssignmentService;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;
import com.encore.piano.util.ZingCoder;

import org.json.JSONException;

import java.util.ArrayList;

public class AssignmentPrint extends AppCompatActivity {

	TextView tvAssignmentNumber;
	private ImageView imgQRCode;
	private TextView tvCustomer;
	private TextView tvMakeModel;
	private TextView tvSerialNumber;
	private TextView tvBench;
    private Button btnCancel;
    private Button btnPrint;

	private String consignmentId;
	private AssignmentModel model;
    private ArrayList units;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_print);

        try {
            consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);

            if(Service.assignmentService == null)
                Service.assignmentService = new AssignmentService(AssignmentPrint.this);
            model = Service.assignmentService.getAll(consignmentId);

            if(Service.unitService == null)
                Service.unitService = new UnitService(AssignmentPrint.this);
            units = Service.unitService.getUnitsByOrderId(model.getOrderId());

        } catch (UrlConnectionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JSONNullableException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (NetworkStatePermissionException e) {
            e.printStackTrace();
        } catch (DatabaseInsertException e) {
            e.printStackTrace();
        }

        tvAssignmentNumber = (TextView) findViewById(R.id.tvAssignmentNumber);
        imgQRCode = (ImageView)findViewById(R.id.imgQRCode);
		tvCustomer = (TextView) findViewById(R.id.tvCustomer);
		tvMakeModel = (TextView) findViewById(R.id.tvMakeModel);
		tvSerialNumber = (TextView) findViewById(R.id.tvSerialNumber);
		tvBench = (TextView) findViewById(R.id.tvBench);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnPrint = (Button) findViewById(R.id.btnPrint);

        tvAssignmentNumber.setText(model.getAssignmentNumber());
        //imgQRCode.setImageBitmap(ZingCoder.generateQRCode(this, model.getAssignmentNumber()));
		tvCustomer.setText("Customer: " + model.getCustomerCode() + " " + model.getCustomerName());

        btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
					finish();	
			}
		});

        btnPrint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0)
            {
                new LablePrinter(AssignmentPrint.this).print(model, units);
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
				AssignmentPrint.this.finish();
				Intent i = new Intent(AssignmentPrint.this, Assignment.class);
				startActivity(i);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
