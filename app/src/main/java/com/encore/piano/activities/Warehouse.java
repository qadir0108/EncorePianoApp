package com.encore.piano.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.data.NumberConstants;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.DatabaseUpdateException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.data.StringConstants;
import com.encore.piano.model.UnitModel;
import com.encore.piano.server.Service;
import com.encore.piano.server.UnitService;
import com.encore.piano.util.Alerter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Warehouse extends AppCompatActivity {

    private TextView tvCategory;
    private TextView tvType;
    private TextView tvSize;
    private TextView tvMake;
    private TextView tvModel;
    private TextView tvFinish;
    private TextView tvSerialNumber;
    private CheckBox chkBench;
    private CheckBox chkPlayer;
    private CheckBox chkBoxed;

    private Button btnScan;
    private Button btnGallery;
    private Button btnSave;
    private Button btnCancel;
    Intent intentGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.warehouse);

        tvCategory = (TextView)findViewById(R.id.tvCategory);
        tvType = (TextView)findViewById(R.id.tvType);
        tvSize = (TextView)findViewById(R.id.tvSize);
        tvMake = (TextView)findViewById(R.id.tvMake);
        tvModel = (TextView)findViewById(R.id.tvModel);
        tvFinish = (TextView)findViewById(R.id.tvFinish);
        tvSerialNumber = (TextView)findViewById(R.id.tvSerialNumber);
        chkBench = (CheckBox)findViewById(R.id.chkBench);
        chkPlayer = (CheckBox)findViewById(R.id.chkPlayer);
        chkBoxed = (CheckBox)findViewById(R.id.chkBoxed);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setFocusableInTouchMode(true);
        btnScan.requestFocus();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Warehouse.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan Pick ticket");
                integrator.initiateScan();
            }
        });

        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnGallery.setEnabled(false);
        intentGallery = new Intent(Warehouse.this, WarehouseUnitGallery.class);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intentGallery.putExtra(StringConstants.INTENT_KEY_UNIT_ID, unitModel.getUnitId());
                startActivity(intentGallery);
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(Warehouse.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(Alerter.title)
                        .setContentText("Are you sure?")
                        .setConfirmText("Yes,Save")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                saveUnit();
                                sDialog.hide();

                                Alerter.success(Warehouse.this, "Warehouse unit updated successfully!");

//                                sDialog.setTitleText("Done!")
//                                        .setContentText("Saved!")
//                                        .setConfirmText("OK")
//                                        .setConfirmClickListener(null)
//                                        .showCancelButton(false)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setCancelText("No, cancel!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.warehouse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.backtomain:
                Warehouse.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String serialNumber = scanResult.getContents();
            if(serialNumber == null) serialNumber = "54321";
            Alerter.success(this, serialNumber);
            loadUnit(serialNumber);
        }
    }

    private void loadUnit(String serialNumber) {
        try {
            Service.unitService = new UnitService(Warehouse.this);
            UnitModel model = Service.unitService.getUnitsBySerialNumber(serialNumber);
            intentGallery.putExtra(StringConstants.INTENT_KEY_UNIT_ID, model.getId());
            btnGallery.setEnabled(true);

            tvCategory.setText(model.getCategory());
            tvType.setText(model.getType());
            tvSize.setText(model.getSize());
            tvMake.setText(model.getMake());
            tvModel.setText(model.getModel());
            tvFinish.setText(model.getFinish());
            tvSerialNumber.setText(model.getSerialNumber());
            chkBench.setChecked(model.isBench());
            chkPlayer.setChecked(model.isPlayer());
            chkBoxed.setChecked(model.isBoxed());

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

    private void saveUnit() {
        try {
            Service.unitService = new UnitService(Warehouse.this);
            String serialNumber = tvSerialNumber.getText().toString();
            UnitModel model = Service.unitService.getUnitsBySerialNumber(serialNumber);
            model.setCategory(tvCategory.getText().toString());
            model.setType(tvType.getText().toString());
            model.setSize(tvSize.getText().toString());
            model.setMake(tvMake.getText().toString());
            model.setModel(tvModel.getText().toString());
            model.setFinish(tvFinish.getText().toString());
            model.setSerialNumber(tvSerialNumber.getText().toString());
            model.setBench(chkBench.isChecked());
            model.setPlayer(chkPlayer.isChecked());
            model.setBoxed(chkBoxed.isChecked());

            Service.unitService.saveUnit(model);

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
        } catch (DatabaseUpdateException e) {
            e.printStackTrace();
        }
    }

}
