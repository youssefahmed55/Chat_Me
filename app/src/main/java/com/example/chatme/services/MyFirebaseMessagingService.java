package com.example.chatme.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.chatme.R;
import com.example.chatme.pojo.ContactModel;
import com.example.chatme.ui.HomeActivity;
import com.example.chatme.ui.sharedprefrence.PreferenceRepo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyFirebaseMessagingService extends  FirebaseMessagingService {
    private static final String TAG = "MyFirebaseInstanceIDSer";
    //private SharedPreferences sharedPreferences ;
    @Inject
    PreferenceRepo preferenceRepo;


    @Override
    public void onCreate() {
        super.onCreate();
        //sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
    }
    @Override
    public void handleIntent(Intent intent) {
        //add a log, and you'll see the method will be triggered all the time (both foreground and background).
        Log.d( "FCM", "handleIntent");

        //if you don't know the format of your FCM message,
        //just print it out, and you'll know how to parse it
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("FCM", "Key: " + key + " Value: " + value);
            }
        }

        //the background notification is created by super method
        //but you can't remove the super method.
        //the super method do other things, not just creating the notification
        super.handleIntent(intent);

        //remove the Notificaitons
        removeFirebaseOrigianlNotificaitons();

        if (bundle ==null)
            return;

        //pares the message

        String body = (String) bundle.get("gcm.notification.body");
        String title = "";
        if(bundle.containsKey("gcm.notification.title"))
          title = (String) bundle.get("gcm.notification.title");

        String title2 ="";
        if(preferenceRepo.getArrayOfContactsSharedPreferences()!=null) {
            for (ContactModel contactModel : preferenceRepo.getArrayOfContactsSharedPreferences()) {
                if (title.equals(contactModel.getNum().substring(1))) {
                    title2 = contactModel.getName();
                    break;
                }
            }

            if (title2.equals("")) {
                title2 = "+" + title;
            }
            sendNotification(body, title2);
        }
    }




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "remoteMessage.getNotification(): "  + remoteMessage.getNotification().getBody());


           /* String title = remoteMessage.getNotification().getTitle();
            String title2 ="";
            for (ContactModel contactModel : getArrayOfContactsSharedPreferences()){
                if (title.equals(contactModel.getNum().substring(1))) {
                    title2 = contactModel.getName();
                    break;
                }
            }

            if (title2.equals("")){title2 = "+" + title;}

            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "onMessageReceived: " + title + "-" + title2 + "=" + body);
            sendNotification(body,title2);*/

        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    private void sendNotification(String messageBody , String title) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "My channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    private void removeFirebaseOrigianlNotificaitons() {

        //check notificationManager is available
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null )
            return;

        //check api level for getActiveNotifications()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //if your Build version is less than android 6.0
            //we can remove all notifications instead.
            //notificationManager.cancelAll();
            return;
        }


        //check there are notifications
        StatusBarNotification[] activeNotifications =
                notificationManager.getActiveNotifications();
        if (activeNotifications == null)
            return;

        //remove all notification created by library(super.handleIntent(intent))
        for (StatusBarNotification tmp : activeNotifications) {
            Log.d("FCM StatusBarNo",
                    "StatusBarNotification tag/id: " + tmp.getTag() + " / " + tmp.getId());
            String tag = tmp.getTag();
            int id = tmp.getId();

            //trace the library source code, follow the rule to remove it.
            if (tag != null && tag.contains("FCM-Notification"))
                notificationManager.cancel(tag, id);
        }
    }
}
