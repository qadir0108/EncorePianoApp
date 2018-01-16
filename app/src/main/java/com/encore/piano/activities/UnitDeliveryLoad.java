package com.encore.piano.activities;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.asynctasks.SyncDeliver;
import com.encore.piano.asynctasks.SyncLoad;
import com.encore.piano.data.StaticData;
import com.encore.piano.enums.AdditionalItemStatusEnum;
import com.encore.piano.enums.PianoStatusEnum;
import com.encore.piano.enums.TakenLocationEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.data.StringConstants;
import com.encore.piano.model.AssignmentModel;
import com.encore.piano.server.AssignmentService;
import com.encore.piano.util.Alerter;
import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;
import com.encore.piano.util.DateTimeUtility;
import com.encore.piano.util.FileUtility;
import com.encore.piano.util.UIUtility;
import com.encore.piano.views.SignatureView;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnitDeliveryLoad extends AppCompatActivity {

	CheckBox chkMainLoaded;
	RadioGroup rdgBench1, rdgBench2, rdgCasterCups, rdgCover, rdgLamp, rdgManual;
	RadioGroup rdgMisc1, rdgMisc2, rdgMisc3;

    SignatureView mSignature;
    File filePath;
    String fileName = "";
    String username = "";

    TextView tvUnitName;
    Spinner spnrReceiver;
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_delivery_load);

		chkMainLoaded = (CheckBox) findViewById(R.id.chkMainLoaded);
		rdgBench1 = (RadioGroup) findViewById(R.id.rdgBench1);
		rdgBench2 = (RadioGroup) findViewById(R.id.rdgBench2);
		rdgCasterCups = (RadioGroup) findViewById(R.id.rdgCasterCups);
		rdgCover = (RadioGroup) findViewById(R.id.rdgCover);
		rdgLamp = (RadioGroup) findViewById(R.id.rdgLamp);
		rdgManual = (RadioGroup) findViewById(R.id.rdgManual);
		rdgMisc1 = (RadioGroup) findViewById(R.id.rdgMisc1);
		rdgMisc2 = (RadioGroup) findViewById(R.id.rdgMisc2);
		rdgMisc3 = (RadioGroup) findViewById(R.id.rdgMisc3);

        tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        spnrReceiver = (Spinner) findViewById(R.id.spnrReceiver);
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

		chkMainLoaded.setChecked(unitModel.isLoaded());
		setChecked(R.id.rdNotAvailable1, unitModel.getAdditionalBench1Status() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable2, unitModel.getAdditionalBench2Status() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable3, unitModel.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable4, unitModel.getAdditionalCoverStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable5, unitModel.getAdditionalLampStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable6, unitModel.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable7, unitModel.getAdditionalMisc1Status() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable8, unitModel.getAdditionalMisc2Status() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable9, unitModel.getAdditionalMisc3Status() == AdditionalItemStatusEnum.NotAvailable);

		setChecked(R.id.rdMissing1, unitModel.getAdditionalBench1Status() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing2, unitModel.getAdditionalBench2Status() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing3, unitModel.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing4, unitModel.getAdditionalCoverStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing5, unitModel.getAdditionalLampStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing6, unitModel.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing7, unitModel.getAdditionalMisc1Status() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing8, unitModel.getAdditionalMisc2Status() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing9, unitModel.getAdditionalMisc3Status() == AdditionalItemStatusEnum.Missing);

		setChecked(R.id.rdLoaded1, unitModel.getAdditionalBench1Status() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded2, unitModel.getAdditionalBench2Status() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded3, unitModel.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded4, unitModel.getAdditionalCoverStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded5, unitModel.getAdditionalLampStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded6, unitModel.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded7, unitModel.getAdditionalMisc1Status() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded8, unitModel.getAdditionalMisc2Status() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded9, unitModel.getAdditionalMisc3Status() == AdditionalItemStatusEnum.Loaded);

        receivers.add("Select person name");
        receivers.add(assignmentModel.getCallerName());
        receivers.add(assignmentModel.getPickupName());
        receivers.add(assignmentModel.getPickupAlternateContact());
        receiversAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, receivers);
        receiversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrReceiver.setAdapter(receiversAdapter);
        if (receivers.size() > 1)
            spnrReceiver.setSelection(1);

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
                    Alerter.error(UnitDeliveryLoad.this, "Error while saving signature");
                }
            }
        });
        btnCancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                setResult(RESULT_CANCELED);
                UnitDeliveryLoad.this.finish();
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
                Intent intentGallery = new Intent(UnitDeliveryLoad.this, AssignmentGallery.class);
                intentGallery.putExtra(StringConstants.INTENT_KEY_UNIT_ID, unitModel.getId());
                intentGallery.putExtra(StringConstants.INTENT_KEY_TAKEN_LOCATION, TakenLocationEnum.Pickup.Value);
                startActivity(intentGallery);
            }
        });
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

    public boolean save()
    {
        boolean isSuccess = true;

        if (spnrReceiver.getSelectedItem() != null)
            username = spnrReceiver.getSelectedItem().toString();

        try
        {
            mSignature.saveToCard(filePath);
            unitModel.setReceiverName(username);

            if(chkMainLoaded.isChecked())
                unitModel.setPianoStatus(PianoStatusEnum.Picked.Value);
            else
                unitModel.setPianoStatus(PianoStatusEnum.Booked.Value);

            unitModel.setPickedAt(DateTimeUtility.getCurrentTimeStamp());
            unitModel.setPickerName(username);
            unitModel.setPickerSignaturePath(filePath.getAbsolutePath());

            AdditionalItemStatusEnum bench1 =  (rdgBench1.getCheckedRadioButtonId() == R.id.rdNotAvailable1)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgBench1.getCheckedRadioButtonId() == R.id.rdMissing1
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalBench1Status(bench1);

            AdditionalItemStatusEnum bench2 =  (rdgBench2.getCheckedRadioButtonId() == R.id.rdNotAvailable2)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgBench2.getCheckedRadioButtonId() == R.id.rdMissing2
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalBench2Status(bench2);

            AdditionalItemStatusEnum casterCups =  (rdgCasterCups.getCheckedRadioButtonId() == R.id.rdNotAvailable3)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgCasterCups.getCheckedRadioButtonId() == R.id.rdMissing3
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalCasterCupsStatus(casterCups);

            AdditionalItemStatusEnum cover =  (rdgCover.getCheckedRadioButtonId() == R.id.rdNotAvailable4)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgCover.getCheckedRadioButtonId() == R.id.rdMissing4
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalCoverStatus(cover);

            AdditionalItemStatusEnum lamp =  (rdgLamp.getCheckedRadioButtonId() == R.id.rdNotAvailable5)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgLamp.getCheckedRadioButtonId() == R.id.rdMissing5
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalLampStatus(lamp);

            AdditionalItemStatusEnum manual =  (rdgManual.getCheckedRadioButtonId() == R.id.rdNotAvailable6)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgManual.getCheckedRadioButtonId() == R.id.rdMissing6
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalOwnersManualStatus(manual);

            AdditionalItemStatusEnum misc1 =  (rdgMisc1.getCheckedRadioButtonId() == R.id.rdNotAvailable7)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgMisc1.getCheckedRadioButtonId() == R.id.rdMissing7
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalMisc1Status(misc1);

            AdditionalItemStatusEnum misc2 =  (rdgMisc2.getCheckedRadioButtonId() == R.id.rdNotAvailable8)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgMisc2.getCheckedRadioButtonId() == R.id.rdMissing8
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalMisc2Status(misc2);

            AdditionalItemStatusEnum misc3 =  (rdgMisc3.getCheckedRadioButtonId() == R.id.rdNotAvailable9)
                    ? AdditionalItemStatusEnum.NotAvailable : ( rdgMisc3.getCheckedRadioButtonId() == R.id.rdMissing9
                    ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
            unitModel.setAdditionalMisc3Status(misc3);

            Service.unitService.setLoaded(unitModel);

            new SyncLoad(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, assignmentId, unitId);

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


    private void setChecked(int radioId, boolean isChecked) {
		((RadioButton) findViewById(radioId)).setChecked(isChecked);
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
			this.finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
