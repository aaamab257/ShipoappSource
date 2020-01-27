package com.middle.east.shipo.ui.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.Helper.URLHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.Services.MyOrderNotifications;
import com.middle.east.shipo.Services.NewOrderAdded;
import com.middle.east.shipo.data.MyOrdersData;
import com.middle.east.shipo.data.NewOrderData;
import com.middle.east.shipo.ui.Activities.CreateOrder;
import com.middle.east.shipo.ui.Adapters.MyOrdersAdapter;
import com.middle.east.shipo.ui.Adapters.NewOrdersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrder extends Fragment {

    RecyclerView recnew ;
    View v ;
    NewOrdersAdapter adapter ;
    ArrayList<NewOrderData> mList ;
    Spinner fromProv , fromCity , toProv , toCity , RateSpi ;
    CardView search ;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    TextView noOrder ;
    ArrayList<String> CountryName , CountryId , CityName , CityId;
    ArrayList<String> CountryName2 , CountryId2 , CityName2 , CityId2;
    String cityid = "" , areaid = "";
    String cityid2 = "" , areaid2="" ;
    String [] Rating = {"0 نجمة","1 نجمة","2 نجمتين","3 نجمات","4 نجمات","5 نجمات"};
    int [] rate = {0 , 1 , 2 , 3 , 4 , 5};
    int ra ;
    RecyclerView recSearch ;
    public NewOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_order, container, false);
        Intviews();
        loadSpinnerData();
        loadSpinnerData2();
        RateSpi.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Rating));
        fromProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        fromCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                areaid = CityId.get(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        toProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        toCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                areaid2 = CityId2.get(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        RateSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 ra = rate[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recnew.setVisibility(View.GONE);
                recSearch.setVisibility(View.VISIBLE);
                if (isInternet) {
                    customDialog.setCancelable(false);
                    customDialog.show();
                    JSONObject object2 = new JSONObject();
                    try {
                        object2.put("from_province_id", cityid);
                        object2.put("from_city_id", areaid);
                        object2.put("to_province_id", cityid2);
                        object2.put("to_city_id", areaid2);
                        object2.put("rate", ""+ra);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.FILTER, object2, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int size = mList.size();
                            mList.clear();
                            adapter.notifyItemRangeRemoved(0, size);
                            recnew.clearOnChildAttachStateChangeListeners();
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            try {
                                JSONArray array = response.getJSONArray("offers");
                                for (int i = 0 ; i < array.length() ; i ++ ) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    Log.e("AREA", response+"");
                                    Log.e("AREA2", SharedHelper.getKey(getContext(), "area"));
                                    String order_id = object1.optString("id");
                                    String order_time = object1.optString("time");
                                    String order_history = object1.optString("date");
                                    String order_price = object1.optString("price");
                                    String order_details = object1.optString("details");
                                    String order_fee = object1.optString("fee");
                                    String order_create = object1.optString("created_at");
                                    String order_status = object1.optString("status");
                                    String order_cityf = object1.optString("fromprovince");
                                    String order_areaf = object1.optString("fromcity");
                                    String order_addressf = object1.optString("from_address");
                                    /*JSONObject obadd2 = object1.getJSONObject("to_province");
                                    String order_city2 = obadd2.optString("governorate_name");*/
                                    String order_area2 = object1.optString("tocity");
                                    String order_address2 = object1.optString("to_address");//city
                                    String order_img = object1.optString("imageUrl");
                                    String order_name = object1.optString("name");//name
                                    String order_phone = object1.optString("phone");
                                    mList.add(new NewOrderData(order_id, order_name, order_details, order_img, order_price, order_fee, order_phone, order_cityf, order_areaf, order_addressf, order_address2, order_area2, order_address2, order_history, order_time, order_create, order_status));
                                    Log.e("SIZEMLIST", mList.size()+"");
                                    adapter = new NewOrdersAdapter(getContext(), mList);
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    mLayoutManager.setReverseLayout(true);
                                    mLayoutManager.setStackFromEnd(true);
                                    recSearch.setLayoutManager(mLayoutManager);
                                    recSearch.setAdapter(adapter);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (customDialog.isShowing()) customDialog.dismiss();

                            Toast.makeText(getContext(), "لا يوجد طلبات", Toast.LENGTH_SHORT).show();


                        }
                    });
                    requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(jsonObjectRequest);
                } else {
                    Toast.makeText(getContext(), "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            helper = new ConnectionHelper(getContext());
                            isInternet = helper.isConnectingToInternet();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
                Log.e("IDSSS",cityid+areaid+cityid2+areaid2+ra);
            }


        });
        /*Intent startIntent = new Intent(getContext(), NewOrderAdded.class);
        getActivity().startService(startIntent);*/
        getNewOrder();
        return v;
    }
    private void Searchfun(String cityid, String areaid, String cityid2, String areaid2, float ra) {

    }
    private void getNewOrder() {
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            final JSONObject object = new JSONObject();
            try {
                object.put("token", SharedHelper.getKey(getContext(),"token"));
                object.put("user", SharedHelper.getKey(getContext(),"user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.GETNEWORDERS,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    try {

                        //SharedHelper.putKey(getContext(),"neworderlength",""+array.length());
                        /*Intent intent = new Intent(getContext(), NewOrderAdded.class);
                        getActivity().startService(intent);*/
                        JSONArray array = response.getJSONArray("offers");
                        for (int i = 0 ; i < array.length() ; i ++ ){
                            JSONObject object1 = array.getJSONObject(i);
                            if(object1.optString("fromcity").equals(SharedHelper.getKey(getContext(),"area"))){
                                Log.e("AREA",object1.optString("fromcity"));
                                Log.e("AREA2",SharedHelper.getKey(getContext(),"area"));
                                String order_id = object1.optString("id");
                                String order_time = object1.optString("time");
                                String order_history = object1.optString("date");
                                String order_price = object1.optString("price");
                                String order_details = object1.optString("details");
                                String order_fee = object1.optString("fee");
                                String order_create = object1.optString("created_at");
                                String order_status = object1.optString("status");
                                String order_cityf = object1.optString("fromprovince");
                                String order_areaf = object1.optString("fromcity");
                                String order_addressf = object1.optString("from_address");
                                JSONObject obadd2 = object1.getJSONObject("to_province");
                                String order_city2 = obadd2.optString("governorate_name");
                                String order_area2 = object1.optString("tocity");
                                String order_address2 = object1.optString("to_address");//city
                                String order_img = object1.optString("imageUrl");
                                String order_name = object1.optString("name");//name
                                String order_phone = object1.optString("phone");
                                mList.add(new NewOrderData(order_id,order_name,order_details,order_img,order_price,order_fee,order_phone,order_cityf,order_areaf,order_addressf,order_city2,order_area2,order_address2,order_history,order_time,order_create,order_status));
                                adapter = new NewOrdersAdapter(getContext(),mList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recnew.setLayoutManager(mLayoutManager);
                                recnew.setAdapter(adapter);


                            }else if(!object1.optString("fromcity").equals(SharedHelper.getKey(getContext(),"area"))){
                                String order_id = object1.optString("id");
                                String order_time = object1.optString("time");
                                String order_history = object1.optString("date");
                                String order_price = object1.optString("price");
                                String order_details = object1.optString("details");
                                String order_fee = object1.optString("fee");
                                String order_create = object1.optString("created_at");
                                String order_status = object1.optString("status");
                                String order_cityf = object1.optString("fromprovince");
                                String order_areaf = object1.optString("fromcity");
                                String order_addressf = object1.optString("from_address");
                                JSONObject obadd2 = object1.getJSONObject("to_province");
                                String order_city2 = obadd2.optString("governorate_name");
                                String order_area2 = object1.optString("tocity");
                                String order_address2 = object1.optString("to_address");//city
                                String order_img = object1.optString("imageUrl");
                                String order_name = object1.optString("name");//name
                                String order_phone = object1.optString("phone");
                                mList.add(new NewOrderData(order_id,order_name,order_details,order_img,order_price,order_fee,order_phone,order_cityf,order_areaf,order_addressf,order_city2,order_area2,order_address2,order_history,order_time,order_create,order_status));
                                adapter = new NewOrdersAdapter(getContext(),mList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recnew.setLayoutManager(mLayoutManager);
                                recnew.setAdapter(adapter);
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
                    if (noOrder.getVisibility() == View.VISIBLE){
                        noOrder.setVisibility(View.GONE);
                    }else {
                        noOrder.setVisibility(View.VISIBLE);
                    }
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
    public void Intviews() {
        RateSpi = v.findViewById(R.id.sp_rate);
        CountryName=new ArrayList<>();
        CountryId = new ArrayList<>();
        CityName = new ArrayList<>();
        CityId = new ArrayList<>();
        CountryName2=new ArrayList<>();
        CountryId2 = new ArrayList<>();
        CityName2 = new ArrayList<>();
        CityId2 = new ArrayList<>();
        fromCity = v.findViewById(R.id.sp_fromcity);
        fromProv = v.findViewById(R.id.sp_fromprov);
        toCity = v.findViewById(R.id.sp_tocity);
        toProv = v.findViewById(R.id.sp_toprov);
        RateSpi = v.findViewById(R.id.sp_rate);
        search = v.findViewById(R.id.btn_search);
        noOrder = v.findViewById(R.id.text_noOrder);
        mList = new ArrayList<>();
        helper = new ConnectionHelper(getContext());
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(getContext());
        if(!isInternet){
            Toast.makeText(getContext(), "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        /*adapter = new NewOrdersAdapter();*/
        recnew = v.findViewById(R.id.rec_newOrders);
        recSearch = v.findViewById(R.id.rec_search);
    }
    public void loadSpinnerData() {
        if (isInternet) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.PROVINCES, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.e("id123",response);
                    try{
                        //JSONObject jsonObject= new JSONObject( response);
                        JSONArray jsonArray=response.getJSONArray("provinces");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String city =jsonObject1.getString("governorate_name");
                            Log.e("Cite",city);
                            String id= jsonObject1.getString("id");
                            Log.e("id123",id);
                            CountryName.add(city);
                            CountryId.add(id);
                        }
                        fromProv.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryName));

                    }catch (JSONException e){e.printStackTrace();}


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(getContext(), "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(getContext(), "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(getContext());
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
    public void loadSpinnerData2() {
        if (isInternet) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.PROVINCES, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.e("id123",response);
                    try{
                        //JSONObject jsonObject2=new JSONObject(response);
                        JSONArray jsonArray=response.getJSONArray("provinces");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String city =jsonObject1.getString("governorate_name");
                            Log.e("Cite",city);
                            String id= jsonObject1.getString("id");
                            Log.e("id123",id);
                            CountryName2.add(city);
                            CountryId2.add(id);
                        }
                        toProv.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryName2));

                    }catch (JSONException e){e.printStackTrace();}


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(getContext(), "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(getContext(), "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(getContext());
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
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
                        toCity.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, CityName2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(getContext(), "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(getContext(), "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(getContext());
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
                        fromCity.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, CityName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();

                    Toast.makeText(getContext(), "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();


                }
            });
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(getContext(), "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(getContext());
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}
