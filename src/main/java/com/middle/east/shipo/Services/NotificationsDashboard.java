package com.middle.east.shipo.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.middle.east.shipo.Helper.ConnectionHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.ui.Activities.MandobActivity;
import com.middle.east.shipo.ui.Activities.Notifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationsDashboard extends Service {
    TimerTask timerTask;
    Timer timer;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;


    public static final String NOTIFICATION_CHANNEL_ID = "10002";
    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        //onTaskRemoved(intent);

        startTimer();

        return START_STICKY;
    }
    final Handler handler = new Handler();
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        CallApi();

                    }
                });
            }
        };
    }
    private void CallApi() {
        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
        if (isInternet) {

            final JSONObject object = new JSONObject();
            try {
                object.put("token", SharedHelper.getKey(getApplicationContext(), "token"));
                object.put("user", SharedHelper.getKey(getApplicationContext(), "user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.NOTIFICATIONS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray array = response.getJSONArray("notifications");
                        //Toast.makeText(getApplicationContext(), "New", Toast.LENGTH_SHORT).show();
                        int len = Integer.parseInt(SharedHelper.getKey(getApplicationContext(), "notilen"));
                        if (array.length() > len) {
                                JSONObject object1 = array.getJSONObject(array.length()-1);
                                String status = object1.optString("status");
                            if(status.equals("0")){
                                String ntitle = object1.optString("title");
                                String ndes = object1.optString("description");
                                createNotification(ntitle , ndes);
                            }else if(status.equals("1") && SharedHelper.getKey(getApplicationContext(),"type").equals("1")){
                                String ntitle = object1.optString("title");
                                String ndes = object1.optString("description");
                                createNotification(ntitle , ndes);
                            }else if(status.equals("2") && SharedHelper.getKey(getApplicationContext(),"type").equals("0")){
                                String ntitle = object1.optString("title");
                                String ndes = object1.optString("description");
                                createNotification(ntitle , ndes);
                            }




                            Log.e("TAGSd", "NewOrderAdded");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        } else {

        }

    }


    private void createNotification(String ntitle, String ndes) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = null;
        notificationIntent = new Intent(getApplicationContext(), Notifications.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = null;

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.newlogo);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.newlogo)
                    .setLargeIcon(bitmap1)
                    .setContentTitle(ntitle)
                    .setContentText(ndes)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.newlogo)
                    .setContentTitle(ntitle)
                    .setContentText(ndes)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        //Random random = new Random();
        int notificationID = 0;
        notificationManager.notify(notificationID, notificationBuilder.build());

    }
    @Override
    public void onDestroy() {
        stoptimertask();
        super.onDestroy();
    }
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public IBinder onBind(Intent arg0) {
        return null;
    }
}