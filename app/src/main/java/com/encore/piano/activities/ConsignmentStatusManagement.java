package com.encore.piano.activities;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.asynctasks.SyncConsignment;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.ConsignmentModel;
import com.encore.piano.model.TripodModel;
import com.encore.piano.services.TripService;

public class ConsignmentStatusManagement extends Activity implements OnClickListener, OnItemSelectedListener {

	Spinner tripStatus;
	Spinner podStatus;

	TextView signedValue;
	TextView imagestaken;

	EditText signName;

	Button sign;
	Button viewCaptureImages;
	Button cancel;
	Button save;

	ActionBar actionBar;

	String consignmentId = "";
	ArrayList<String> consignmentIds = null;
	ConsignmentModel consignment = null;
	private Spinner exceptionReason;
	private ArrayList<TripodModel> tripStatuses;
    private ArrayList<TripodModel> podStatuses;
    private ArrayList<TripodModel> exceptionStatuses;
	private String tripS;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consignmentstatusmanagement);

//		actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayHomeAsUpEnabled(true);

		try
		{
			if (ServiceUtility.tripService == null)
				ServiceUtility.tripService = new TripService(this);

            // KQ
			tripStatuses = ServiceUtility.tripService.GetTripArrayStatuses();
			podStatuses = ServiceUtility.tripService.GetTripArrayStatuses();
			exceptionStatuses = ServiceUtility.tripService.GetExceptionStatuses();

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONNullableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkStatePermissionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseInsertException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UrlConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tripS = getIntent().getExtras().getString(ServiceUtility.CUSTOMER_TRIP_STATUS);

		if (getIntent().getExtras().getString(ServiceUtility.ITEM_INTENT_KEY) != null)
		{
			consignmentId = getIntent().getExtras().getString(ServiceUtility.ITEM_INTENT_KEY);
			consignment = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);
		}
		else
			consignmentIds = getIntent().getExtras().getStringArrayList(ServiceUtility.CONSIGNMENT_ID_LIST_KEY);

		tripStatus = (Spinner) findViewById(R.id.tripStatusSpinner);
		podStatus = (Spinner) findViewById(R.id.podStatusSpinner);
		exceptionReason = (Spinner) findViewById(R.id.reasonSpinner);

		signedValue = (TextView) findViewById(R.id.signedValueTextView);
		imagestaken = (TextView) findViewById(R.id.imagesTakenValueTextView);

		signName = (EditText) findViewById(R.id.signNameEditTExt);

		sign = (Button) findViewById(R.id.signButton);
		viewCaptureImages = (Button) findViewById(R.id.viewCaptureButton);
		cancel = (Button) findViewById(R.id.cancelStatusManagementButton);
		save = (Button) findViewById(R.id.saveStatusManagementButton);

        // KQ
//		ArrayAdapter<TripodModel> tripadapter = new ArrayAdapter<TripodModel>(this, android.R.layout.simple_spinner_item, tripStatuses);
//		tripadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		tripStatus.setAdapter(tripadapter);

		int tripPosition = 0;
        // KQ
//		if (consignmentService != null && consignmentService.getTripStatus() != null && !consignmentService.getTripStatus().equals(""))
//			for (int i = 0; i < tripStatuses.size(); i++)
//				if (tripStatuses.get(i).getCode().equals(consignmentService.getTripStatus()))
//				{
//					tripPosition = i;
//					i = tripStatuses.size();
//				}
		tripStatus.setSelection(tripPosition);

        // KQ
//		ArrayAdapter<TripodModel> podadapter = new ArrayAdapter<TripodModel>(this, android.R.layout.simple_spinner_item, podStatuses);
//		podadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		podStatus.setAdapter(podadapter);

		int podPosition = 0;
        // KQ
//		if (consignmentService != null && consignmentService.getPodStatus() != null && !consignmentService.getPodStatus().equals(""))
//			for (int i = 0; i < podStatuses.size(); i++)
//				if (podStatuses.get(i).getCode().equals(consignmentService.getPodStatus()))
//				{
//					podPosition = i;
//					i = podStatuses.size();
//				}
		podStatus.setSelection(podPosition);

        // KQ
//		ArrayAdapter<TripodModel> exceptionAdapter = new ArrayAdapter<TripodModel>(this, android.R.layout.simple_spinner_item, exceptionStatuses);
//		exceptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		exceptionReason.setAdapter(exceptionAdapter);

		if (consignment != null)
		{
			imagestaken.setText(String.valueOf(ServiceUtility.consignmentService.GetImagesCount(consignment.getId())));
			signName.setText(consignment.getCustomerName());
		}

		sign.setOnClickListener(this);
		viewCaptureImages.setOnClickListener(this);
		cancel.setOnClickListener(this);
		save.setOnClickListener(this);

		//	if (consignmentService != null && consignmentService.isSaved())
		//		DisableComponents(consignmentService);
		//	else
		{
			tripStatus.setOnItemSelectedListener(this);
			podStatus.setOnItemSelectedListener(this);

			SetSignedTextValueAndColor();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.topmenu_consignment_detail, menu);

		//	if (consignmentService != null && consignmentService.isSaved())
		//menu.getItem(0).setEnabled(false);
		//	save.setEnabled(false);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{

		switch (item.getItemId())
		{
		//			case R.id.consignmentsavemenuitem:
		//				onSaveOptionClicked();
		//				break;
		//			case android.R.id.home:
		//				ConsignmentStatusManagement.this.finish();
		//				break;
		case R.id.backtoconsignment:
			this.finish();
			break;
		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.signButton:
			String name = signName.getText().toString();
			if (name.equals(""))
				Toast.makeText(ConsignmentStatusManagement.this, "Please fill signer name.", Toast.LENGTH_SHORT).show();
			else
			{
				Intent i = new Intent(ConsignmentStatusManagement.this, CustomerCaptureSignature.class);
				i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_USERNAME, name);

				i.putExtra(ServiceUtility.CUSTOMER_TRIP_STATUS, ((TripodModel) tripStatus.getSelectedItem()).getCode());
				i.putExtra(ServiceUtility.CUSTOMER_POD_STATUS, ((TripodModel) podStatus.getSelectedItem()).getCode());

				if (consignmentIds == null)
				{
					i.putExtra(ServiceUtility.CUSTOMER_ADDRESS, consignment.getDeliveryAddress());
					i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_FILENAME, consignmentId);
					startActivityForResult(i, ServiceUtility.CUSTOMER_ACTIVITY_REQUEST_CODE);
				}
				else
				{
					i.putStringArrayListExtra(ServiceUtility.CUSTOMER_SIGN_LIST, consignmentIds);
					startActivityForResult(i, ServiceUtility.CUSTOMER_ACTIVITY_REQUEST_CODE_MULTI);
				}
			}
			break;
		case R.id.viewCaptureButton:
			Intent i = new Intent(ConsignmentStatusManagement.this, Gallery.class);

			if (consignmentIds != null)
			{
				i.putStringArrayListExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY_MULTI, consignmentIds);
				startActivityForResult(i, ServiceUtility.GALLERY_ACTIVITY_REQUEST_CODE_MULTI);
			}
			else
			{
				i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, consignment.getId());
				startActivityForResult(i, ServiceUtility.GALLERY_ACTIVITY_REQUEST_CODE);
			}
			break;
		case R.id.saveStatusManagementButton:
			onSaveOptionClicked();
			break;
		case R.id.cancelStatusManagementButton:
			setResult(RESULT_CANCELED);
			this.finish();
			break;
		default:
			break;
		}

	}

	private void onSaveOptionClicked()
	{
		String tripStatus = "F"; // for transfocus

		try
		{
			if (consignmentIds != null)
			{
				for (String consignmentId : consignmentIds)
				{
					ConsignmentModel consignmentModel = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);
					if (consignmentModel.getOrderType() == ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY)
						tripStatus = consignmentModel.getTripStatus();

					ServiceUtility.consignmentService.SaveConsignment(consignmentId, tripStatus, ((TripodModel) exceptionReason.getSelectedItem()).getCode(), new String[] {});
					new SyncConsignment(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);
				}
			}
			else
			{
				// if rapid, then get trip status from the value passed
				ConsignmentModel consignmentModel = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);
				if (consignmentModel.getOrderType() == ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY)
					tripStatus = tripS;

				ServiceUtility.consignmentService.SaveConsignment(consignment.getId(), tripStatus, ((TripodModel) exceptionReason.getSelectedItem()).getCode(), new String[] {});
				new SyncConsignment(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);
			}

			Toast.makeText(ConsignmentStatusManagement.this, "Changes saved successfully.", Toast.LENGTH_LONG).show();

			//	Intent i = new Intent(this, com.androidpodui.activities.Consignment.class);
			//setResult(ServiceUtility.FORWARD_TO_CONS_LIST);
			//	startActivity(i);
			setResult(RESULT_OK);
			ConsignmentStatusManagement.this.finish();
		} catch (DatabaseUpdateException e)
		{
			Toast.makeText(ConsignmentStatusManagement.this, "Error occured while saving consignmentService changes.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adView, View view, int position, long arg3)
	{

		switch (adView.getId())
		{
		case R.id.tripStatusSpinner:
			if (tripStatus.getSelectedItem().toString().startsWith("S"))
				SignLayoutEnabled(false);
			else
				SignLayoutEnabled(true);
			break;
		case R.id.podStatusSpinner:
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}

	private void SignLayoutEnabled(boolean status)
	{
		if (consignment != null && !consignment.isSaved())
		{
			podStatus.setEnabled(status);
			sign.setEnabled(status);
			signName.setEnabled(status);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		switch (requestCode)
		{
		case ServiceUtility.CUSTOMER_ACTIVITY_REQUEST_CODE:
			consignment = ServiceUtility.consignmentService.GetConsignmentById(consignmentId);
			SetSignedTextValueAndColor();
			break;
		case ServiceUtility.GALLERY_ACTIVITY_REQUEST_CODE:
			imagestaken.setText(String.valueOf(ServiceUtility.consignmentService.GetImagesCount(consignment.getId())));
			break;
		case ServiceUtility.CUSTOMER_ACTIVITY_REQUEST_CODE_MULTI:
			consignment = ServiceUtility.consignmentService.GetConsignmentById(consignmentIds.get(0));
			SetSignedTextValueAndColor();
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void SetSignedTextValueAndColor()
	{
		if (consignment != null && consignment.isSigned())
		{
			signedValue.setText("YES");
			signedValue.setTextColor(Color.GREEN);
			DisableSigningComponents();
		}
		else
		{
			signedValue.setText("NO");
			signedValue.setTextColor(Color.RED);
		}
	}

	private void DisableSigningComponents()
	{
		tripStatus.setEnabled(false);
		podStatus.setEnabled(false);
		signedValue.setEnabled(false);
		signName.setEnabled(false);
		sign.setEnabled(false);
	}

	private void DisableComponents(ConsignmentModel consignment)
	{
		imagestaken.setEnabled(false);
		viewCaptureImages.setEnabled(false);
		save.setEnabled(false);
		tripStatus.setEnabled(false);
		podStatus.setEnabled(false);
		signedValue.setEnabled(false);
		signName.setEnabled(false);
		sign.setEnabled(false);
	}

}
