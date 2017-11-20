package com.example.muhammet.communicator.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Muhammet on 20.11.2017.
 */

public class CommunicatorContract {

    public static final String AUTHORITY = "com.example.muhammet.communicator";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_BUY_MES = "buy_mes";

    public static final class BuyMeEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUY_MES).build();

        public static final String TABLE_NAME = "buy_mes";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

    }

}
