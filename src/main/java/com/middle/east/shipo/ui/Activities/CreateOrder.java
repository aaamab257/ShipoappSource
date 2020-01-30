package com.middle.east.shipo.ui.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CreateOrder extends AppCompatActivity {
    ImageView back;
    EditText oname, otime, ohistory, oprice, ofee, odetails, octiy, olocafrom, olocato , address2 , address;
    CardView add;
    Double flatt, flngt, tlat, tlng;
    RequestQueue requestQueue ,requestQueuef ;
    ConnectionHelper helper;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    private int mYear, mMonth, mDay;
    ArrayList<String> CountryName , CountryId , CityName , CityId;
    ArrayList<String> CountryName2 , CountryId2 , CityName2 , CityId2;
    String cityid , areaid ;
    String cityid2 , areaid2 ;
    Spinner CitySp , AreaSp ;
    Spinner CitySp2 , AreaSp2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        /**/
        IntView();
        loadSpinnerData();
        loadSpinnerData2();
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
        CitySp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityid2 = CountryId2.get(i);
                LoadAreaSpinner2(cityid2);
                CityName2.clear();
                CityId2.clear();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        AreaSp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                areaid2 = CityId2.get(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        otime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateOrder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        otime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        ohistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateOrder.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                ohistory.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadOrder();
            }
        });
    }



    private void loadSpinnerData2() {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLHelper.PROVINCES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("id123",response);
                try{
                    JSONObject jsonObject2=new JSONObject(response);
                    JSONArray jsonArray=jsonObject2.getJSONArray("provinces");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String city =jsonObject1.getString("governorate_name");
                        Log.e("Cite",city);
                        String id= jsonObject1.getString("id");
                        Log.e("id123",id);
                        CountryName2.add(city);
                        CountryId2.add(id);
                    }
                    CitySp2.setAdapter(new ArrayAdapter<>(CreateOrder.this, R.layout.spinner_item, CountryName2));

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

    private void UploadOrder() {
        final String sname = oname.getText().toString();
        final String sprice = oprice.getText().toString();
        final String sfee = ofee.getText().toString();
        final String sdetails = odetails.getText().toString();
        final String history = ohistory.getText().toString();
        final String time = otime.getText().toString();
        final String saddress = address.getText().toString();
        final String saddress2 = address2.getText().toString();
        if (history.equals("")) {
            ohistory.setError("ادخل التاريخ");
        } else if (time.equals("")) {
            otime.setError("ادخل الوقت");
        } else if (saddress.isEmpty()) {
            address.setError("ادخل العنوان");
        } else if (saddress2.isEmpty()) {
            address2.setError("ادخل العنوان");
        }else {
            JSONObject oobje = new JSONObject();
            try {
                oobje.put("token", SharedHelper.getKey(CreateOrder.this, "token"));
                oobje.put("user", SharedHelper.getKey(CreateOrder.this, "user_id"));
                oobje.put("from_province_id", cityid);
                oobje.put("from_city_id", areaid);
                oobje.put("from_address", saddress);
                oobje.put("to_province_id", cityid2);
                oobje.put("to_city_id", areaid2);
                oobje.put("to_address", saddress2);
                oobje.put("history", history);
                oobje.put("price", sprice);
                oobje.put("fee", sfee);
                oobje.put("details", sdetails);
                oobje.put("status", "0");
                oobje.put("name", sname);
                oobje.put("phone", SharedHelper.getKey(CreateOrder.this, "phone"));
                oobje.put("date", history);
                oobje.put("time", time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isInternet) {
                customDialog.setCancelable(false);
                customDialog.show();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLHelper.CREATEORDER, oobje, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (customDialog.isShowing()) {
                            customDialog.dismiss();
                        }
                        Toast.makeText(CreateOrder.this, "تم تسجيل الشحنة بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateOrder.this , HomeActivity.class));
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (customDialog.isShowing()) customDialog.dismiss();
                    }
                });
                requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
            } else {
                Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        helper = new ConnectionHelper(CreateOrder.this);
                        isInternet = helper.isConnectingToInternet();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
            }

        }



    private void IntView() {
        address2 = findViewById(R.id.addressto);
        address = findViewById(R.id.addressfrom);
        CitySp = findViewById(R.id.sp_mohafza);
        AreaSp = findViewById(R.id.sp_area);
        CitySp2 = findViewById(R.id.sp_mohafza2);
        AreaSp2 = findViewById(R.id.sp_area2);
        CountryName=new ArrayList<>();
        CountryId = new ArrayList<>();
        CityName = new ArrayList<>();
        CityId = new ArrayList<>();
        CountryName2=new ArrayList<>();
        CountryId2 = new ArrayList<>();
        CityName2 = new ArrayList<>();
        CityId2 = new ArrayList<>();
        helper = new ConnectionHelper(CreateOrder.this);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(CreateOrder.this);
        if (!isInternet) {
            Toast.makeText(CreateOrder.this, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        add = findViewById(R.id.btn_addads);
        back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(CreateOrder.this , HomeActivity.class));
            }
        });
        oname = findViewById(R.id.order_name);
        otime = findViewById(R.id.order_time);
        ohistory = findViewById(R.id.order_history);
        oprice = findViewById(R.id.order_price);
        ofee = findViewById(R.id.order_fee);
        odetails = findViewById(R.id.order_details);
        octiy = findViewById(R.id.order_city);
        olocafrom = findViewById(R.id.order_locafrom);
        olocato = findViewById(R.id.order_locato);
    }
    private void LoadAreaSpinner2(String cityid2) {
        if (isInternet) {

            JSONObject object = new JSONObject();
            try {
                object.put("province_id", cityid2);

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
                            CityName2.add(city);
                            CityId2.add(id);
                        }
                        AreaSp2.setAdapter(new ArrayAdapter<>(CreateOrder.this, R.layout.spinner_item, CityName2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(CreateOrder.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(CreateOrder.this);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
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
                        AreaSp.setAdapter(new ArrayAdapter<>(CreateOrder.this, R.layout.spinner_item, CityName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(CreateOrder.this, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(CreateOrder.this);
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
                    CitySp.setAdapter(new ArrayAdapter<>(CreateOrder.this, R.layout.spinner_item, CountryName));

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CreateOrder.this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                flatt = data.getDoubleExtra("lat", 0);
                flngt = data.getDoubleExtra("lng", 0);
                String address = "";
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(CreateOrder.this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(flatt, flngt, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    // Log.d("safrsaf", "nvklv" + addresses.get(0).getCountryCode());
                    String des = addresses.get(0).getLocality();
                    Log.e("dister", des);
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    Log.d("safrsaf", "nvklv" + address);
                    olocafrom.setText("" + address);
                    octiy.setText(des);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("safrsaf", "nvklv" + e.getMessage());
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                tlat = data.getDoubleExtra("lat", 0);
                tlng = data.getDoubleExtra("lng", 0);
                String address = "";
                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(CreateOrder.this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(tlat, tlng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    // Log.d("safrsaf", "nvklv" + addresses.get(0).getCountryCode());
                    String des = addresses.get(0).getLocality();
                    Log.e("dister", des);
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    Log.d("safrsaf", "nvklv" + address);
                    olocato.setText("" + address);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("safrsaf", "nvklv" + e.getMessage());
                }
            }
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
}
