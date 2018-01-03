package com.example.muhammet.communicator.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.muhammet.communicator.MainActivity;
import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.HouseCheckActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.sync.CommunicatorSyncIntentService;
import com.example.muhammet.communicator.sync.CommunicatorSyncTask;

public class NotificationUtilities {

    private static final int SPENDING_NOTIFICATION_ID = 1000;
    private static final int BUY_ME_NOTIFICATION_ID = 1001;
    private static final int SPENDING_PENDING_INTENT_ID = 2000;
    private static final int BUY_ME_PENDING_INTENT_ID = 2001;
    private static final String SPENDING_NOTIFICATION_CHANNEL_ID = "spending_notification_channel";
    private static final String BUY_ME_NOTIFICATION_CHANNEL_ID = "buy_me_notification_channel";
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 3000;

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void remindUserBecauseBuyMeAdded(Context context, String facebook_id, String house_id) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    SPENDING_NOTIFICATION_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,BUY_ME_NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.add_spending_image)
                .setContentTitle(context.getString(R.string.buy_me_added_notification_title))
                .setContentText(context.getString(R.string.buy_me_added_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.buy_me_added_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(buyMeAddedAction(context, facebook_id, house_id))
                .addAction(ignoreBuyMeAddedAction(context, facebook_id, house_id))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(BUY_ME_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, HouseCheckActivity.class);
        return PendingIntent.getActivity(
                context,
                BUY_ME_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static NotificationCompat.Action ignoreBuyMeAddedAction(Context context, String facebook_id, String house_id){
        Intent ignoreBuyMeIntent = new Intent(context, CommunicatorSyncIntentService.class);
        ignoreBuyMeIntent.setAction(CommunicatorSyncTask.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreBuyMePendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreBuyMeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action ignoreBuyMeAction = new NotificationCompat.Action(R.drawable.buy_me_image,
                "Ignore it!",
                ignoreBuyMePendingIntent);

        return ignoreBuyMeAction;

    }


    private static NotificationCompat.Action buyMeAddedAction(Context context, String facebook_id, String house_id){
        Intent intentToSyncImmediately = new Intent(context, CommunicatorSyncIntentService.class);
        intentToSyncImmediately.putExtra("facebook_id", facebook_id);
        intentToSyncImmediately.putExtra("house_id", house_id);
        intentToSyncImmediately.setAction(CommunicatorSyncTask.ACTION_UPDATE_BUY_MES);
        PendingIntent updateBuyMePendingIntent = PendingIntent.getService(
                context,
                BUY_ME_PENDING_INTENT_ID,
                intentToSyncImmediately,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        NotificationCompat.Action updateBuyMeAction = new NotificationCompat.Action(R.drawable.add_spending_image,
                "See it!",
                updateBuyMePendingIntent);

        return updateBuyMeAction;

    }

//    private static Bitmap largeIcon(Context context) {
//
//        Resources res       = context.getResources();
////        Bitmap    largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
//        return largeIcon;
//    }

}
