package com.encore.piano.fcm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.encore.piano.R;
import com.encore.piano.activities.Consignment;
import com.encore.piano.activities.Conversation;
import com.encore.piano.asynctasks.AsyncParams;
import com.encore.piano.asynctasks.FetchAndStoreConsignments;
import com.encore.piano.enums.MessageTypeEnum;
import com.encore.piano.util.AlertUtility;
import com.encore.piano.util.CommonUtility;
import com.encore.piano.util.StringUtility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

import static com.encore.piano.util.CommonUtility.ACTION_POD_MESSAGE_C;
import static com.encore.piano.util.CommonUtility.EXTRA_MESSAGE;
import static com.encore.piano.util.CommonUtility.FROM_NOTIFICATION;
/**
 * Created by Administrator on 9/6/2017.
 */

public class FirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "EncorePiano";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        CommonUtility.playSound(this);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();
            if (StringUtility.compare(data.get("MessageType"), MessageTypeEnum.Consignment.Value)) {
                String id = (String)data.get(MessageTypeEnum.Id.Value);

                AsyncParams[] params = {
                        new AsyncParams(getApplicationContext(), id, 1123,  "")
                };

                int notificationId = generateNotification(getApplicationContext(), MessageTypeEnum.Consignment.Value, id);
                CommonUtility.broadCastPODMessage(getApplicationContext(), MessageTypeEnum.Consignment.Value, id, notificationId);
            }
            else if (StringUtility.compare(data.get("MessageType"), MessageTypeEnum.Message.Value)) {

            }

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private static int generateNotification(Context context, String type, String Id) {

        int notificationId = ("Message" + new Date().toString()).hashCode();

        int icon = 0;

        String title = "";
        String message = "";

        Intent notificationIntent = null;
        if(type.equals(MessageTypeEnum.Message.Value))
        {
            notificationIntent = new Intent(context, Conversation.class);
            icon = R.drawable.ic_menu_compose;
            title = "New Message!";
            message = "New message received from Server";
        }
        else if (type.equals(MessageTypeEnum.Consignment.Value))
        {
            notificationIntent = new Intent(context, Consignment.class);
            icon = R.drawable.ic_menu_copy;
            title = "New Pickup Order!";
            message = "New Pickup Order received from Server";
        }
        else if (type.equals(MessageTypeEnum.Consignment.Value))
        {
            notificationIntent = new Intent(context, Consignment.class);
            icon = R.drawable.ic_menu_delete;
            title = "Consignments Deleted";
            message = "Consignments are deleted from Server";
        }

        notificationIntent.putExtra(EXTRA_MESSAGE, Id);
        notificationIntent.putExtra(FROM_NOTIFICATION, "yes");

        CommonUtility.showNotification(context, notificationIntent, notificationId, icon, title, message);

        return notificationId;

    }
}
