package com.example.muhammet.communicator.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.muhammet.communicator.activities.BaseActivity;
import com.example.muhammet.communicator.data.CommunicatorContract;
import com.example.muhammet.communicator.utilities.NetworkUtilities;
import com.example.muhammet.communicator.utilities.NotificationUtilities;
import com.example.muhammet.communicator.utilities.SQLiteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.muhammet.communicator.utilities.SQLiteUtils.addBuyMeToLocal;

public class ServiceTasks {

    public static final String ACTION_ADD_BUY_ME = "add-buy-me";
    public static final String ACTION_ADD_MEMBER = "add-member";
    public static final String ACTION_ADD_NEW_HUOSE = "add-new-house";
    public static final String ACTION_ADD_SPENDING = "add-spending";
    public static final String ACTION_DELETE_BUY_ME = "delete-buy-me";
    public static final String ACTION_DELETE_SPENDING = "delete-spending";
    public static final String ACTION_DELETE_ALL_BUY_MES = "delete-all-buy-mes";
    public static final String ACTION_CHECK_USER = "check-user";
    public static final String ACTION_CHECK_HOUSES = "check-houses";
    public static final String ACTION_UPDATE_USER = "update-user";

    synchronized public static void addBuyMe(Context context, String name, String description, String facebook_id, String house_id){

        String url = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/buy_mes";

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      resultJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = addBuyMeToLocal(context, name, description, facebook_id, house_id);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    resultJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", resultJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        NotificationUtilities.remindUserBecauseBuyMeAdded(context);

        Intent intent = new Intent(context,BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        intent.putExtra("fragment", "buy_me_fragment");
        context.startActivity(intent);
    }

    synchronized public static void addMember(Context context, String facebook_id, String house_id){

        String url = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/members";

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      resultJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("house_id", house_id);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    resultJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", resultJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    synchronized public static void addNewHouse(Context context, String name, String facebook_id){
        String url = NetworkUtilities.buildWithFacebookId(facebook_id) + "/houses?facebook_id=" + facebook_id;
        String house_id = "";

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      resultJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = SQLiteUtils.addHouseToLocal(context, name, facebook_id);
            house_id = jsonParam.getString("id");

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    resultJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", resultJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        ServiceUtils.updateUserService(context, ACTION_UPDATE_USER, facebook_id,house_id);
        //UpdateUserTask updateUserTask = new UpdateUserTask(context, facebook_id, house_id);
        //updateUserTask.execute(NetworkUtilities.buildWithFacebookId(facebook_id));

    }

    synchronized public static void addSpending(Context context, String name, double cost, String facebook_id, String house_id){
        String url = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/spendings";

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = SQLiteUtils.addSpendingToLocal(context, name, cost, facebook_id, house_id);

            Log.i("JSON", jsonParam.toString());

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", communicatorJsonStr);
        } catch (IOException e) {
            Log.e("SpendingTask", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        NotificationUtilities.remindUserBecauseSpendingAdded(context);

        Intent intent = new Intent(context, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("facebook_id", facebook_id);
        intent.putExtra("house_id", house_id);
        intent.putExtra("fragment", "spending_fragment");
        context.startActivity(intent);
    }

    synchronized public static void deleteAllBuyMes(Context context, String facebook_id, String house_id){
        String url  = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/buy_mes";

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            context.getContentResolver().delete(
                    CommunicatorContract.BuyMeEntry.CONTENT_URI,
                    null,
                    null);

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", communicatorJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

    synchronized public static void deleteBuyMe(Context context, String facebook_id, String house_id, String buy_me_id){
        String url = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/buy_mes/" + String.valueOf(buy_me_id);

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            Uri uri = CommunicatorContract.BuyMeEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(buy_me_id).build();

            context.getContentResolver().delete(
                    uri,
                    null,
                    null);

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", communicatorJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    synchronized public static void deleteSpending(Context context, String facebook_id, String house_id, String spending_id){
        String url = NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id,house_id) + "/spendings/" + String.valueOf(spending_id);

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            Uri uri = CommunicatorContract.SpendingEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(spending_id).build();

            Log.i("uri", uri.toString());

            context.getContentResolver().delete(
                    uri,
                    null,
                    null);

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", communicatorJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

    synchronized public static void updateUser(Context context, String facebook_id, String house_id){
        String url = NetworkUtilities.buildWithFacebookId(facebook_id);

        HttpURLConnection urlConnection   = null;
        BufferedReader reader          = null;
        String 		      resultJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("house_id", house_id);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    resultJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", resultJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        String success = "";
        try {
            JSONObject resultJson  = new JSONObject(resultJsonStr);
            success = resultJson.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(success.equals("true")){
            Intent intent = new Intent(context, BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("facebook_id", facebook_id);
            intent.putExtra("house_id", String.valueOf(house_id));
            context.startActivity(intent);
        }

    }

    synchronized public static void checkUser(Context context, String first_name, String last_name, String photo_url, String facebook_id){
        String url = NetworkUtilities.STATIC_COMMUNICATOR_URL + "signup";

        HttpURLConnection urlConnection   = null;
        BufferedReader    reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = SQLiteUtils.addMemberToLocal(context, first_name, last_name, photo_url, facebook_id);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("RESULT", communicatorJsonStr);

        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        String success = "";

        JSONObject communicatorJson = null;
        JSONObject jsonObject1 = null;

        try {
            communicatorJson  = new JSONObject(communicatorJsonStr);
            success = communicatorJson.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String houses = "12";

        if (success.equals("false")){

            try {
                jsonObject1 = communicatorJson.getJSONObject("data");
                houses      = jsonObject1.getString("houses");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(houses.length() != 2){
                String house_id = "";
                try {
                    house_id = jsonObject1.getString("house_id");
                    Log.i("houseIdInCheckUser", house_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, BaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("facebook_id", facebook_id);
                intent.putExtra("house_id", house_id);
                context.startActivity(intent);
            }
        }


    }

    synchronized public static void checkHouses(Context context, String invitation_code, String facebook_id){
        String url = NetworkUtilities.STATIC_COMMUNICATOR_URL + "api/users/" + facebook_id + "/houses/" + invitation_code;

        HttpURLConnection urlConnection   = null;
        BufferedReader    reader          = null;
        String 		      communicatorJsonStr = null;

        try {
            URL communicatorURL = new URL(url);
            urlConnection  = (HttpURLConnection) communicatorURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer     = new StringBuffer();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    communicatorJsonStr = buffer.toString();
                }
            }

            Log.i("communicatorJsonStr", communicatorJsonStr);
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainActivity", "Error closing stream", e);
                }
            }
        }

        JSONObject jsonObject = null;
        JSONObject jsonObject1 = null;

        try {
            jsonObject = new JSONObject(communicatorJsonStr);
            String success = jsonObject.getString("success");

            if(success.equals("true")){

                jsonObject1 = jsonObject.getJSONObject("data");
                String house_id = jsonObject1.getString("id");

                Cursor cursorForUser = context.getContentResolver().query(CommunicatorContract.UserEntry.CONTENT_URI,
                        null,
                        "facebook_id=?",
                        new String[]{facebook_id},
                        null);

                if(cursorForUser.moveToFirst()){
                    long user_id = cursorForUser.getLong(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry._ID));
                    String first_name = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FIRST_NAME));
                    String last_name = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_LAST_NAME));
                    double balance = cursorForUser.getDouble(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_BALANCE));
                    String photo_url = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_PHOTO_URL));
                    int status = cursorForUser.getInt(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_STATUS));
                    String created = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_CREATED_TIME));
                    String fid = cursorForUser.getString(cursorForUser.getColumnIndex(CommunicatorContract.UserEntry.COLUMN_FACEBOOK_ID));
                    String hid = house_id;

                    ContentValues cv = new ContentValues();
                    cv.put("_id", user_id);
                    cv.put("first_name", first_name);
                    cv.put("last_name", last_name);
                    cv.put("balance", balance);
                    cv.put("photo_url", photo_url);
                    cv.put("status", status);
                    cv.put("created_time", created);
                    cv.put("facebook_id", fid);
                    cv.put("house_id", hid);

                    context.getContentResolver().update(CommunicatorContract.UserEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(user_id)).build(), cv, null,null);
                }
                cursorForUser.close();

                ServiceUtils.addMemberService(context, ACTION_ADD_MEMBER, facebook_id, house_id);

                //AddMemberTask addMemberTask = new AddMemberTask(context, facebook_id, house_id);
                //addMemberTask.execute(NetworkUtilities.buildWithFacebookIdAndHouseId(facebook_id, house_id) + "/members");

                Intent intent = new Intent(context, BaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("facebook_id", facebook_id);
                intent.putExtra("house_id", house_id);
                context.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
