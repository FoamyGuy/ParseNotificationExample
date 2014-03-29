package com.makemyandroidapp.parsenotificationexample;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
/*****************************
 * This class will receive custom push notifications
 * from parse.com. These are different than the "plain"
 * message push notifications. 
 * 
 * There must be an action defined within the Intent-Filter
 * for this receiver in the manifest.xml file. And the same
 * action must be specified on the notification when it is
 * pushed.
 * 
 * You can optionally pass JSON data from parse.com which will
 * be avaialable in the onReceive() method here.
 *****************************/
public class ParseReceiver extends BroadcastReceiver {
	private final String TAG = "Parse Notification";
	private String msg = "";
	@Override
	public void onReceive(Context ctx, Intent intent) {
		Log.i(TAG, "PUSH RECEIVED!!!");
		
		try {
			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

			Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
			Iterator itr = json.keys();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				Log.d(TAG, "..." + key + " => " + json.getString(key));
				if(key.equals("string")){
					msg = json.getString(key);
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
		
		
		Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(),
                R.drawable.happy);
		
		Intent launchActivity = new Intent(ctx, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(ctx, 0, launchActivity, 0);
		
		Notification noti = new NotificationCompat.Builder(ctx)
        .setContentTitle("PUSH RECEIVED")
        .setContentText(msg)
        .setSmallIcon(R.drawable.happy)
        .setLargeIcon(icon)
        .setContentIntent(pi)
        .setAutoCancel(true)
        .build();
		
		NotificationManager nm = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, noti);

	}

}
