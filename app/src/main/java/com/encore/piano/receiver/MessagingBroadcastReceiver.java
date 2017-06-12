package com.encore.piano.receiver;

import static com.encore.piano.util.CommonUtility.EXTRA_MESSAGE;
import static com.encore.piano.util.CommonUtility.EXTRA_NOTIFICATION_ID;

import com.encore.piano.asynctasks.AsyncParams;
import com.encore.piano.asynctasks.SyncMessages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class MessagingBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String messageId = intent.getExtras().getString(EXTRA_MESSAGE);
		int notificationId = intent.getExtras().getInt(EXTRA_NOTIFICATION_ID);


        AsyncParams[] params = {
                new AsyncParams(context, messageId, notificationId,  "")
        };
		
		new SyncMessages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params );
		
	}

}
