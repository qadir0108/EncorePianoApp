package com.encore.piano.activities;

import android.bluetooth.BluetoothSocket;
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
import com.encore.piano.print.BlueToothDeviceList;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;
import com.encore.piano.util.ZingCoder;

import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
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

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    String message;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assignment_print);

        try {
            consignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
            model = Service.assignmentService.getAll(consignmentId);
            Service.unitService = new UnitService(AssignmentPrint.this);
            ArrayList units = Service.unitService.getUnitsByOrderId(consignmentId);
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
        imgQRCode.setImageBitmap(ZingCoder.generateQRCode(this, model.getAssignmentNumber()));
		tvCustomer.setText("Customer: " + model.getCustomerCode() + " " + model.getCustomerName());

//		tvMakeModel.setText("Make/Model: " + modelUnit.getMake() + " " + modelUnit.getModel());
//		tvSerialNumber.setText("Serial: " + modelUnit.getSerialNumber());
//		tvBench.setText("Benches: " + (modelUnit.isBench() ? "W/B" : "N/B"));

        message = "Assignment #: " + model.getAssignmentNumber() + "\n\n";
        message += "\nCustomer: " + model.getCustomerCode() + " " + model.getCustomerName();
//        message += "\nMake/Model: " + modelUnit.getMake() + " " + modelUnit.getModel();
//        message += "\nSerial: " + modelUnit.getSerialNumber();
//        message += "\nBenches: " + (modelUnit.isBench() ? "W/B" : "N/B");
        message += "\n\n";

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
                //new BlueToothPrinter().sendData(AssignmentPrint.this, message);
                //Alerter.success(AssignmentPrint.this, message);
                connect();
            }
        });
	}

    protected void connect() {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), BlueToothDeviceList.class);
            this.startActivityForResult(BTIntent, BlueToothDeviceList.REQUEST_CONNECT_BT);
        }
        else{

            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btoutputstream = opstream;
            print_bt();

        }

    }


    private void print_bt() {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            btoutputstream = btsocket.getOutputStream();

            byte[] printformat = { 0x1B, 0x21, FONT_TYPE };
            btoutputstream.write(printformat);
            btoutputstream.write(message.getBytes());
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BlueToothDeviceList.getSocket();
            if(btsocket != null){
                print_bt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
