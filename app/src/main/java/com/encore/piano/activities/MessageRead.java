package com.encore.piano.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.model.MessageModel;
import com.encore.piano.model.MessageModel.MessageFolderEnum;

import java.util.Date;

public class MessageRead extends Activity implements OnClickListener{

	TextView sender;
	TextView subject;
	TextView text;
	//TextView recipient;
	TextView timestamp;
	
	Button close;
	Button reply;
	
	EditText messageReplyText;
	
	MessageModel messageModel;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messageread);
		
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		sender = (TextView)findViewById(R.id.messageSenderValue);
		subject = (TextView)findViewById(R.id.messageSubjectValue);
		text = (TextView)findViewById(R.id.messageTextValue);
		//recipient = (TextView)findViewById(R.id.replyStaticTextView);
		timestamp = (TextView)findViewById(R.id.messagereadtimestamp);
		
		close = (Button)findViewById(R.id.messageCloseButton);
		reply = (Button)findViewById(R.id.messageReplyButton);
		
		close.setOnClickListener(this);
		reply.setOnClickListener(this);
		
		messageReplyText = (EditText)findViewById(R.id.messageReplyEditText);
		
		String messageId = getIntent().getExtras().getString(ServiceUtility.MESSAGE_INTENT_KEY);
		
		messageModel = ServiceUtility.messageService.GetMessageById(messageId);
		
//		if(messageModel.getFolder().equals("Sent"));
//		{
//			messageReplyText.setEnabled(false);
//			reply.setEnabled(false);
//			recipient.setEnabled(false);
//		}
		
		sender.setText(messageModel.getSender());
		subject.setText(messageModel.getSubject());
		text.setText(messageModel.getMessageText());
		timestamp.setText(messageModel.getTimestamp());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.topmenu_messages, menu);
		
//		if(messageModel.getFolder().equals("Sent")){
//			menu.getItem(0).setEnabled(false);
//		}
//		
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.backtomessages:		
				this.finish();
			break;	
//			case R.id.messagereadmenureply:
//				onReplyOptionClick();
//			break;
//			case android.R.id.home:		
//				MessageRead.this.finish();
//				break;
			default:
				break;
		}
	
		return super.onOptionsItemSelected(item);
	}
	
	private void onReplyOptionClick(){
		MessageModel message = new MessageModel();
		message.setFolder(MessageFolderEnum.Outbox.Value);
		//model.setID(messageModel.getID() + "_reply_" + ServiceUtility.GetMilisTimeToString());
		message.setMessageText(messageReplyText.getText().toString());
		message.setRecipient(sender.getText().toString());
		message.setSender(ServiceUtility.loginService.LoginModel.getUserName());
		message.setSubject(subject.getText().toString());
		message.setTimestamp(new Date().toString());
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("message",message);
		setResult(RESULT_OK,returnIntent);
		finish();
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
			case R.id.messageCloseButton:
				setResult(RESULT_CANCELED);
				this.finish();
				break;
			case R.id.messageReplyButton:
				onReplyOptionClick();
				break;
			default:
				break;
		}		
	}
	
}
