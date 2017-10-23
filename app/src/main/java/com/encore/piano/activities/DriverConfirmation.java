package com.encore.piano.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.encore.piano.R;
import com.encore.piano.server.ConfirmationService;
import com.encore.piano.server.Service;
import com.encore.piano.exceptions.*;
import com.encore.piano.listview.confirmation.ConfirmationAdapter;
import org.json.JSONException;

public class DriverConfirmation extends Activity implements OnClickListener, OnItemClickListener{
	
	Button AcceptAndSign;
	ListView confirmationListView;
	
	ConfirmationAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_confirmation);
		
		confirmationListView = (ListView)findViewById(R.id.confirmationListView);
		AcceptAndSign = (Button)findViewById(R.id.acceptAndSignButton);
		AcceptAndSign.setEnabled(false);
		
		confirmationListView.setOnItemClickListener(this);
		AcceptAndSign.setOnClickListener(this);
		
		new FetchConfirmations().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
	}
	
	class FetchConfirmations extends AsyncTask<Void, Void, Object>
	{

		@Override
		protected Object doInBackground(Void... params) {
			try {
				Service.confirmationService = new ConfirmationService(DriverConfirmation.this);
			} catch (UrlConnectionException e) {
				return e;
			} catch (JSONException e) {
				return e;
			} catch (JSONNullableException e) {
				return e;
			} catch (NotConnectedException e) {
				return e;
			} catch (NetworkStatePermissionException e) {
				return e;
			} catch (DatabaseInsertException e) {
				return e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if(result instanceof Exception)
				Toast.makeText(DriverConfirmation.this, result.getClass().getName(), Toast.LENGTH_SHORT).show();
			else{
				AcceptAndSign.setEnabled(true);
				adapter = new ConfirmationAdapter(DriverConfirmation.this);
				confirmationListView.setAdapter(adapter);				
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		
		boolean signed = true;
		for(int i = 0; i < Service.confirmationService.Confirmations.size(); i++)
		{
			if(signed)
			{
				if(!Service.confirmationService.Confirmations.get(i).isConfirmed())
					signed = false;
			}
		}
		
		if(signed)
		{
			Intent i = new Intent(DriverConfirmation.this, DriverSignature.class);
			startActivity(i);
			DriverConfirmation.this.finish();
		}
		else
			Toast.makeText(DriverConfirmation.this, "All conditions must be accepted.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long arg3) {
		if(listview.getId() == R.id.confirmationListView){
			if(!Service.confirmationService.Confirmations.get(position).isConfirmed())
				Service.confirmationService.Confirmations.get(position).setConfirmed(true);
			else
				Service.confirmationService.Confirmations.get(position).setConfirmed(false);
			
			adapter.notifyDataSetChanged();
			
		}
		
	}
}
