package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreen extends AppCompatActivity {
    TextView newAcc;
    ImageView back;
    EditText userMail , userPass ;
    CardView login;
    String typeS;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        IntView();
        helper = new ConnectionHelper(LoginScreen.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(LoginScreen.this);
        if(!isInternet){
            Toast.makeText(LoginScreen.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginScreen.this, RegisterActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userMail.getText().toString();
                String pass = userPass.getText().toString();
                if (email.isEmpty()){
                    userMail.setError("ادخل البريد الإلكترونى");
                }else if (pass.isEmpty()){
                    userPass.setError("ادخل كلمة السر");
                }else {
                    Loginfun(email , pass);
                }

            }
        });
    }
    //function to login
    private void Loginfun(final String email, String pass) {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("phone", email);
                object.put("password", pass);
                object.put("firetoken", SharedHelper.getKey(this , "regId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.LOGIN, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    String id = response.optString("user_id");
                    typeS = response.optString("type");
                    String token = response.optString("token");
                    SharedHelper.putKey(LoginScreen.this , "email",email);
                    SharedHelper.putKey(LoginScreen.this , "token",token);
                    SharedHelper.putKey(LoginScreen.this , "first_name",response.optString("first_name"));
                    SharedHelper.putKey(LoginScreen.this , "last_name",response.optString("last_name"));
                    SharedHelper.putKey(LoginScreen.this , "phone",response.optString("phone"));
                    SharedHelper.putKey(LoginScreen.this , "area",response.optString("province"));
                    SharedHelper.putKey(LoginScreen.this , "city",response.optString("city"));
                    SharedHelper.putKey(LoginScreen.this , "user_imageURL",response.optString("imageUrl"));
                    SharedHelper.putKey(LoginScreen.this , "type",response.optString("type"));
                    Log.e("URL",response.optString("imageUrl"));
                    if(typeS.equals("1")){
                        SharedHelper.putKey(LoginScreen.this , "user_id",id);
                        startActivity(new Intent(LoginScreen.this  , HomeActivity.class));
                        SharedHelper.putKey(LoginScreen.this ,"isLoged","true");
                        SharedHelper.putKey(LoginScreen.this ,"isType","1");
                        finish();
                    }else if(typeS.equals("0")){
                        SharedHelper.putKey(LoginScreen.this , "user_id",id);
                        //startService(new Intent(LoginScreen.this, NotificationService.class));
                        startActivity(new Intent(LoginScreen.this  , MandobActivity.class));
                        SharedHelper.putKey(LoginScreen.this ,"isLoged","true");
                        SharedHelper.putKey(LoginScreen.this ,"isType","0");
                        finish();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();


                        Toast.makeText(LoginScreen.this, "خطأ فى البريد الألكترونى او كلمة السر", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(LoginScreen.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void IntView() {
        userMail = findViewById(R.id.ed_usermail);
        userPass = findViewById(R.id.ed_userpass);
        login = findViewById(R.id.btn_login);
        newAcc = findViewById(R.id.txt_newacc);
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
