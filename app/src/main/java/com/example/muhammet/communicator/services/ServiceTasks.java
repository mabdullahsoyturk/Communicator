package com.example.muhammet.communicator.services;

import android.content.Context;

public class ServiceTasks {

    public static final String ACTION_ADD_BUY_ME = "add-buy-me";
    public static final String ACTION_ADD_MEMBER = "add-member";
    public static final String ACTION_ADD_NEW_HUOSE = "add-new-house";
    public static final String ACTION_ADD_SPENDING = "add-spending";

    synchronized public static void addBuyMe(Context context, String name, String description, String facebook_id, String house_id){

    }

    synchronized public static void addMember(Context context, String facebook_id, String house_id){

    }

    synchronized public static void addNewHouse(Context context, String name, String facebook_id){

    }

    synchronized public static void addSpending(Context context, String name, double cost, String facebook_id, String house_id){

    }
}
