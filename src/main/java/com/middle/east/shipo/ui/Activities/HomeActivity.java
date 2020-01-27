package com.middle.east.shipo.ui.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.middle.east.shipo.Helper.ConnectionHelper;
import com.middle.east.shipo.Helper.CustomDialog;
import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.data.OnWayTraderOrdersData;
import com.middle.east.shipo.data.PeopleData;
import com.middle.east.shipo.ui.Adapters.AdsAdapter;
import com.middle.east.shipo.ui.Adapters.OnWayAdapter;
import com.middle.east.shipo.ui.Adapters.PeopleAdapter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recPeople, onWAY;
    PeopleAdapter peopleAdapter;
    CardView fabtrader;
    Handler handler;
    private NavigationView navView;
    OnWayAdapter onWayAdapter;
    TextView name, email;
    ArrayList<OnWayTraderOrdersData> monwayList;
    int size = 0 ;
    AdsAdapter adsAdapter;
    TextView MyOrdersTxt;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;
    int delay = 10000;
    TextView nOrders ;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    ArrayList<PeopleData> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        IntView();
        getOnWayOffers();
        //startService(new Intent(this, APINotifications.class));
        //getMandoben();
        LanguageHelper.ChangeLang(getResources(), "ar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_home);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        handler = new Handler();
        if(monwayList.size() > size){
            handler.postDelayed(new Runnable() {
                public void run() {

                    getOnWayOffers();
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }

    }

    private void getOnWayOffers() {
        if (isInternet) {

            JSONObject object = new JSONObject();
            try {
                object.put("token", SharedHelper.getKey(this, "token"));
                object.put("user", SharedHelper.getKey(this, "user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.ONWAYORDERS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Log.e("RES", response + "");
                    monwayList.clear();
                    try {
                        JSONArray object2 = response.getJSONArray("offers");
                        for (int i = 0; i < object2.length(); i++) {
                            if(object2.length() == 0){
                                nOrders.setVisibility(View.VISIBLE);
                            }else{
                                JSONObject object3 = object2.getJSONObject(i);
                                String id = object3.optString("id");
                                String delegate_id = object3.optString("delegate_id");
                                String name = object3.optString("name");
                                String price = object3.optString("price");
                                String details = object3.optString("to_address");
                                String phone = object3.optString("phone");
                                //first_name
                                /*String f_name = obj.optString("first_name");
                                String l_name = obj.optString("last_name");*/

                                //String Rate = obj.optString("rate");

                                monwayList.add(new OnWayTraderOrdersData(id, name, phone,details,price));
                                onWayAdapter = new OnWayAdapter(HomeActivity.this, monwayList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                onWAY.setLayoutManager(mLayoutManager);
                                onWAY.setAdapter(onWayAdapter);
                                nOrders.setVisibility(View.GONE);
                                size = monwayList.size();
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
                    } else if (response.statusCode == 402) {
                        Toast.makeText(HomeActivity.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(HomeActivity.this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(HomeActivity.this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(HomeActivity.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }


    @SuppressLint("RestrictedApi")
    private void IntView() {

        nOrders = findViewById(R.id.noOrders);
        monwayList = new ArrayList<>();
        onWAY = findViewById(R.id.onwayREC);
        mList = new ArrayList<>();
        helper = new ConnectionHelper(HomeActivity.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(HomeActivity.this);
        if (!isInternet) {
            Toast.makeText(HomeActivity.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        MyOrdersTxt = findViewById(R.id.people_name);
        adsAdapter = new AdsAdapter();

        fabtrader = findViewById(R.id.traderadd);
        /*if(SharedHelper.getKey(HomeActivity.this , "type").equals("trader")){
            fabtrader.setVisibility(View.VISIBLE);
            recAds.setVisibility(View.GONE);
            MyOrdersTxt.setText("طلباتى");
            fabtrader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this , CreateOrder.class));
                    *//*final Dialog dialog = new Dialog(HomeActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_add_ads);
                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int width = display.getWidth();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    ImageView close = dialog.findViewById(R.id.close_dialog);
                    CardView btn_add = dialog.findViewById(R.id.btn_addads);
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(HomeActivity.this, "تم اضافة الإعلان", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();*//*
                }
            });
        }else if(SharedHelper.getKey(HomeActivity.this , "type").equals("rep")){
            if (fabtrader.getVisibility()== View.VISIBLE){
                fabtrader.setVisibility(View.GONE);

            }
            recAds.setLayoutManager(new LinearLayoutManager(this));
            recAds.setAdapter(adsAdapter);
        }*/
        fabtrader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CreateOrder.class));
            }
        });
        recPeople = findViewById(R.id.horRecycler);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LanguageHelper.ChangeLang(getResources(), "ar");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LanguageHelper.ChangeLang(getResources(), "ar");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanguageHelper.ChangeLang(getResources(), "ar");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LanguageHelper.ChangeLang(getResources(), "ar");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LanguageHelper.ChangeLang(getResources(), "ar");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        }/*else if (id == R.id.nav_lang) {

        }*/ else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(HomeActivity.this, AboutUs.class));

        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(HomeActivity.this, ContactUs.class));

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));

        } else if (id == R.id.nav_myorders) {
            startActivity(new Intent(HomeActivity.this, MyOrdersTrader.class));

        } else if (id == R.id.nav_logout) {
            SharedHelper.putKey(HomeActivity.this ,"isLoged","");
            SharedHelper.putKey(HomeActivity.this ,"isType","");
            startActivity(new Intent(HomeActivity.this, NavActivity.class));
            finish();
        } else if (id == R.id.nav_mynoti) {
            startActivity(new Intent(HomeActivity.this, Notifications.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
