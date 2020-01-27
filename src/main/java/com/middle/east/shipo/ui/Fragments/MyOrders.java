package com.middle.east.shipo.ui.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.middle.east.shipo.Services.MyOrderNotifications;
import com.middle.east.shipo.data.MyOrdersData;
import com.middle.east.shipo.ui.Activities.MandobActivity;
import com.middle.east.shipo.ui.Adapters.MyOrdersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrders extends Fragment {
    RecyclerView recMyOrders ;
    View v ;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    MyOrdersAdapter myOrdersAdapter ;
    ArrayList<MyOrdersData> mList ;
    Handler handler;
    int delay = 10000;
    public MyOrders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_my_orders, container, false);
        IntiViews();
        Intent intent = new Intent(getContext(), MyOrderNotifications.class);
        getActivity().startService(intent);
        getOrders();
        handler.postDelayed(new Runnable() {
            public void run() {

                //getOrders();
                handler.postDelayed(this, delay);
            }
        }, delay);


        return v ;
    }

    private void getOrders() {
        if(isInternet) {

            if(mList.size() > 0){
                mList.clear();
            }
            JSONObject object = new JSONObject();
            try {
                object.put("token", SharedHelper.getKey(getContext(),"token"));
                object.put("user", SharedHelper.getKey(getContext(),"user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.GetOrders,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray array = response.getJSONArray("offers");
                        SharedHelper.putKey(getContext(),"length",""+array.length());
                        /*Intent intent = new Intent(getContext(), MyOrderNotifications.class);
                        getActivity().startService(intent);*/
                        for (int i = 0 ; i < array.length() ; i ++ ){
                            JSONObject object1 = array.getJSONObject(i);
                            String status = object1.optString("status");
                            if(status.equals("1")){
                                String order_id = object1.optString("id");
                                String order_date = object1.optString("date");
                                String order_time = object1.optString("time");
                                String order_price = object1.optString("price");
                                String order_fee = object1.optString("fee");
                                String order_details = object1.optString("details");
                                String name = object1.optString("name");
                                String order_phone = object1.optString("phone");
                                String fcity = object1.optString("fromprovince");
                                String farea = object1.optString("fromcity");
                                String faddress = object1.optString("from_address");
                                JSONObject oobo = object1.getJSONObject("to_province");
                                String tcity = oobo.optString("governorate_name");
                                String tarea = object1.optString("tocity");
                                String taddress = object1.optString("to_address");
                                String order_userID = object1.optString("user_id");
                                mList.add(new MyOrdersData(order_id , order_date , order_time , order_price ,order_fee , order_details , "تم اختيارك" , name , order_phone,fcity, farea , faddress,tcity, tarea , taddress,"",order_userID));
                                myOrdersAdapter = new MyOrdersAdapter(getContext() , mList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recMyOrders.setLayoutManager(mLayoutManager);
                                recMyOrders.setAdapter(myOrdersAdapter);
                            }else if(status.equals("2")){
                                String order_id = object1.optString("id");
                                String order_date = object1.optString("date");
                                String order_time = object1.optString("time");
                                String order_price = object1.optString("price");
                                String order_fee = object1.optString("fee");
                                String order_details = object1.optString("details");
                                String name = object1.optString("name");
                                String order_phone = object1.optString("phone");
                                String fcity = object1.optString("fromprovince");
                                String farea = object1.optString("fromcity");
                                String faddress = object1.optString("from_address");
                                JSONObject oobo = object1.getJSONObject("to_province");
                                String tcity = oobo.optString("governorate_name");
                                String tarea = object1.optString("tocity");
                                String taddress = object1.optString("to_address");
                                String order_userID = object1.optString("user_id");
                                mList.add(new MyOrdersData(order_id , order_date , order_time , order_price ,order_fee , order_details , "جارى توصيله" , name , order_phone,fcity, farea , faddress,tcity, tarea , taddress,"",order_userID));
                                myOrdersAdapter = new MyOrdersAdapter(getContext() , mList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recMyOrders.setLayoutManager(mLayoutManager);
                                recMyOrders.setAdapter(myOrdersAdapter);
                            }else if(status.equals("3")){
                                String order_id = object1.optString("id");
                                String order_date = object1.optString("date");
                                String order_time = object1.optString("time");
                                String order_price = object1.optString("price");
                                String order_fee = object1.optString("fee");
                                String order_details = object1.optString("details");
                                String name = object1.optString("name");
                                String order_phone = object1.optString("phone");
                                String fcity = object1.optString("fromprovince");
                                String farea = object1.optString("fromcity");
                                String faddress = object1.optString("from_address");
                                JSONObject oobo = object1.getJSONObject("to_province");
                                String tcity = oobo.optString("governorate_name");
                                String tarea = object1.optString("tocity");
                                String taddress = object1.optString("to_address");
                                String order_userID = object1.optString("user_id");
                                mList.add(new MyOrdersData(order_id , order_date , order_time , order_price ,order_fee , order_details , "تم تسليمه" , name , order_phone,fcity, farea , faddress,tcity, tarea , taddress,"",order_userID));
                                myOrdersAdapter = new MyOrdersAdapter(getContext() , mList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recMyOrders.setLayoutManager(mLayoutManager);
                                recMyOrders.setAdapter(myOrdersAdapter);
                            }else if(status.equals("4")){
                                String order_id = object1.optString("id");
                                String order_date = object1.optString("date");
                                String order_time = object1.optString("time");
                                String order_price = object1.optString("price");
                                String order_fee = object1.optString("fee");
                                String order_details = object1.optString("details");
                                String name = object1.optString("name");
                                String order_phone = object1.optString("phone");
                                String fcity = object1.optString("fromprovince");
                                String farea = object1.optString("fromcity");
                                String faddress = object1.optString("from_address");
                                JSONObject oobo = object1.getJSONObject("to_province");
                                String tcity = oobo.optString("governorate_name");
                                String tarea = object1.optString("tocity");
                                String taddress = object1.optString("to_address");
                                String order_userID = object1.optString("user_id");
                                String reason = object1.optString("reason");
                                mList.add(new MyOrdersData(order_id , order_date , order_time , order_price ,order_fee , order_details , "ملغى" , name , order_phone,fcity, farea , faddress,tcity, tarea , taddress,reason,order_userID));
                                myOrdersAdapter = new MyOrdersAdapter(getContext() , mList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recMyOrders.setLayoutManager(mLayoutManager);
                                recMyOrders.setAdapter(myOrdersAdapter);

                            }


                            /**/
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
                        Toast.makeText(getContext(), "خطأ فى البريد الإلكترونى او كلمة السر", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
                        Toast.makeText(getContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(getContext(), "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(getContext());
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }



    private void IntiViews() {
        handler = new Handler();
        recMyOrders = v.findViewById(R.id.user_orders);
        helper = new ConnectionHelper(getContext());
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(getContext());
        if(!isInternet){
            Toast.makeText(getContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        mList = new ArrayList<MyOrdersData>();
        /**/
    }

}
