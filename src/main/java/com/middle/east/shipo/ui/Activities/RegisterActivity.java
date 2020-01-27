package com.middle.east.shipo.ui.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public class RegisterActivity extends AppCompatActivity {
    CustomDialog dialog;
    CardView regis;
    ImageView back, imagedis;
    TextView choose;
    EditText fname, phone, pass, email, location, lname;
    String fullname, sphone, spass, semail, slocation, slname, des, encodedImage;
    Double latt, lngt;
    RelativeLayout openmap;
    RequestQueue requestQueue;
    Bitmap user_imgn;
    ConnectionHelper helper;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    int PERMISSION_REQUEST_CODE = 200;
    private final int GALLERY = 2;
    Spinner CitySp , AreaSp ;
    ArrayList<String> CountryName , CountryId , CityName , CityId;
    String cityid , areaid ,CountryZipCode="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent get = getIntent();
        String pphone = get.getStringExtra("Phone");
        IntViews();
        phone.setText(pphone);
        phone.setEnabled(false);
        CitySp = findViewById(R.id.sp_mohafza);
        AreaSp = findViewById(R.id.sp_area);
        loadSpinnerData();
        GetCountryZipCode();
        CitySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityid = CountryId.get(i);
                LoadAreaSpinner(cityid);
                CityName.clear();
                CityId.clear();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        AreaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                areaid = CityId.get(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionlocation()) {
                    Intent i = new Intent(RegisterActivity.this, Map.class);
                    startActivityForResult(i, 1);
                } else {
                    requestPermissionlocation();
                }
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = fname.getText().toString();
                sphone = phone.getText().toString();
                spass = pass.getText().toString();
                slocation = location.getText().toString();
                slname = lname.getText().toString();
                if (fullname.isEmpty()) {
                    fname.setError("ادخل الاسم");
                } else if (sphone.isEmpty()) {
                    phone.setError("ادخل الهاتف ");
                } else if (spass.isEmpty()) {
                    pass.setError("ادخل كلمة المرور ");
                }   else if (slname.isEmpty()) {
                    location.setError("رجاء ادخل الاسم الثانى");
                } else {
                    RegisterNewAccount(fullname, sphone, spass, semail, slocation, slname);
                }
                /*startActivity(new Intent(RegisterActivity.this , NavActivity.class));*/
            }


        });
    }
    public String GetCountryZipCode(){
        String CountryID="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }
    private void LoadAreaSpinner(String cityid) {
        if (isInternet) {

            JSONObject object = new JSONObject();
            try {
                object.put("province_id", cityid);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CITIES, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray("cities");
                        for(int i=0;i<array.length();i++){
                            JSONObject jsonObject1=array.getJSONObject(i);
                            String city =jsonObject1.getString("city_name");
                            Log.e("Cite",city);
                            String id= jsonObject1.getString("id");
                            Log.e("id123",id);
                            CityName.add(city);
                            CityId.add(id);
                        }
                        AreaSp.setAdapter(new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1, CityName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(RegisterActivity.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(RegisterActivity.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void loadSpinnerData() {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLHelper.PROVINCES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("id123",response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("provinces");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String city =jsonObject1.getString("governorate_name");
                            Log.e("Cite",city);
                            String id= jsonObject1.getString("id");
                            Log.e("id123",id);
                            CountryName.add(city);
                            CountryId.add(id);
                        }
                    CitySp.setAdapter(new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, CountryName));

                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void RegisterNewAccount( String fullname,  String sphone,  String spass,  String semail, String slocation,  String slname) {

        if (isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (user_imgn != null) {
                user_imgn.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } else {
                user_imgn = BitmapFactory.decodeResource(RegisterActivity.this.getResources(),
                        R.drawable.newlogo);
                encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            }
                JSONObject object = new JSONObject();
                try {
                    object.put("first_name", fullname);
                    object.put("last_name", slname);
                    object.put("city_id", areaid);
                    object.put("province_id", cityid);
                    object.put("type", SharedHelper.getKey(this,"user"));
                    object.put("password", spass);
                    object.put("phone", sphone);
                    object.put("image", "data:image/jpeg;base64,"+encodedImage);
                    Log.e("IMAGE","data:image/jpeg;base64,"+encodedImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.REGISTER, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (customDialog.isShowing()) {
                            customDialog.dismiss();
                        }
                        Toast.makeText(RegisterActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this , LoginScreen.class));
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (customDialog.isShowing()) customDialog.dismiss();

                        Toast.makeText(RegisterActivity.this, "رقم الهاتف تم تسجيل من قبل", Toast.LENGTH_SHORT).show();


                    }
                });
                requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(jsonObjectRequest);
            } else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(RegisterActivity.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


    }
        private void IntViews () {
            CountryName=new ArrayList<>();
            CountryId = new ArrayList<>();
            CityName = new ArrayList<>();
            CityId = new ArrayList<>();
            imagedis = findViewById(R.id.img_user);
            choose = findViewById(R.id.choose_image);
            helper = new ConnectionHelper(RegisterActivity.this);
            isInternet = helper.isConnectingToInternet();
            customDialog = new CustomDialog(RegisterActivity.this);
            if (!isInternet) {
                Toast.makeText(RegisterActivity.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
            }
            openmap = findViewById(R.id.relLocatoin);
            lname = findViewById(R.id.lastname);
            fname = findViewById(R.id.fullname);
            phone = findViewById(R.id.user_phone);
            pass = findViewById(R.id.user_pass);
            email = findViewById(R.id.user_email);
            location = findViewById(R.id.user_location);
            back = findViewById(R.id.backimg);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            dialog = new CustomDialog(RegisterActivity.this);
            regis = findViewById(R.id.btn_regis);

        }

        @Override
        protected void onPause () {
            super.onPause();
            dialog.dismiss();
            LanguageHelper.ChangeLang(getResources(),"ar");
        }
        private boolean checkPermissionlocation () {
            int result2 = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
            return result2 == PackageManager.PERMISSION_GRANTED;
        }
        private void requestPermissionlocation () {

            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

        }
        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RegisterActivity.this.RESULT_CANCELED) {
                return;
            }
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    latt = data.getDoubleExtra("lat", 0);
                    lngt = data.getDoubleExtra("lng", 0);
                    String address = "";
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latt, lngt, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        // Log.d("safrsaf", "nvklv" + addresses.get(0).getCountryCode());
                        des = addresses.get(0).getLocality();
                        Log.e("dister", des);
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        Log.d("safrsaf", "nvklv" + address);
                        location.setText("" + address);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("safrsaf", "nvklv" + e.getMessage());
                    }
                }
            } else if (requestCode == GALLERY) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        imagedis.setImageBitmap(bitmap);
                        user_imgn = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "خطأ فى رفع الصورة!", Toast.LENGTH_SHORT).show();
                    }
                }

            } else if (requestCode == CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imagedis.setImageBitmap(thumbnail);
                user_imgn = thumbnail;
                Toast.makeText(RegisterActivity.this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
            }

        }
        private void requestPermissiongallary () {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

        }
        private void requestPermissioncamera () {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        }
        private void showPictureDialog () {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("اختر الصورة من");
            String[] pictureDialogItems = {
                    "معرض الصور",
                    "الكاميرا"};
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    if (checkPermissiongallary()) {
                                        choosePhotoFromGallary();
                                    } else {
                                        requestPermissiongallary();
                                    }

                                    break;
                                case 1:
                                    if (checkPermissioncamera()) {
                                        takePhotoFromCamera();
                                    } else {
                                        requestPermissioncamera();
                                    }

                                    break;
                            }
                        }
                    });
            pictureDialog.show();
        }
        private boolean checkPermissiongallary () {
            int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return result2 == PackageManager.PERMISSION_GRANTED;
        }
        private boolean checkPermissioncamera () {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        public void choosePhotoFromGallary () {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY);
        }
        private void takePhotoFromCamera () {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
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
    protected void onStop() {
        super.onStop();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }
    }

