package com.encore.piano.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.encore.piano.R;
import com.encore.piano.enums.AdditionalItemStatusEnum;
import com.encore.piano.enums.PianoStatusEnum;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.data.StringConstants;
import com.encore.piano.util.Alerter;
import com.encore.piano.logic.PreferenceUtility;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;

import org.json.JSONException;

public class UnitDeliveryLoad extends AppCompatActivity implements
		OnClickListener {

	CheckBox chkMainLoaded;
	RadioGroup rdgBenches;
	RadioGroup rdgCasterCups;
	RadioGroup rdgCover;
	RadioGroup rdgLamp;
	RadioGroup rdgManual;

	Button btnLoad;
    Button btnCancel;

	// GoogleMap map = null;
	UnitModel model = null;
	String unitId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_delivery_load);

		chkMainLoaded = (CheckBox) findViewById(R.id.chkMainLoaded);
		rdgBenches = (RadioGroup) findViewById(R.id.rdgBenches);
		rdgCasterCups = (RadioGroup) findViewById(R.id.rdgCasterCups);
		rdgCover = (RadioGroup) findViewById(R.id.rdgCover);
		rdgLamp = (RadioGroup) findViewById(R.id.rdgLamp);
		rdgManual = (RadioGroup) findViewById(R.id.rdgManual);
		btnLoad =(Button)findViewById(R.id.btnLoad);
		btnLoad.setOnClickListener(this);
        btnCancel =(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

		InitializeWidgets();
	}

	private void InitializeWidgets()
	{
		PreferenceUtility.GetPreferences(this);
		unitId = getIntent().getExtras().getString(StringConstants.INTENT_KEY_UNIT_ID);
		model = Service.unitService.getUnitsByUnitId(unitId);

		chkMainLoaded.setChecked(model.isLoaded());
		setChecked(R.id.rdNotAvailable1, model.getAdditionalBenchesStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable2, model.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable3, model.getAdditionalCoverStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable4, model.getAdditionalLampStatus() == AdditionalItemStatusEnum.NotAvailable);
		setChecked(R.id.rdNotAvailable5, model.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.NotAvailable);

		setChecked(R.id.rdMissing1, model.getAdditionalBenchesStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing2, model.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing3, model.getAdditionalCoverStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing4, model.getAdditionalLampStatus() == AdditionalItemStatusEnum.Missing);
		setChecked(R.id.rdMissing5, model.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.Missing);

		setChecked(R.id.rdLoaded1, model.getAdditionalBenchesStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded2, model.getAdditionalCasterCupsStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded3, model.getAdditionalCoverStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded4, model.getAdditionalLampStatus() == AdditionalItemStatusEnum.Loaded);
		setChecked(R.id.rdLoaded5, model.getAdditionalOwnersManualStatus() == AdditionalItemStatusEnum.Loaded);
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

	@Override
	public void onClick(View v)
	{
        switch (v.getId()) {
			case R.id.btnLoad:
                try {
                    if(chkMainLoaded.isChecked())
                        model.setPianoStatus(PianoStatusEnum.Picked.Value);
                    else
                        model.setPianoStatus(PianoStatusEnum.Booked.Value);

                    AdditionalItemStatusEnum benches =  (rdgBenches.getCheckedRadioButtonId() == R.id.rdNotAvailable1)
                            ? AdditionalItemStatusEnum.NotAvailable : ( rdgBenches.getCheckedRadioButtonId() == R.id.rdMissing1
                            ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
                    model.setAdditionalBenchesStatus(benches);

                    AdditionalItemStatusEnum casterCups =  (rdgCasterCups.getCheckedRadioButtonId() == R.id.rdNotAvailable2)
                            ? AdditionalItemStatusEnum.NotAvailable : ( rdgCasterCups.getCheckedRadioButtonId() == R.id.rdMissing2
                            ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
                    model.setAdditionalCasterCupsStatus(casterCups);

                    AdditionalItemStatusEnum cover =  (rdgCover.getCheckedRadioButtonId() == R.id.rdNotAvailable3)
                            ? AdditionalItemStatusEnum.NotAvailable : ( rdgCover.getCheckedRadioButtonId() == R.id.rdMissing3
                            ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
                    model.setAdditionalCoverStatus(cover);

                    AdditionalItemStatusEnum lamp =  (rdgLamp.getCheckedRadioButtonId() == R.id.rdNotAvailable4)
                            ? AdditionalItemStatusEnum.NotAvailable : ( rdgLamp.getCheckedRadioButtonId() == R.id.rdMissing4
                            ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
                    model.setAdditionalLampStatus(lamp);

                    AdditionalItemStatusEnum manual =  (rdgManual.getCheckedRadioButtonId() == R.id.rdNotAvailable5)
                            ? AdditionalItemStatusEnum.NotAvailable : ( rdgManual.getCheckedRadioButtonId() == R.id.rdMissing5
                            ? AdditionalItemStatusEnum.Missing : AdditionalItemStatusEnum.Loaded );
                    model.setAdditionalOwnersManualStatus(manual);

                    Service.unitService = new UnitService(UnitDeliveryLoad.this);
                    Service.unitService.setLoaded(model);

                    setResult(RESULT_OK);
                    finish();

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
                } catch (DatabaseUpdateException e) {
                    e.printStackTrace();
                    Alerter.error(UnitDeliveryLoad.this,e.getMessage());
                }
				break;

            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
		default:
			break;
		}
	}
}
