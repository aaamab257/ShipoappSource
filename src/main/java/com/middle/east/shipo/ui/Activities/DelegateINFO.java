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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class DelegateINFO extends AppCompatActivity {
    TextView deleName, name, phone, email;
    ImageView back;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_info);
        intViews();
        Intent get = getIntent();
        String getID = get.getStringExtra("id");
        String getimage = get.getStringExtra("image");
        if (getimage != null) {
            Picasso.with(DelegateINFO.this).load(getimage).into(userImage);
        } else {
            Picasso.with(DelegateINFO.this).load(R.drawable.newlogo).into(userImage);
        }
        GetDelegateInfo(getID);
    }

    private void GetDelegateInfo(String getID) {
        if (isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            final JSONObject object = new JSONObject();
            try {
                object.put("user", getID);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.DELEGATEINFO, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object1 = response.getJSONObject("delegate");
                        String dname = object1.getString("first_name") + object1.getString("last_name");
                        String demail = object1.getString("email");
                        String dphone = object1.getString("phone");
                        name.setText(dname);
                        email.setText(demail);
                        phone.setText(dphone);
                        deleName.setText(dname);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();


                    Toast.makeText(DelegateINFO.this, "خطأ فى البريد الألكترونى او كلمة السر", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(DelegateINFO.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void intViews() {
        userImage = findViewById(R.id.img_delegate);
        name = findViewById(R.id.txt_delegateName);
        email = findViewById(R.id.txt_delegateemail);
        phone = findViewById(R.id.txt_delegatePhone);
        helper = new ConnectionHelper(DelegateINFO.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(DelegateINFO.this);
        if (!isInternet) {
            Toast.makeText(DelegateINFO.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        deleName = findViewById(R.id.deleName);
        back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
}
