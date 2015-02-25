package com.maalaang.waltz.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.maalaang.waltz.DBHandler;
import com.maalaang.waltz.R;
import com.maalaang.waltz.view.ReceiveActivity;

/**
 * Created by User on 2015-02-06.
 */
public class GcmIntentService extends IntentService
{
    public static final int NOTIFICATION_ID = 1235;

    public GcmIntentService()
    {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty())
        { // has effect of unparcelling Bundle
         /*
          * Filter messages based on message type. Since it is likely that GCM
          * will be extended in the future with new message types, just ignore
          * any message types you're not interested in, or that you don't
          * recognize.
          */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification("Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                String msg = intent.getStringExtra("msg");
                // Post notification of received message.
//            sendNotification("Received: " + extras.toString());
                sendNotification(msg);
                Log.i("GcmIntentService.java | onHandleIntent", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), ReceiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        DBHandler dbHandler = DBHandler.open(this);
        Cursor cursor = dbHandler.show_contact();
        String name = null;
        cursor.moveToFirst();
        while(cursor.getPosition()!=cursor.getCount()){
            if(cursor.getString(1).equals(msg)) {
                name = cursor.getString(2);
                break;
            }
            cursor.moveToNext();
        }
        dbHandler.close();
        if(name == null)
            name = msg;
        intent.putExtra("name", name);
        intent.putExtra("pnum", msg);
        if(msg.equals("hangup")){
            if(ReceiveActivity.activity != null)
                ReceiveActivity.activity.finish();
            msg = "부재중"+name;
        }else{
            startActivity(intent);

        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Waltz")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}