package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.middle.east.shipo.Helper.ConnectionHelper;
import com.middle.east.shipo.Helper.CustomDialog;
import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.Services.MyFirebaseMessagingService;
import com.middle.east.shipo.Services.MyOrderNotifications;
import com.middle.east.shipo.Services.NotificationsDashboard;
import com.middle.east.shipo.data.TraderHistory;
import com.middle.east.shipo.ui.Adapters.NotificationsAdapter;
import com.middle.east.shipo.ui.Adapters.TraderHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Notifications extends AppCompatActivity {
    RecyclerView recNotifi ;
    NotificationsAdapter adapter ;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    ImageView back ;
    Boolean isInternet;
    Handler handler;
    CustomDialog customDialog;
    int delay = 5000;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    public static int lenght ;
    ArrayList<com.middle.east.shipo.data.Notifications> mList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        IntViews();
        Intent intent = new Intent(Notifications.this, NotificationsDashboard.class);
        startService(intent);
        getNoti();
        /*handler.postDelayed(new Runnable() {
            public void run() {

                handler.postDelayed(this, delay);
            }
        }, delay);*/

    }

    private void getNoti() {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("user", SharedHelper.getKey(this , "user_id"));
                object.put("token", SharedHelper.getKey(this , "token"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.NOTIFICATIONS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    mList.clear();
                    try {
                        JSONArray object = response.getJSONArray("notifications");
                        lenght = object.length();
                        for (int i = 0 ; i < object.length();i++){
                            SharedHelper.putKey(Notifications.this , "notilen",""+object.length());
                            JSONObject object1 = object.getJSONObject(i);
                            String nid = object1.optString("id");
                            String ntitle = object1.optString("title");
                            String ndes = object1.optString("description");
                            String status = object1.optString("status");
                            if(status.equals("0")){
                                //getNotifications(ntitle , ndes);
                                mList.add(new com.middle.east.shipo.data.Notifications(nid ,ntitle,ndes));
                                adapter = new NotificationsAdapter(Notifications.this , mList);
                                recNotifi.setLayoutManager(new LinearLayoutManager(Notifications.this));
                                recNotifi.setAdapter(adapter);
                                //SharedHelper.getKey(Notifications.this,"type")
                            }else if(status.equals("1") && SharedHelper.getKey(Notifications.this,"type").equals("1") ){
                                //getNotifications(ntitle , ndes);
                                mList.add(new com.middle.east.shipo.data.Notifications(nid ,ntitle,ndes));
                                adapter = new NotificationsAdapter(Notifications.this , mList);
                                recNotifi.setLayoutManager(new LinearLayoutManager(Notifications.this));
                                recNotifi.setAdapter(adapter);
                            }else if(status.equals("2") && SharedHelper.getKey(Notifications.this,"type").equals("0") ){
                                //getNotifications(ntitle , ndes);
                                mList.add(new com.middle.east.shipo.data.Notifications(nid ,ntitle,ndes));
                                adapter = new NotificationsAdapter(Notifications.this , mList);
                                recNotifi.setLayoutManager(new LinearLayoutManager(Notifications.this));
                                recNotifi.setAdapter(adapter);
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 404) {
                        Toast.makeText(Notifications.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
                        Toast.makeText(Notifications.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(Notifications.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void getNotifications(String ntitle, String ndes) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "2010")
                .setContentTitle(ntitle)
                .setContentText(ndes)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.newlogo)
                .setAutoCancel(true);
        Log.e("Hello","Helloooo");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    private void IntViews() {
        handler = new Handler();
        back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        helper = new ConnectionHelper(Notifications.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(Notifications.this);
        if(!isInternet){
            Toast.makeText(Notifications.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        recNotifi = findViewById(R.id.recNotifications);
        mList = new ArrayList<>();

    }
    @Override
    protected void onResume() {
        super.onResume();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }
}
