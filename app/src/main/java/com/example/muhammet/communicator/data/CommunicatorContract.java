package com.example.muhammet.communicator.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CommunicatorContract {

    public static final String UI_UPDATE_BROADCAST = "com.example.muhammet.communicator.uiupdatebroadcast";
    public static final String SERVICE_FINISHED_BROADCAST = "com.example.muhammet.communicator.servicefinishedbroadcast";

    public static final String AUTHORITY = "com.example.muhammet.communicator";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    public static final String PATH_BUY_MES = "buy_mes";
    public static final String PATH_USERS = "users";
    public static final String PATH_HOUSES = "houses";
    public static final String PATH_SPENDINGS = "spendings";
    public static final String PATH_HOUSE_MEMBERS = "house_members";

    public static Uri buildMemberUri(long id) {
        return ContentUris.withAppendedId(UserEntry.CONTENT_URI, id);
    }

    public static Uri buildSpendingUri(long id){
        return ContentUris.withAppendedId(SpendingEntry.CONTENT_URI, id);
    }

    public static Uri buildBuyMeUri(long id){
        return ContentUris.withAppendedId(BuyMeEntry.CONTENT_URI, id);
    }

    public static final class UserEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String TABLE_NAME           = "users";
        public static final String COLUMN_FIRST_NAME    = "first_name";
        public static final String COLUMN_LAST_NAME     = "last_name";
        public static final String COLUMN_BALANCE       = "balance";
        public static final String COLUMN_PHOTO_URL     = "photo_url";
        public static final String COLUMN_STATUS        = "status";
        public static final String COLUMN_FACEBOOK_ID   = "facebook_id";
        public static final String COLUMN_HOUSE_ID      = "house_id";
        public static final String COLUMN_CREATED_TIME  = "created_time";

    }

    public static final class HouseEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSES).build();

        public static final String TABLE_NAME = "houses";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FACEBOOK_ID = "facebook_id";
        public static final String COLUMN_CREATED_TIME = "created_time";
    }

    public static final class BuyMeEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUY_MES).build();

        public static final String TABLE_NAME = "buy_mes";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_FACEBOOK_ID = "facebook_id";
        public static final String COLUMN_HOUSE_ID = "house_id";
        public static final String COLUMN_CREATED_TIME = "created_time";
    }

    public static final class SpendingEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPENDINGS).build();

        public static final String TABLE_NAME = "spendings";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COST = "cost";
        public static final String COLUMN_FACEBOOK_ID = "facebook_id";
        public static final String COLUMN_HOUSE_ID = "house_id";
        public static final String COLUMN_CREATED_TIME = "created_time";
    }

    public static final class HouseMemberEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HOUSE_MEMBERS).build();

        public static final String TABLE_NAME = "house_members";

        public static final String COLUMN_HOUSE_ID = "house_id";
        public static final String COLUMN_FACEBOOK_ID = "facebook_id";
    }
}
