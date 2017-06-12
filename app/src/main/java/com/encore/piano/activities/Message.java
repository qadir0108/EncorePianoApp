package com.encore.piano.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.encore.piano.R;
import com.encore.piano.services.MessageService;
import com.encore.piano.services.ServiceUtility;
import com.encore.piano.business.TaskQueue;
import com.encore.piano.business.TaskQueue.EnumTask;
import com.encore.piano.exceptions.DatabaseInsertException;
import com.encore.piano.exceptions.EmptyStringException;
import com.encore.piano.exceptions.JSONNullableException;
import com.encore.piano.exceptions.MessageSendingException;
import com.encore.piano.exceptions.MessageSendingUnsuccessfull;
import com.encore.piano.exceptions.NetworkStatePermissionException;
import com.encore.piano.exceptions.NotConnectedException;
import com.encore.piano.exceptions.UrlConnectionException;
import com.encore.piano.interfaces.ProgressUpdateListener;
import com.encore.piano.listview.message.MessageAdapter;
import com.encore.piano.model.MessageModel;
import com.encore.piano.model.ProgressUpdateModel;
import com.encore.piano.util.CommonUtility;

public class Message extends Activity implements OnItemClickListener, OnClickListener, ProgressUpdateListener {

	ListView listviewMessages;
	MessageAdapter adapter;

	Button btnNewMessage;
	Button btnSyncMessages;

	LinearLayout layoutProgress;
	TextView txtTitle = null;
	TextView txtText = null;
	ProgressBar progressBar = null;

	ActionBar actionBar;

	int activeFolder = 0; //MessageFolderEnum.Inbox.Value;

	String UserName = "";
	TextView tvUser = null;

	MessageModel messageToSend = null;

	//    private final BroadcastReceiver mHandlePODMessageReceiver = new BroadcastReceiver() {
	//        @Override
	//        public void onReceive(Context context, Intent intent) {
	//            String messageId = intent.getExtras().getString(EXTRA_MESSAGE);
	//            onSyncMessagesOptionClick();
	//        }
	//    };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);

		listviewMessages = (ListView) findViewById(R.id.listviewMessages);
		btnNewMessage = (Button) findViewById(R.id.btnNewMessage);
		btnSyncMessages = (Button) findViewById(R.id.btnSyncMessages);

		layoutProgress = (LinearLayout) findViewById(R.id.layoutProgress);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtText = (TextView) findViewById(R.id.txtText);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		listviewMessages.setOnItemClickListener(this);
		btnNewMessage.setOnClickListener(this);
		btnSyncMessages.setOnClickListener(this);

		//		registerReceiver(mHandlePODMessageReceiver, new IntentFilter(ACTION_POD_MESSAGE_M));

		UserName = getIntent().getStringExtra("UserName");
		tvUser = (TextView) findViewById(R.id.tvUser);
		tvUser.setText(UserName);

		LoadMessagesBySender(UserName);

	}

	public boolean LoadMessagesBySender(String UserID)
	{

		try
		{
			adapter = new MessageAdapter(Message.this, UserID);
		} catch (UrlConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONNullableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkStatePermissionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listviewMessages.setAdapter(adapter);

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.topmenu_messages, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId()) {
		case R.id.backtomessages:
			this.finish();
			break;
		//		case R.id.messagereadmenureply:
		//			onReplyOptionClick();
		//		break;
		//		case android.R.id.home:		
		//			MessageRead.this.finish();
		//			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);

	}

	public void onMainClicked()
	{
		this.finish();

	}

	public void onRunSheetClicked()
	{
		Intent i = new Intent(this, com.encore.piano.activities.Consignment.class);
		startActivity(i);

	}

	public void onShowMapClicked()
	{
		Intent i = new Intent(this, com.encore.piano.activities.Map.class);
		startActivity(i);

	}

	private void onSyncMessagesOptionClick()
	{
		//new SyncMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		TaskQueue.getQueue().add(EnumTask.SyncMessages);

		processQueue();

	}

	public void processQueue()
	{
		if (TaskQueue.getQueue().peek() != null)
		{
			if (TaskQueue.getQueue().peek() == EnumTask.SyncMessages)
			{
				new SyncMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			}
		}

		if (TaskQueue.getQueue().peek() != null)
		{
			if (TaskQueue.getQueue().peek() == EnumTask.SendMessage)
			{
				new SendMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, messageToSend);
			}
		}

	}

	private void onNewMessageOptionClick()
	{
		Intent i = new Intent(Message.this, com.encore.piano.activities.NewMessage.class);
		i.putExtra("UserName", UserName);
		startActivityForResult(i, ServiceUtility.NEW_MESSAGE_ACTIVITY_REQUEST_CODE);
	}

	ProgressDialog dialog = null;

	class SyncMessages extends AsyncTask<Void, Void, String>
	{

		@Override
		protected void onPreExecute()
		{

			layoutProgress.setVisibility(View.VISIBLE);

			txtTitle.setText("Message synchronization");
			txtText.setText("Initializing...");
			progressBar.setIndeterminate(false);
			progressBar.setMax(5);
			progressBar.setProgress(1);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params)
		{
			try
			{
				if (ServiceUtility.messageService == null)
					ServiceUtility.messageService = new MessageService(getApplicationContext());

				ServiceUtility.messageService.setTaskName("Synchronizing Messages");
				ServiceUtility.messageService.RegisterProgressUpdateListener(Message.this);
				ServiceUtility.messageService.CheckForNewMessages();
				//ServiceUtility.MessageService.SendUnsentMessages();
			} catch (UrlConnectionException e)
			{
				return "UrlConnectionException";
			} catch (JSONException e)
			{
				return "JSONException";
			} catch (JSONNullableException e)
			{
				return "JSONNullableException";
			} catch (NotConnectedException e)
			{
				return "NotConnectedException";
			} catch (NetworkStatePermissionException e)
			{
				return "NetworkStatePermissionException";
			} catch (DatabaseInsertException e)
			{
				return "DatabaseInsertException";
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (result != null && !result.equals(""))
			{
				ShowMessage("Error in Message Synchronziation", result);
			}
			else
			{
				try
				{
					progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.SRC_IN);

					adapter = new MessageAdapter(Message.this, UserName);
				} catch (UrlConnectionException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONNullableException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotConnectedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NetworkStatePermissionException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listviewMessages.setAdapter(adapter);
			}

			CommonUtility.fadeOutView(Message.this, layoutProgress);
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
		case R.id.btnNewMessage:
			onNewMessageOptionClick();
			break;
		case R.id.btnSyncMessages:
			onSyncMessagesOptionClick();
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long arg3)
	{

		String messageId = ((TextView) view.findViewById(R.id.messageId)).getText().toString();

		Intent i = new Intent(Message.this, com.encore.piano.activities.MessageRead.class);
		i.putExtra(ServiceUtility.MESSAGE_INTENT_KEY, messageId);
		startActivityForResult(i, ServiceUtility.MESSAGE_REPLY_ACTIVITY_REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (resultCode == RESULT_OK)
		{
			messageToSend = (MessageModel) data.getSerializableExtra("message");

			TaskQueue.getQueue().add(EnumTask.SendMessage);

			processQueue();

		}

		//if(resultCode == ServiceUtility.MESSAGE_REPLY_ACTIVITY_REQUEST_CODE || resultCode == ServiceUtility.NEW_MESSAGE_ACTIVITY_REQUEST_CODE)
		//	onSyncMessagesOptionClick();

		//		try {
		//			adapter = new MessageAdapter(Message.this, UserName);
		//		} catch (UrlConnectionException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (JSONException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (JSONNullableException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (NotConnectedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (NetworkStatePermissionException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		listviewMessages.setAdapter(adapter);
		//		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ShowMessage(final String title, String message)
	{

		if (layoutProgress.getVisibility() != View.VISIBLE)
		{
			layoutProgress.setVisibility(View.VISIBLE);
		}

		txtTitle.setText(title);
		txtText.setText(message);

		progressBar.getProgressDrawable().setColorFilter(Color.RED, Mode.SRC_IN);
		CommonUtility.fadeOutView(this, layoutProgress);

	}

	@Override
	public void OnProgressUpdateListener(final ProgressUpdateModel model)
	{

		runOnUiThread(new Runnable() {

			@Override
			public void run()
			{
				if (model != null)
				{

					int stepPercentage = model.getStep() / model.getItemsCount() * 100;

					if (stepPercentage == 100)
					{

						if (TaskQueue.getQueue().peek() != null)
						{

							if (TaskQueue.getQueue().peek() == EnumTask.SendMessage)
							{
								TaskQueue.getQueue().remove();
								onSyncMessagesOptionClick();
							}
							else
								TaskQueue.getQueue().remove();

						}

						if (ServiceUtility.messageService != null)
							ServiceUtility.messageService.UnRegisterProgressUpdateListener();

					}

					progressBar.setMax(model.getItemsCount());
					progressBar.setProgress(model.getStep());
					txtTitle.setText(model.getTaskName());
					txtText.setText(model.getTitle() + " - " + model.getMessage());

					//					if(model.getStep() > model.getItemsCount())
					//					{
					//						ShowMessage("Completed", model.getMessage());
					//					}
					//					else
					//					{
					//					updateProgressBar(model.getItemsCount(),model.getStep(), model.getTitle(), model.getMessage());
					//					}
				}
			}
		});

	}

	@Override
	protected void onPause()
	{

		ServiceUtility.messageService.UnRegisterProgressUpdateListener();
		super.onPause();
	}

	//	@Override
	//    protected void onDestroy() {
	//        unregisterReceiver(mHandlePODMessageReceiver);
	//        super.onDestroy();
	//    }

	class SendMessage extends AsyncTask<MessageModel, Void, Object>
	{

		@Override
		protected void onPreExecute()
		{

			txtTitle.setText("Message sending");
			txtText.setText("Initializing...");
			progressBar.setIndeterminate(false);
			progressBar.setMax(5);
			progressBar.setProgress(1);
			progressBar.getProgressDrawable().clearColorFilter();

			super.onPreExecute();
		};

		MessageModel sentModel = null;

		@Override
		protected Object doInBackground(MessageModel... params)
		{
			try
			{

				if (ServiceUtility.messageService == null)
					ServiceUtility.messageService = new MessageService(Message.this);

				ServiceUtility.messageService.setTaskName("Sending Message");
				ServiceUtility.messageService.RegisterProgressUpdateListener(Message.this);
				sentModel = ServiceUtility.messageService.SendMessage(params[0]);

			} catch (ClientProtocolException e)
			{
				return "ClientProtocolException";
			} catch (MessageSendingException e)
			{
				return "MessageSendingException";
			} catch (JSONException e)
			{
				return "JSONException";
			} catch (IOException e)
			{
				return "IOException";
			} catch (JSONNullableException e)
			{
				return "JSONNullableException";
			} catch (EmptyStringException e)
			{
				return "EmptyStringException";
			} catch (MessageSendingUnsuccessfull e)
			{
				return "MessageSendingUnsuccessfull";
			} catch (UrlConnectionException e)
			{
				return "UrlConnectionException";
			} catch (NotConnectedException e)
			{
				return "NotConnectedException";
			} catch (NetworkStatePermissionException e)
			{
				return "NetworkStatePermissionException";
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object result)
		{
			if (result != null && !result.equals(""))
			{
				ShowMessage("Error sending message", "Message not sent.");
			}

			super.onPostExecute(result);
		}
	}

}
