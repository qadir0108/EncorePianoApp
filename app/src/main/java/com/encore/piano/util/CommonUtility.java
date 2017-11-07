package com.encore.piano.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.encore.piano.R;
import com.encore.piano.enums.MessageTypeEnum;

public final class CommonUtility {

	public static final String TAG = "EncorePiano";

	public static final String ACTION_DISPLAY_GCM_MESSAGE = "DISPLAY_GCM_MESSAGE";
	public static final String ACTION_DISPLAY_POD_MESSAGE = "DISPLAY_POD_MESSAGE";

	public static final String ACTION_POD_MESSAGE_M = "POD_MESSAGE_M";
	public static final String ACTION_POD_MESSAGE_C = "POD_MESSAGE_C";

	public static final String ACTION_UPDATE_CONSIGNMENTS = "UPDATE_CONSIGNMENTS";

	/**
	 * Intent's extra that contains the message to be displayed.
	 */
	public static final String EXTRA_MESSAGE = "message";
	
	public static final String FROM_NOTIFICATION = "from_notification";

	public static final String EXTRA_NOTIFICATION_ID = "notificationId";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *          application's context.
	 * @param message
	 *          message to be displayed.
	 */
	public static void displayGCMMessage(Context context, String message)
	{
		Intent intent = new Intent(ACTION_DISPLAY_GCM_MESSAGE);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	public static void displayPODMessage(Context context, String message)
	{
		Intent intent = new Intent(ACTION_DISPLAY_POD_MESSAGE);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	public static void broadCastPODMessage(Context context, String type, String Id, int notificationId)
	{
		Intent intent = null;
		if (type.equals(MessageTypeEnum.Message.Value))
			intent = new Intent(ACTION_POD_MESSAGE_M);
		else if (type.equals(MessageTypeEnum.Consignment.Value))
			intent = new Intent(ACTION_POD_MESSAGE_C);

		intent.putExtra(EXTRA_MESSAGE, Id);
		intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
		context.sendBroadcast(intent);
	}

	public static void showNotification(Context context, Intent notificationIntent, int notificationId, int icon, String title, String message)
	{

		NotificationManager notificationManager = null;
		Builder mBuilder = null;

		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setAutoCancel(true);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

		mBuilder.setSmallIcon(icon);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(message);
		mBuilder.setContentIntent(pendingIntent);

		notificationManager.notify(notificationId, mBuilder.build());

	}

	public static void playSound(Context context)
	{

		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();

	}
	public static void playSoundRinger(Context context)
	{

		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();

	}
	
	public static void fadeOutView(final Context context, final View v)
	{
		final Animation fadeOutAnimation = AnimationUtils.loadAnimation(
				context, R.anim.fadeout);
		fadeOutAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0)
			{
			}

			@Override
			public void onAnimationRepeat(Animation arg0)
			{
			}

			@Override
			public void onAnimationEnd(Animation arg0)
			{
				v.setVisibility(View.GONE);

			}
		});
		// hintImageView.setAnimation(animation);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run()
			{
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						v.startAnimation(fadeOutAnimation);
					}
				});
			}
		}, 5000);
	}
}
