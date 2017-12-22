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

/**
 * Created by Muhammet on 22.12.2017.
 */

public class NotificationUtilities {

    private static final int SPENDING_NOTIFICATION_ID = 1000;
    private static final int BUY_ME_NOTIFICATION_ID = 1001;
    private static final int SPENDING_PENDING_INTENT_ID = 2000;
    private static final int BUY_ME_PENDING_INTENT_ID = 2001;
    private static final String SPENDING_NOTIFICATION_CHANNEL_ID = "spending_notification_channel";
    private static final String BUY_ME_NOTIFICATION_CHANNEL_ID = "buy_me_notification_channel";

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void remindUserBecauseSpendingAdded(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    SPENDING_NOTIFICATION_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,SPENDING_NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                .setSmallIcon(R.drawable.ic_drink_notification)
//                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.spending_added_notification_title))
                .setContentText(context.getString(R.string.spending_added_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.spending_added_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);
//        notificationBuilder
//                .addAction(drinkWaterAction(context))
//                .addAction(ignoreReminderAction(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(SPENDING_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                SPENDING_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

//    private static Bitmap largeIcon(Context context) {
//
//        Resources res       = context.getResources();
////        Bitmap    largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
//        return largeIcon;
//    }

}
