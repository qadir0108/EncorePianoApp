package com.encore.piano.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.views.SignatureView;
import com.encore.piano.asynctasks.SyncConsignment;
import com.encore.piano.db.Database;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.ConsignmentModel;
import com.encore.piano.model.PianoModel;
import com.encore.piano.services.ItemService;

public class CustomerCaptureSignature extends AppCompatActivity {

	FrameLayout signatureLayoutContent;
	SignatureView mSignature;
	Button ClearButton,
			SaveButton;
	public static String tempDir;
	public int count = 1;
	public String current = null;
	private Bitmap mBitmap;
	View mView;
	File mypath;
	ArrayList<File> myPaths = new ArrayList<File>();

	String fileName = "";
	String username = "";
	String tripStatus = "";
	String podStatus = "";

	ArrayList<String> consignmentIds = null;

	String path = "/EncorePianoApp";

	ActionBar actionBar;
	private TextView itemNames;
	private String consigmentAddress;
	private Spinner receiverSpinner;
	private Button exceptionsButton;
	private String consignmentID;
	ArrayList<String> model = new ArrayList<String>();
	ArrayAdapter<String> tripadapter;
	private Button addNewButton;
	private String arrivalTime;
	private ConsignmentModel consignmentModel = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_signature);

        prepareDirectory();

//		actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayHomeAsUpEnabled(true);

		username = getIntent().getExtras().getString(ServiceUtility.CUSTOMER_SIGN_CAPTURE_USERNAME);
		tripStatus = getIntent().getExtras().getString(ServiceUtility.CUSTOMER_TRIP_STATUS);
		podStatus = getIntent().getExtras().getString(ServiceUtility.CUSTOMER_POD_STATUS);

		//tripStatus = "D";
		//podStatus = "R";

		tempDir = Environment.getExternalStorageDirectory() + "/" + path + "/";
		String itemStr = new String();

		try
		{
			if (ServiceUtility.itemService == null)
				ServiceUtility.itemService = new ItemService(this);
		} catch (UrlConnectionException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		} catch (JSONNullableException e)
		{
			e.printStackTrace();
		} catch (NotConnectedException e)
		{
			e.printStackTrace();
		} catch (NetworkStatePermissionException e)
		{
			e.printStackTrace();
		} catch (DatabaseInsertException e)
		{
			e.printStackTrace();
		}

		if (getIntent().getExtras().getString(ServiceUtility.CUSTOMER_SIGN_CAPTURE_FILENAME) != null)
		{
			fileName = getIntent().getExtras().getString(ServiceUtility.CUSTOMER_SIGN_CAPTURE_FILENAME);
			consignmentID = fileName;
			current = fileName + ".png";
			mypath = new File(tempDir, current);

			ArrayList<PianoModel> Items = ServiceUtility.itemService.GetItemsForConsignment(fileName);
			int counter = 1;
			for (PianoModel m : Items)
			{
				itemStr += "Piano " + counter + ": " + m.getName() + "\r\n";
				counter++;
			}

			consignmentModel = Database.GetConsignments(this, false, false, consignmentID).get(0);
		}
		else
		{
			consignmentIds = getIntent().getStringArrayListExtra(ServiceUtility.CUSTOMER_SIGN_LIST);
			int counter = 1;
			for (String id : consignmentIds)
			{
				fileName = UUID.randomUUID().toString() + ".png";
				myPaths.add(new File(tempDir, fileName));

				ConsignmentModel m = Database.GetConsignments(this, false, false, id).get(0);

				itemStr += "Order " + counter + ": " + m.getDeliveryAddress() + "\r\n";
				counter++;
			}
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			arrivalTime = extras.getString(ServiceUtility.ARRIVAL_TIME);
		}

		consigmentAddress = getIntent().getStringExtra(ServiceUtility.CUSTOMER_ADDRESS);

		signatureLayoutContent = (FrameLayout) findViewById(R.id.signatureContentLayout);
		//	mSignature = new signatureService(this, null);
		mSignature = (SignatureView) findViewById(R.id.View01);
		mSignature.setBackgroundColor(Color.WHITE);
		//	signatureLayoutContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ClearButton = (Button) findViewById(R.id.clearbutton);
		SaveButton = (Button) findViewById(R.id.savebutton);
		SaveButton.setEnabled(false);
		mSignature.setSaveButton(SaveButton);

		itemNames = (TextView) findViewById(R.id.itemnames);
		receiverSpinner = (Spinner) findViewById(R.id.receiverspinner);
		exceptionsButton = (Button) findViewById(R.id.exceptionsbutton);
		addNewButton = (Button) findViewById(R.id.btn_AddNew);

		itemNames.setText(itemStr);

		model.add("Select person name");

		// if multiple consignments then load similar delivery address from db and filter out duplicates here. filteration can be done
		// at db level
		if (consigmentAddress != null)
		{
			ArrayList<String> addresses = new ArrayList<>(); //Database.GetConsignmentReceiversByAddress(this, consigmentAddress);

			for (String m1 : addresses)
			{
				if (m1 == null)
					continue;

				boolean found = false;

				// avoid duplicate entries
				for (String m2 : model)
					if (m2.equals(m1))
					{
						found = true;
						break;
					}

				if (!found)
					model.add(m1);
			}

		}

		tripadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, model);
		tripadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		receiverSpinner.setAdapter(tripadapter);

		//	if (model.size() > 1)
		//		receiverSpinner.setSelection(1);

		ClearButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mSignature.clear();
				SaveButton.setEnabled(false);
			}
		});

		SaveButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onSaveOptionClick();
			}
		});

		exceptionsButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{

                Intent i = new Intent(CustomerCaptureSignature.this, ConsignmentStatusManagement.class);
				//	i.putExtra(ServiceUtility.CUSTOMER_SIGN_CAPTURE_USERNAME, name);
				i.putExtra(ServiceUtility.CUSTOMER_TRIP_STATUS, tripStatus);

				if (consignmentIds != null)
					i.putExtra(ServiceUtility.CONSIGNMENT_ID_LIST_KEY, consignmentIds);
				else
					i.putExtra(ServiceUtility.ITEM_INTENT_KEY, consignmentID);

				startActivityForResult(i, 0);

			}
		});

		addNewButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				showInputDlg();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		setResult(resultCode);
		finish();

		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void showInputDlg()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add name");

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				model.add(input.getText().toString());

				tripadapter.notifyDataSetChanged();
				receiverSpinner.setSelection(receiverSpinner.getCount() - 1);

			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		AlertDialog alertToShow = builder.create();
		alertToShow.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertToShow.show();

		//builder.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.topmenu_consignment_change_status, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId()) {

		case R.id.backtochangestatus:
			setResult(RESULT_CANCELED);
			this.finish();
			break;

		//			case R.id.signaturesavemenuitem:
		//				onSaveOptionClick();
		//			break;
		//			case R.id.signatureclearmenuitem:
		//				onClearOptionClick();
		//				break;
		//			case android.R.id.home:				
		//				CustomerCaptureSignature.this.finish();
		//				break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void onSaveOptionClick()
	{
		if (save())
		{
			setResult(RESULT_OK);
			finish();
		}
		else
			Toast.makeText(CustomerCaptureSignature.this, "Error occured while saving signatureService.", Toast.LENGTH_LONG).show();
	}

	public boolean save()
	{
		boolean returnValue = true;

		if (username.equals("unknown"))
			if (receiverSpinner.getSelectedItem() != null)
				username = receiverSpinner.getSelectedItem().toString();

		try
		{
			if (consignmentIds != null)
			{
				for (int i = 0; i < consignmentIds.size(); i++)
				{
					String consignmentId = consignmentIds.get(i);
					mSignature.SaveToCard(myPaths.get(i));

					ServiceUtility.consignmentService.WriteCustomerSignatureAndStatus(username, myPaths.get(i).getAbsolutePath(), consignmentId, tripStatus, podStatus);
					ServiceUtility.consignmentService.SaveConsignment(consignmentId, tripStatus, podStatus, new String[] {});

					new SyncConsignment(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentId);
				}
			}
			else
			{
				mSignature.SaveToCard(mypath);
				ServiceUtility.consignmentService.WriteCustomerSignatureAndStatus(username, mypath.getAbsolutePath(), consignmentID, tripStatus, podStatus);

				// Write Statuses
				if (consignmentModel.getOrderType().equals(ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY))
					ServiceUtility.consignmentService.SaveConsignment(consignmentID, tripStatus, podStatus, new String[] {}); // trip status is already saved but no harm here
				else
					// save consignmentService also mark saved
					ServiceUtility.consignmentService.SaveConsignment(consignmentID, tripStatus, podStatus, new String[] {});

				//sync here in both rapid and transfocus case, before opening departure time ui
				new SyncConsignment(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, consignmentID);

				if (consignmentModel.getOrderType().equals(ServiceUtility.JOB_TYPE_CONTAINER_DELIVERY))
				{
					// only set status if customer signed successfully, in case user goes back, we remember the status, user cannot
					// go forward unless signed successfully

                    //ServiceUtility.FirebaseMessageService.SetTripStatus(consignmentID, "startedimplicitly");

					Intent i = new Intent(CustomerCaptureSignature.this, DepartureTime.class);
					i.putExtra(ServiceUtility.CONSIGNMENT_INTENT_KEY, consignmentID);
					i.putExtra(ServiceUtility.CUSTOMER_POD_STATUS, podStatus);
					i.putExtra(ServiceUtility.ARRIVAL_TIME, arrivalTime);
					startActivity(i);
				}

			}

		} catch (FileNotFoundException e)
		{
			returnValue = false;
		} catch (IOException e)
		{
			returnValue = false;
		} catch (DatabaseUpdateException e)
		{
			returnValue = false;
		}

		return returnValue;

	}

	private void onClearOptionClick()
	{
		mSignature.clear();
		SaveButton.setEnabled(false);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	private boolean prepareDirectory()
	{
		try
		{
			if (makedirs())
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this, "Could not initiate File System.. Is SDCard mounted properly?", 1000).show();
			return false;
		}
	}

	private boolean makedirs()
	{
		File tempdir = new File(tempDir);
		if (!tempdir.exists())
			tempdir.mkdirs();

		return (tempdir.isDirectory());
	}

}