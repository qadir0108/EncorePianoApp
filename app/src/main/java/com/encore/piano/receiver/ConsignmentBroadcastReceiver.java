package com.encore.piano.receiver;

import static com.encore.piano.util.CommonUtility.EXTRA_MESSAGE;
import static com.encore.piano.util.CommonUtility.EXTRA_NOTIFICATION_ID;
import static com.encore.piano.util.CommonUtility.ACTION_POD_MESSAGE_C;
import static com.encore.piano.util.CommonUtility.ACTION_POD_MESSAGE_CD;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.encore.piano.asynctasks.AsyncParams;
import com.encore.piano.asynctasks.DeleteConsignments;
import com.encore.piano.asynctasks.FetchAndStoreConsignments;

public class ConsignmentBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String Id = intent.getExtras().getString(EXTRA_MESSAGE);
		int notificationId = intent.getExtras().getInt(EXTRA_NOTIFICATION_ID);
		
		AsyncParams[] params = {
				new AsyncParams(context, Id, notificationId,  "")
				};
		
		if(intent.getAction().equals(ACTION_POD_MESSAGE_C))
			new FetchAndStoreConsignments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
//		else if(intent.getAction().equals(ACTION_POD_MESSAGE_CD))
//			new deleteAssignments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	}
	
}
