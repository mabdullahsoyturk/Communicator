package com.example.muhammet.communicator.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class ServiceUtils {

    public static void addBuyMeService(@NonNull final Context context, String action, String name, String description, String facebook_id, String house_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        context.startService(intent);
    }

    public static void addMemberService(@NonNull final Context context, String action, String facebook_id, String house_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        context.startService(intent);
    }

    public static void addNewHouseService(@NonNull final Context context, String action, String name, String facebook_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("name", name);
        intent.putExtra("facebook_id", facebook_id);
        context.startService(intent);
    }

    public static void addSpendingService(@NonNull final Context context, String action, String name, double cost, String facebook_id, String house_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("name", name);
        intent.putExtra("cost", cost);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        context.startService(intent);
    }

    public static void deleteAllBuyMesService(@NonNull final Context context, String action, String facebook_id, String house_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        context.startService(intent);
    }

    public static void deleteBuyMeService(@NonNull final Context context, String action, String facebook_id, String house_id, String buy_me_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        intent.putExtra("buy_me_id", buy_me_id);
        context.startService(intent);
    }

    public static void deleteSpendingService(@NonNull final Context context, String action, String facebook_id, String house_id, String spending_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        intent.putExtra("spending_id", spending_id);
        context.startService(intent);
    }

    public static void checkHousesService(@NonNull final Context context, String action, String invitation_code, String facebook_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("invitation_code", invitation_code);
        intent.putExtra("facebook_id", facebook_id);;
        context.startService(intent);
    }

    public static void checkUserService(@NonNull final Context context, String action, String first_name, String last_name, String photo_url,String facebook_id) {
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("first_name", first_name);
        intent.putExtra("last_name", last_name);
        intent.putExtra("photo_url", photo_url);
        intent.putExtra("facebook_id", facebook_id);
        context.startService(intent);
    }

    public static void updateUserService(@NonNull final Context context, String action, String facebook_id, String house_id){
        Intent intent = new Intent(context, CommunicatorIntentService.class);
        intent.setAction(action);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        context.startService(intent);

    }

}
