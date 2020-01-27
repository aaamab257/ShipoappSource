package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutUs extends AppCompatActivity {
    ImageView back ;
    TextView phone, address , des , email , name;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        IntViews();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        callAPI();
    }

    private void callAPI() {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.ABOUTUS, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Log.e("Responseee" , ""+response);
                    try {
                        JSONObject object = response.getJSONObject("about");
                        String sname = object.optString("name");
                        String semail = object.optString("email");
                        String sphone = object.optString("phone");
                        String saddress = object.optString("address");
                        String sdes = object.optString("description");
                        name.setText(sname);
                        email.setText(semail);
                        phone.setText(sphone);
                        address.setText(saddress);
                        des.setText(sdes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(AboutUs.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void IntViews() {
        helper = new ConnectionHelper(AboutUs.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(AboutUs.this);
        if(!isInternet){
            Toast.makeText(AboutUs.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        back = findViewById(R.id.img_back);
        phone = findViewById(R.id.txt_comanyphone);
        address = findViewById(R.id.txt_companyaddress);
        des = findViewById(R.id.txt_companydes);
        email = findViewById(R.id.txt_companyemail);
        name = findViewById(R.id.txt_companyName);
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
