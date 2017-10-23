package com.encore.piano.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.encore.piano.R;
import com.encore.piano.data.StaticData;
import com.encore.piano.data.StringConstants;
import com.encore.piano.enums.PianoStatusEnum;
import com.encore.piano.enums.TripStatusEnum;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.AssignmentService;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;
import com.encore.piano.util.Alerter;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.FileUtility;
import com.encore.piano.views.SignatureView;
import com.encore.piano.asynctasks.SyncConsignment;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;

public class UnitDeliveryUnLoad extends AppCompatActivity {

	FrameLayout signatureLayoutContent;
	SignatureView mSignature;
	Button btnClear;
    Button btnSave;
	public int count = 1;
	public String current = null;
	private Bitmap mBitmap;
	File filePath;
	String fileName = "";
	String username = "";

    private TextView tvUnitName;
    private Spinner spnrReceiver;
    private Spinner spnrStatus;
	private Button btnCancel;
	ArrayList<String> receivers = new ArrayList<String>();
	ArrayAdapter<String> receiversAdapter;
	private Button btnAddReceiver;
	private Button btnGallery;

    AssignmentModel assignmentModel = null;
    UnitModel model = null;
    String assignmentId, unitId;

    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_delivery_unload);

        mSignature = (SignatureView) findViewById(R.id.View01);
        tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        spnrReceiver = (Spinner) findViewById(R.id.spnrReceiver);
        spnrStatus = (Spinner) findViewById(R.id.spnrStatus);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnAddReceiver = (Button) findViewById(R.id.btnAddReceiver);
        btnGallery = (Button) findViewById(R.id.btnGallery);

        signatureLayoutContent = (FrameLayout) findViewById(R.id.signatureContentLayout);
        mSignature.setBackgroundColor(Color.WHITE);
        btnClear = (Button) findViewById(R.id.clearbutton);
        btnSave = (Button) findViewById(R.id.savebutton);
        btnSave.setEnabled(false);
        mSignature.setSaveButton(btnSave);

        assignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
        unitId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_UNIT_ID);

        String unitName = "";
		try
		{
            if (Service.unitService == null)
				Service.unitService = new UnitService(this);
			model = Service.unitService.getUnitsByUnitId(unitId);

            StringBuilder sb = new StringBuilder();
            sb.append(model.getCategory() + " ");
            sb.append(model.getType()+ " ");
            sb.append(model.getMake()+ " ");
            sb.append(model.getModel()+ " ");
            sb.append(model.getFinish()+ " ");
            unitName = sb.toString().replace("null", "");
            tvUnitName.setText(unitName);

            if (Service.assignmentService == null)
                Service.assignmentService = new AssignmentService(this);
            assignmentModel = Service.assignmentService.getAll(assignmentId);

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

		receivers.add("Select person name");
		receivers.add(assignmentModel.getCallerName());
		receiversAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, receivers);
		receiversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnrReceiver.setAdapter(receiversAdapter);
        if (receivers.size() > 1)
            spnrReceiver.setSelection(1);

        List statuses = StaticData.getPianoStatuses();
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
		statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrStatus.setAdapter(statusAdapter);

        fileName = UUID.randomUUID().toString() + ".jpg";
        filePath = new File(FileUtility.getPODDirectory(unitId) + fileName);

		btnClear.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				mSignature.clear();
				btnSave.setEnabled(false);
			}
		});

		btnSave.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
                if (save())
                {
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Alerter.error(UnitDeliveryUnLoad.this, "Error while saving signature");
                }
			}
		});
		btnCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
                UnitDeliveryUnLoad.this.finish();
			}
		});
		btnAddReceiver.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				showInputDlg();
			}
		});
		btnGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentGallery = new Intent(UnitDeliveryUnLoad.this, AssignmentGallery.class);
                intentGallery.putExtra(StringConstants.INTENT_KEY_UNIT_ID, model.getId());
                startActivity(intentGallery);
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
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				receivers.add(input.getText().toString());
				receiversAdapter.notifyDataSetChanged();
				spnrReceiver.setSelection(spnrReceiver.getCount() - 1);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.assignment_status, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.backtodetails:
			setResult(RESULT_CANCELED);
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean save()
	{
		boolean returnValue = true;

        if (spnrReceiver.getSelectedItem() != null)
            username = spnrReceiver.getSelectedItem().toString();

		try
		{
            mSignature.SaveToCard(filePath);
            Service.assignmentService.writeCustomerSignatureAndStatus(username, filePath.getAbsolutePath(), assignmentId);

            model.setPianoStatus(PianoStatusEnum.Delivered.Value);
            model.setDeliveredAt(DateTimeUtility.getCurrentTimeStamp());
            model.setDeliveredAt(DateTimeUtility.getCurrentTimeStamp());
            Service.unitService.setDelivered(model);

            new SyncConsignment(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, assignmentId);

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
		btnSave.setEnabled(false);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}