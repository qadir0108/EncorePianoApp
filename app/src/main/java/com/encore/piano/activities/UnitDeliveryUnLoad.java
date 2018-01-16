package com.encore.piano.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.asynctasks.SyncDeliver;
import com.encore.piano.data.StaticData;
import com.encore.piano.data.StringConstants;
import com.encore.piano.enums.AdditionalItemStatusEnum;
import com.encore.piano.enums.TakenLocationEnum;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.AssignmentService;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;
import com.encore.piano.util.Alerter;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.FileUtility;
import com.encore.piano.util.UIUtility;
import com.encore.piano.views.SignatureView;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;

public class UnitDeliveryUnLoad extends AppCompatActivity {

	SignatureView mSignature;
	File filePath;
	String fileName = "";
	String username = "";

    TextView tvUnitName;
    Spinner spnrReceiver;
    Spinner spnrStatus;
	ArrayList<String> receivers = new ArrayList<String>();
	ArrayAdapter<String> receiversAdapter;
	Button btnAddReceiver;
	Button btnGallery;
	Button btnCancel;
	Button btnClear;
	Button btnSave;

    AssignmentModel assignmentModel = null;
    UnitModel unitModel = null;
    String assignmentId, unitId;

    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_delivery_unload);

        tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        spnrReceiver = (Spinner) findViewById(R.id.spnrReceiver);
        spnrStatus = (Spinner) findViewById(R.id.spnrStatus);
        btnAddReceiver = (Button) findViewById(R.id.btnAddReceiver);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
        mSignature = (SignatureView) findViewById(R.id.View01);
        mSignature.setBackgroundColor(Color.WHITE);
        mSignature.setSaveButton(btnSave);

        Load();
        Initialize();

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
                setResult(RESULT_CANCELED);
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
                intentGallery.putExtra(StringConstants.INTENT_KEY_UNIT_ID, unitModel.getId());
                intentGallery.putExtra(StringConstants.INTENT_KEY_TAKEN_LOCATION, TakenLocationEnum.Delivery.Value);
                startActivity(intentGallery);
			}
		});
	}

    private void Load()
    {
        try
        {
            assignmentId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_ASSIGNMENT_ID);
            unitId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_UNIT_ID);

            if (Service.unitService == null)
                Service.unitService = new UnitService(this);
            unitModel = Service.unitService.getUnitsByUnitId(unitId);

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
    }

    private void Initialize()
    {
        String unitName = new StringBuilder()
                .append(unitModel.getCategory() + " ")
                .append(unitModel.getType()+ " ")
                .append(unitModel.getMake()+ " ")
                .append(unitModel.getModel()+ " ")
                .append(unitModel.getFinish()+ " ")
                .toString().replace("null", "");
        tvUnitName.setText(unitName);

        UIUtility.setVisible(this, R.id.chkBench1, unitModel.getAdditionalBench1Status() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkBench2, unitModel.getAdditionalBench2Status() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkCasterCups, unitModel.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkCover, unitModel.getAdditionalCoverStatus() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkLamp, unitModel.getAdditionalLampStatus() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkOwnersManual, unitModel.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkMisc1, unitModel.getAdditionalMisc1Status() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkMisc2, unitModel.getAdditionalMisc2Status() == AdditionalItemStatusEnum.Loaded);
        UIUtility.setVisible(this, R.id.chkMisc3, unitModel.getAdditionalMisc3Status() == AdditionalItemStatusEnum.Loaded);

        receivers.add("Select person name");
        receivers.add(assignmentModel.getCallerName());
        receivers.add(assignmentModel.getDeliveryName());
        receivers.add(assignmentModel.getDeliveryAlternateContact());
        receiversAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, receivers);
        receiversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrReceiver.setAdapter(receiversAdapter);
        if (receivers.size() > 1)
            spnrReceiver.setSelection(1);

        List statuses = StaticData.getPianoStatuses().subList(3, StaticData.getPianoStatuses().size());
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrStatus.setAdapter(statusAdapter);

        fileName = UUID.randomUUID().toString() + ".jpg";
        filePath = new File(FileUtility.getPODDirectory(unitId) + fileName);
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
		boolean isSuccess = true;

        if (spnrReceiver.getSelectedItem() != null)
            username = spnrReceiver.getSelectedItem().toString();

        String status = "";
        if (spnrStatus.getSelectedItem() != null)
            status = spnrStatus.getSelectedItem().toString();

		try
		{
            mSignature.saveToCard(filePath);

            unitModel.setPianoStatus(status);
			unitModel.setDeliveredAt(DateTimeUtility.getCurrentTimeStamp());
			unitModel.setReceiverName(username);
			unitModel.setReceiverSignaturePath(filePath.getAbsolutePath());
            unitModel.setBench1Unloaded(UIUtility.isChecked(this, R.id.chkBench1));
            unitModel.setBench2Unloaded(UIUtility.isChecked(this, R.id.chkBench2));
            unitModel.setCasterCupsUnloaded(UIUtility.isChecked(this, R.id.chkCasterCups));
            unitModel.setCoverUnloaded(UIUtility.isChecked(this, R.id.chkCover));
            unitModel.setLampUnloaded(UIUtility.isChecked(this, R.id.chkLamp));
            unitModel.setOwnersManualUnloaded(UIUtility.isChecked(this, R.id.chkOwnersManual));
            unitModel.setMisc1Unloaded(UIUtility.isChecked(this, R.id.chkMisc1));
            unitModel.setMisc2Unloaded(UIUtility.isChecked(this, R.id.chkMisc2));
            unitModel.setMisc3Unloaded(UIUtility.isChecked(this, R.id.chkMisc3));
            Service.unitService.setDelivered(unitModel);

            new SyncDeliver(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, assignmentId, unitId);

		} catch (FileNotFoundException e)
		{
			isSuccess = false;
		} catch (IOException e)
		{
			isSuccess = false;
		} catch (DatabaseUpdateException e)
		{
			isSuccess = false;
		}
		return isSuccess;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}