package com.encore.piano.activities;

import static com.encore.piano.util.CommonUtility.EXTRA_MESSAGE;
import static com.encore.piano.util.CommonUtility.FROM_NOTIFICATION;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.asynctasks.AckMessage;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.listview.conversation.ConversationAdapter;

public class Conversation extends AppCompatActivity implements OnItemClickListener {

	ListView listviewConversations;
	ConversationAdapter adapter;

	String UserName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversations);
		
		if(getIntent().getExtras()!=null)
			if(getIntent().getExtras().containsKey(FROM_NOTIFICATION))
			{
				String MessageId = getIntent().getExtras().getString(EXTRA_MESSAGE);
				
				new AckMessage(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, MessageId);
				
				getIntent().removeExtra(EXTRA_MESSAGE); 
				
			}
		
		listviewConversations = (ListView)findViewById(R.id.listviewConversations);
		listviewConversations.setOnItemClickListener(this);
			
		LoadConversations();
		
	}

	public boolean LoadConversations() {
		
		try {
			
			if(ServiceUtility.loginService != null){
				if(ServiceUtility.loginService.LoginModel != null)
					UserName = ServiceUtility.loginService.LoginModel.getUserName();
			}
			
			adapter = new ConversationAdapter(Conversation.this, UserName);
			
		} catch (UrlConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONNullableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkStatePermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listviewConversations.setAdapter(adapter);
		
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.topmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
//			case R.id.syncmessages:
//				onSyncMessagesOptionClick();
//			break;
//			case R.id.newmessage:
//				onNewMessageOptionClick();
//				break;
		
			case R.id.main:				
				onMainClicked();
				break;
			case R.id.runsheet:				
				onRunSheetClicked();
				break;
			case R.id.showmap:				
				onShowMapClicked();
				break;
//			case R.id.messages:				
//				onMessagesClicked();
//				break;		
				
			default:
				break;
		}
	
		return super.onOptionsItemSelected(item);
	}
	
	public void onMainClicked()
	{
		this.finish();
		
		Intent i = new Intent(this, com.encore.piano.activities.StartScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
		
	}
	
	public void onRunSheetClicked()
	{
		this.finish();
		
		Intent i = new Intent(this, com.encore.piano.activities.Consignment.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
		
	}
	
	public void onShowMapClicked()
	{
		this.finish();
		
		Intent i = new Intent(this, com.encore.piano.activities.Map.class);
		i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long arg3) {
		
		String Sender = ((TextView)view.findViewById(R.id.tvSender)).getText().toString();
		
		Intent i = new Intent(Conversation.this, com.encore.piano.activities.Message.class);
		i.putExtra("UserName", Sender);
		startActivity(i);
		
	}
	
	@Override
	protected void onResume() {
		
		LoadConversations();
		
		super.onResume();
	}
	
}
