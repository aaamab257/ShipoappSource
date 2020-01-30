package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MandobOrderDetails extends AppCompatActivity {
    ImageView back, oimge, ofrom, oto;
    TextView oname, oprice, ofee, odetails, otime;
    CardView oDoit;
    String name, price, fee, phone, details, id, time, cityf, areaf, addressf, city2, area2, address2;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    RelativeLayout cont , bar ;
    TextView doIT ;
    String ophone ;
    Boolean isInternet;
    CustomDialog customDialog;
    boolean mStopHandler = false;
    int PERMISSION_REQUEST_CODE = 200;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    CardView call;
    Handler mHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delegate_details);
        intViews();

        getFromIntent();
        checkper();
        /*if(SharedHelper.getKey(MandobOrderDetails.this , "Done").equals("IsDaone")){
            doIT.setText("مستعد لأستلام الشحنة");
        }*/

        mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                checkIf();
                if (!mStopHandler) {
                    mHandler.postDelayed(this, 1000);
                }
            }
        };

// start it with:
        mHandler.post(runnable);
        TextView odfrom = findViewById(R.id.addressf);
        TextView odto = findViewById(R.id.address2);
        odfrom.setText(cityf + "," + areaf + "," + addressf);
        odto.setText(city2 + "," + area2 + "," + address2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PutData();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int REQUEST_PHONE_CALL = 1;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + " +2" + phone));
                if (ContextCompat.checkSelfPermission(MandobOrderDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MandobOrderDetails.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(intent);
                }


            }
        });
    }

    private void checkIf() {
        if(isInternet) {
            /*customDialog.setCancelable(false);
            customDialog.show();*/
            JSONObject object = new JSONObject();
            try {
                object.put("delegate", SharedHelper.getKey(this , "user_id"));
                object.put("offer",id);
                //object.put("token", SharedHelper.getKey(this , "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHECK, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    /*if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }*/
                    String mes = response.optString("message");
                    if(mes.equals("0")){
                        oDoit.setEnabled(true);
                        doIT.setText("مستعد لأستلام الشحنة");
                        oDoit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChangeStatusOnWay(id);
                                oDoit.setVisibility(View.GONE);
                            }
                        });
                    }else if(mes.equals("1")){
                        oDoit.setEnabled(false);
                        doIT.setText("لقد قمت بأختيار الشحنة");

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    /*if (customDialog.isShowing()) customDialog.dismiss();*/
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 404) {
                        //Toast.makeText(MandobOrderDetails.this, "لقد قمت بأختيار الشحنة من قبل", Toast.LENGTH_SHORT).show();


                    } else if (response.statusCode == 402) {
                        Toast.makeText(MandobOrderDetails.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
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
                    helper = new ConnectionHelper(MandobOrderDetails.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void checkper() {
        int GET_MY_PERMISSION = 1;
        if(ContextCompat.checkSelfPermission(MandobOrderDetails.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MandobOrderDetails.this,
                    Manifest.permission.CALL_PHONE)){
                /* do nothing*/
            }
            else{

                ActivityCompat.requestPermissions(MandobOrderDetails.this,
                        new String[]{Manifest.permission.CALL_PHONE},GET_MY_PERMISSION);
            }
        }
    }
    private void ChangeStatusOnWay(String id) {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("offer",id);
                object.put("delegate", SharedHelper.getKey(this , "user_id"));
                object.put("token", SharedHelper.getKey(this , "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.ACCEPTORDER, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    final Dialog dialog = new Dialog(MandobOrderDetails.this);
                    dialog.setContentView(R.layout.dialog_order);
                    CardView done = dialog.findViewById(R.id.btn_order_done);
                    ImageView close = dialog.findViewById(R.id.close_dialog);
                    SharedHelper.putKey(MandobOrderDetails.this , "Done","IsDaone");
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 404) {
                        Toast.makeText(MandobOrderDetails.this, "سوف يتواصل معك التاجر", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
                        Toast.makeText(MandobOrderDetails.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
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
                    helper = new ConnectionHelper(MandobOrderDetails.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    /*private void ChangeStatusOffer() {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("offer",id);
                object.put("user", SharedHelper.getKey(this , "user_id"));
                object.put("token", SharedHelper.getKey(this , "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.ORDERDONE, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }

                    Toast.makeText(MandobOrderDetails.this, "تم تأكيد الشحنة", Toast.LENGTH_SHORT).show();

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
                    helper = new ConnectionHelper(MandobOrderDetails.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }*/

    private void PutData() {
        otime.setText(time);
        oname.setText(name);
        oprice.setText(price);
        ofee.setText(fee);
        odetails.setText(details);
    }

    private void getFromIntent() {
        Intent from = getIntent();
        name = from.getStringExtra("name");
        price = from.getStringExtra("price");
        fee = from.getStringExtra("fee");
        phone = from.getStringExtra("phone");
        details = from.getStringExtra("details");
        id = from.getStringExtra("oid");
        time = from.getStringExtra("time");
        cityf = from.getStringExtra("cityf");
        areaf = from.getStringExtra("areaf");
        addressf = from.getStringExtra("addressf");
        city2 = from.getStringExtra("city2");
        area2 = from.getStringExtra("area2");
        address2 = from.getStringExtra("address2");
    }


    private void intViews() {
        /*cont = findViewById(R.id.relative);
        cont.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        bar = findViewById(R.id.toolbar);
        bar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));*/
        doIT = findViewById(R.id.txt_doIt);
        call = findViewById(R.id.btn_ocall);
        helper = new ConnectionHelper(MandobOrderDetails.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(MandobOrderDetails.this);
        if(!isInternet){
            Toast.makeText(MandobOrderDetails.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        otime = findViewById(R.id.txt_otime);
        oDoit = findViewById(R.id.btn_odone);
        oimge = findViewById(R.id.img_odetails);
        ofrom = findViewById(R.id.img_ofrom);
        oto = findViewById(R.id.img_oto);
        oname = findViewById(R.id.txt_oname);
        oprice = findViewById(R.id.txt_oprice);
        ofee = findViewById(R.id.txt_ofee);
        //ophone = findViewById(R.id.txt_ophone);
        odetails = findViewById(R.id.txt_odetails);
        back = findViewById(R.id.img_back);
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
