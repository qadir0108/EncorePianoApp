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
import com.encore.piano.services.ConfirmationService;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.exceptions.*;
import com.encore.piano.listview.confirmation.ConfirmationAdapter;
import org.json.JSONException;

public class Confirmation extends Activity implements OnClickListener, OnItemClickListener{
	
	Button AcceptAndSign;
	ListView confirmationListView;
	
	ConfirmationAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
		
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
				ServiceUtility.confirmationService = new ConfirmationService(Confirmation.this);
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
				Toast.makeText(Confirmation.this, result.getClass().getName(), Toast.LENGTH_SHORT).show();
			else{
				AcceptAndSign.setEnabled(true);
				adapter = new ConfirmationAdapter(Confirmation.this);
				confirmationListView.setAdapter(adapter);				
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		
		boolean signed = true;
		for(int i = 0; i < ServiceUtility.confirmationService.Confirmations.size(); i++)
		{
			if(signed)
			{
				if(!ServiceUtility.confirmationService.Confirmations.get(i).isConfirmed())
					signed = false;
			}
		}
		
		if(signed)
		{
			Intent i = new Intent(Confirmation.this, com.encore.piano.activities.CaptureSignature.class);
			startActivity(i);
			Confirmation.this.finish();
		}
		else
			Toast.makeText(Confirmation.this, "All conditions must be accepted.", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long arg3) {
		if(listview.getId() == R.id.confirmationListView){
			if(!ServiceUtility.confirmationService.Confirmations.get(position).isConfirmed())
				ServiceUtility.confirmationService.Confirmations.get(position).setConfirmed(true);
			else
				ServiceUtility.confirmationService.Confirmations.get(position).setConfirmed(false);
			
			adapter.notifyDataSetChanged();
			
		}
		
	}
}
