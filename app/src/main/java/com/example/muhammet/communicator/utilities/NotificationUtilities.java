package com.example.muhammet.communicator.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.muhammet.communicator.R;
import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.activities.HouseCheckActivity;
import com.example.muhammet.communicator.services.CommunicatorIntentService;
import com.example.muhammet.communicator.services.ServiceTasks;
import com.example.muhammet.communicator.sync.CommunicatorSyncIntentService;
import com.example.muhammet.communicator.sync.CommunicatorSyncTask;

public class NotificationUtilities {

    private static final int BUY_ME_PENDING_INTENT_ID = 3417;
    private static final int IGNORE_BUY_ME_PENDING_INTENT_ID = 3418;
    private static final int CHECK_BUY_ME_PENDING_INTENT_ID = 3419;
    private static final int IGNORE_SPENDING_PENDING_INTENT_ID = 3420;
    private static final int CHECK_SPENDING_PENDING_INTENT_ID = 3421;
    private static final int IGNORE_SHOP_PENDING_INTENT_ID = 3422;
    private static final int CHECK_SHOP_PENDING_INTENT_ID = 3423;

    private static final int BUY_ME_NOTIFICATION_ID = 3500;
    private static final int SPENDING_NOTIFICATION_ID = 3501;
    private static final int SHOPPING_NOTIFICATION_ID = 3502;

    private static final String BUY_ME_NOTIFICATION_CHANNEL_ID = "buy_me_notification_channel";
    private static final String SPENDING_NOTIFICATION_CHANNEL_ID = "spending_notification_channel";
    private static final String SHOPPING_NOTIFICATION_CHANNEL_ID = "shopping_notification_channel";

    public static void clearAllNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void remindUserForShoppingDay(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    SHOPPING_NOTIFICATION_CHANNEL_ID,
                    "CHANNEL NAME3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, SHOPPING_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_account_box_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Shopping Day!")
                .setContentText("It's your shopping day! Do not forget it!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Buy things"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(checkShopAction(context))
                .addAction(ignoreShopAction(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(SHOPPING_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void remindUserBecauseSpendingAdded(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    SPENDING_NOTIFICATION_CHANNEL_ID,
                    "CHANNEL NAME2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, SPENDING_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_account_box_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Spending")
                .setContentText("Spending Added")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("My body"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreSpendingAction(context))
                .addAction(checkSpendingAction(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(SPENDING_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void remindUserBecauseBuyMeAdded(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    BUY_ME_NOTIFICATION_CHANNEL_ID,
                    "CHANNEL NAME",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, BUY_ME_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_account_box_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("New Buy Me")
                .setContentText("A new buy me added")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("My body"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreBuyMeAction(context))
                .addAction(checkBuyMeAction(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(BUY_ME_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static NotificationCompat.Action ignoreShopAction(Context context){
        Intent ignoreShopIntent = new Intent(context, CommunicatorSyncIntentService.class);
        ignoreShopIntent.setAction(CommunicatorSyncTask.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreShopPendıngIntent = PendingIntent.getService(
                context,
                IGNORE_SHOP_PENDING_INTENT_ID,
                ignoreShopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action ignoreShopAction = new NotificationCompat.Action(R.drawable.ic_clear_all_black_24dp,
                "Ignore",
                ignoreShopPendıngIntent);

        return ignoreShopAction;
    }

    private static NotificationCompat.Action checkShopAction(Context context){
        Intent checkShopIntent = new Intent(context, CommunicatorSyncIntentService.class);
        checkShopIntent.setAction(CommunicatorSyncTask.ACTION_SHOP_DAY);

        PendingIntent checkShopPendıngIntent = PendingIntent.getService(
                context,
                CHECK_SHOP_PENDING_INTENT_ID,
                checkShopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action checkShopAction = new NotificationCompat.Action(R.drawable.ic_clear_all_black_24dp,
                "Check",
                checkShopPendıngIntent);

        return checkShopAction;
    }

    private static NotificationCompat.Action ignoreBuyMeAction(Context context){
        Intent ignoreBuyMeIntent = new Intent(context, CommunicatorSyncIntentService.class);
        ignoreBuyMeIntent.setAction(CommunicatorSyncTask.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreBuyMePendıngIntent = PendingIntent.getService(
                context,
                IGNORE_BUY_ME_PENDING_INTENT_ID,
                ignoreBuyMeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action ignoreBuyMeAction = new NotificationCompat.Action(R.drawable.ic_clear_all_black_24dp,
                "Ignore",
                ignoreBuyMePendıngIntent);

        return ignoreBuyMeAction;
    }

    private static NotificationCompat.Action checkBuyMeAction(Context context){
        Intent checkBuyMeIntent = new Intent(context, CommunicatorSyncIntentService.class);
        checkBuyMeIntent.setAction(CommunicatorSyncTask.ACTION_CHECK_BUY_MES);

        PendingIntent checkBuyMePendıngIntent = PendingIntent.getService(
                context,
                CHECK_BUY_ME_PENDING_INTENT_ID,
                checkBuyMeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action checkBuyMeAction = new NotificationCompat.Action(R.drawable.ic_clear_all_black_24dp,
                "Check",
                checkBuyMePendıngIntent);

        return checkBuyMeAction;
    }

    private static NotificationCompat.Action ignoreSpendingAction(Context context){
        Intent ignoreSpendingIntent = new Intent(context, CommunicatorSyncIntentService.class);
        ignoreSpendingIntent.setAction(CommunicatorSyncTask.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreSpendingPendıngIntent = PendingIntent.getService(
                context,
                IGNORE_SPENDING_PENDING_INTENT_ID,
                ignoreSpendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action ignoreSpendingAction = new NotificationCompat.Action(R.drawable.ic_clear_all_black_24dp,
                "Ignore",
                ignoreSpendingPendıngIntent);

        return ignoreSpendingAction;
    }

    private static NotificationCompat.Action checkSpendingAction(Context context){
        Intent checkSpendingIntent = new Intent(context, CommunicatorSyncIntentService.class);
        checkSpendingIntent.setAction(CommunicatorSyncTask.ACTION_CHECK_SPENDINGS);

        PendingIntent checkSpendingPendingIntent = PendingIntent.getService(
                context,
                CHECK_SPENDING_PENDING_INTENT_ID,
                checkSpendingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Action checkSpendingAction = new NotificationCompat.Action(R.drawable.ic_clear_all_black_24dp,
                "Check",
                checkSpendingPendingIntent);

        return checkSpendingAction;
    }

    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, BaseActivity.class);

        return PendingIntent.getActivity(
                context,
                BUY_ME_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Bitmap largeIcon(Context context){

        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.app_icon);

        return largeIcon;
    }

}
