package com.encore.piano.activities;

import java.util.Date;
import java.util.Random;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.services.RecipientService;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.model.MessageModel;
import com.encore.piano.model.MessageModel.MessageFolderEnum;
import com.encore.piano.model.RecipientModel;

public class NewMessage extends Activity implements OnClickListener{

	
	//Spinner recipients;
	String recipient;
	EditText subject;
	EditText messageEditText;
	
	Button btnCancel;
	Button btnSend;
	
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newmessage);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
	
		//recipients = (Spinner)findViewById(R.id.recipientsSpinner);
		
		subject = (EditText)findViewById(R.id.subjectNewMessageEditText);
		messageEditText = (EditText)findViewById(R.id.messageNewMessageEditText);
		
		btnCancel = (Button)findViewById(R.id.btnCancelNewMessage);
		btnSend = (Button)findViewById(R.id.btnSendNewMessage);

		btnSend.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		recipient = getIntent().getStringExtra("UserName");
		
		//new FetchRecipients().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.topmenu_messages, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.backtomessages:		
				NewMessage.this.finish();
			break;	
//			case R.id.messagereadmenusend:
//				onSendMessageOptionClick();
//			break;
//			case android.R.id.home:		
//				NewMessage.this.finish();
//				break;
			default:
				break;
		}
	
		return super.onOptionsItemSelected(item);
	}
	
	private void onSendMessageOptionClick(){
		MessageModel message = new MessageModel();
		message.setFolder(MessageFolderEnum.Outbox.Value);
		message.setID(ServiceUtility.GetMilisTimeToString() + String.valueOf(new Random().nextInt(1000)));
		message.setMessageText(messageEditText.getText().toString());
		//message.setRecipient(((RecipientModel)recipients.getSelectedItem()).getName());
		message.setRecipient(recipient);
		message.setSender(ServiceUtility.loginService.LoginModel.getUserName());
		message.setSubject(subject.getText().toString());
		message.setTimestamp(new Date().toString());
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("message",message);
		setResult(RESULT_OK,returnIntent);
		finish();
		
	}
	
	ProgressDialog dialog = null;
	class FetchRecipients extends AsyncTask<Void, Void, String>
	{

		@Override
		protected void onPreExecute() {
			//recipients.setEnabled(false);

			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			try {
				
				ServiceUtility.recipientService = new RecipientService(NewMessage.this);
				if(ServiceUtility.recipientService.getErrorMessage().equals(""))
					return ServiceUtility.recipientService.getErrorMessage();
			} catch (UrlConnectionException e) {
				return "Urlexc";
			} catch (JSONException e) {
				e.printStackTrace();
				return "JSONEXC";
			} catch (JSONNullableException e) {
				return "JSONNULLexc";
			} catch (NotConnectedException e) {
				return "NOTCONNexc";
			} catch (NetworkStatePermissionException e) {
				return "NETWORKSTATEexc";
			} catch (DatabaseInsertException e) {
				return "DBINSERTexc";
			} 
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			if (result != null && !result.equals(""))
				ShowMessage("Error", result);
			else {
				ArrayAdapter<RecipientModel> adapter = new ArrayAdapter<RecipientModel>(
						NewMessage.this, android.R.layout.simple_spinner_item,
						ServiceUtility.recipientService.Recipients);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//recipients.setAdapter(adapter);
				//recipients.setEnabled(true);

			}
			
			super.onPostExecute(result);
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.btnSendNewMessage:
			onSendMessageOptionClick();
			break;
		case R.id.btnCancelNewMessage:
			setResult(RESULT_CANCELED);
			this.finish();
			break;
		default:
				break;
		}
		
	}
	
	private void ShowMessage(final String title, String message){
		
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(NewMessage.this);		
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	
				NewMessage.this.finish();
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	
}
