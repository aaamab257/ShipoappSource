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
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.data.DelegatesInfo;
import com.middle.east.shipo.data.TraderHistory;
import com.middle.east.shipo.ui.Adapters.DelegateAdapters;
import com.middle.east.shipo.ui.Adapters.TraderHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DelegateAcceptedOrders extends AppCompatActivity {
    ImageView back ;
    RecyclerView recDele ;
    ArrayList<DelegatesInfo> mList ;
    DelegateAdapters adapters ;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;
    int delay = 10000;
    TextView nOrders ;
    String offerID ;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    String deleID ;
    String status ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_accepted_orders);
        intViews();
        //checkif();
        Intent getID = getIntent();
        offerID = getID.getStringExtra("id");
        deleID = getID.getStringExtra("deleid");
        status = getID.getStringExtra("status");
        Log.e("helloid",offerID);
        CallDelegateAPI(offerID);

    }



    private void CallDelegateAPI(String offerID) {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("offer", offerID);
                object.put("token", SharedHelper.getKey(this,"token"));
                object.put("delegate", SharedHelper.getKey(this,"user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.DELGATEACCEPTED,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }

                    try {
                        JSONArray object = response.getJSONArray("delegates");
                        for (int i = 0 ; i < object.length();i++){
                            JSONObject oob = object.getJSONObject(i);
                            String delegate_id = oob.optString("delegate_id");
                            String offer_id = oob.optString("offer_id");
                            JSONObject oob2 = oob.getJSONObject("delegate");
                            String name = oob2.optString("first_name")+" " +oob2.optString("last_name");
                            Log.e("RESdd",name);
                            String phone = oob2.optString("phone");
                            String rate = oob2.optString("rate");
                            mList.add(new DelegatesInfo(delegate_id ,name,offer_id,phone,rate));
                            adapters = new DelegateAdapters(DelegateAcceptedOrders.this,mList);
                            recDele.setLayoutManager(new LinearLayoutManager(DelegateAcceptedOrders.this ));
                            recDele.setAdapter(adapters);
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
                        Toast.makeText(DelegateAcceptedOrders.this, "خطأ فى البريد الإلكترونى او كلمة السر", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
                        Toast.makeText(DelegateAcceptedOrders.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(DelegateAcceptedOrders.this);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(DelegateAcceptedOrders.this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(DelegateAcceptedOrders.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void intViews() {
        helper = new ConnectionHelper(DelegateAcceptedOrders.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(DelegateAcceptedOrders.this);
        if (!isInternet) {
            Toast.makeText(DelegateAcceptedOrders.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        mList = new ArrayList<>();
        recDele = findViewById(R.id.recDelegate);
        back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
