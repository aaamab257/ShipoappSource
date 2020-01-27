package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.middle.east.shipo.Helper.ConnectionHelper;
import com.middle.east.shipo.Helper.CustomDialog;
import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.data.TraderHistory;
import com.middle.east.shipo.ui.Adapters.TraderHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrdersTrader extends AppCompatActivity {
    RecyclerView rectraderorders ;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    Boolean isInternet;
    CustomDialog customDialog;
    ImageView back ;
    ArrayList<TraderHistory> mList ;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    TraderHistoryAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent serviceIntent = new Intent(this, NotificationService.class);
  //      startService(serviceIntent);
        setContentView(R.layout.activity_my_orders_trader);
        IntViews();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllOffers();
    }

    private void getAllOffers() {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("token", SharedHelper.getKey(this,"token"));
                object.put("user", SharedHelper.getKey(this,"user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.TRADERORDERS,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Log.e("RES",response+"");
                    try {
                        JSONArray object = response.getJSONArray("offers");
                        for (int i = 0 ; i < object.length();i++){
                            JSONObject object1 = object.getJSONObject(i);
                            String order_id = object1.optString("id");
                            String order_flat = object1.optString("location_from_latt");
                            String order_tlat = object1.optString("location_to_latt");
                            String order_flng = object1.optString("location_from_long");
                            String order_tlng = object1.optString("location_to_long");
                            String order_time = object1.optString("time");
                            String order_history = object1.optString("history");
                            String order_price = object1.optString("price");
                            String order_fee = object1.optString("fee");
                            String order_city = object1.optString("city");
                            String order_details = object1.optString("details");
                            String order_status = object1.optString("status");
                            String order_createdat = object1.optString("created_at");
                            mList.add(new TraderHistory(order_id , order_flat , order_flng , order_tlat ,order_tlng , order_time , order_history , order_price , order_fee,order_city,order_details,order_status,order_createdat));
                            adapter = new TraderHistoryAdapter(MyOrdersTrader.this , mList);
                            rectraderorders.setLayoutManager(new LinearLayoutManager(MyOrdersTrader.this));
                            rectraderorders.setAdapter(adapter);
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
                        Toast.makeText(MyOrdersTrader.this, "خطأ فى البريد الإلكترونى او كلمة السر", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
                        Toast.makeText(MyOrdersTrader.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(MyOrdersTrader.this);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(MyOrdersTrader.this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(MyOrdersTrader.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void IntViews() {
        back = findViewById(R.id.img_back);
        rectraderorders = findViewById(R.id.recTraderOrders);
        helper = new ConnectionHelper(this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(this);
        if(!isInternet){
            Toast.makeText(this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
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
