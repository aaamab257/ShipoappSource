package com.middle.east.shipo.ui.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.middle.east.shipo.ui.Activities.DelegateAcceptedOrders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DelegateAdapters extends RecyclerView.Adapter<DelegateAdapters.MyViewHolder> {
    Context context ;
    ArrayList<DelegatesInfo> mList ;
    Dialog dialog ;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;
    int delay = 10000;
    TextView nOrders ;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    public DelegateAdapters(Context context, ArrayList<DelegatesInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delegate_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(mList.get(position).getName());
        holder.rate.setRating(Float.parseFloat(mList.get(position).getRate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.order_onway);
                final CardView rr = dialog.findViewById(R.id.btn_rr);

                CardView on = dialog.findViewById(R.id.btn_mandobDone);
                final CardView rateing = dialog.findViewById(R.id.rate_btn);
                final RatingBar mBar = dialog.findViewById(R.id.rate_mandob);
                rr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rr.setVisibility(View.GONE);
                        mBar.setVisibility(View.VISIBLE);
                        rateing.setVisibility(View.VISIBLE);

                    }
                });
                rateing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //int noofstars = mBar.getNumStars();
                        float getrating = mBar.getRating();
                        String id = mList.get(position).getId();
                        RateMandob(id , getrating);
                    }
                });

                ImageView close = dialog.findViewById(R.id.close_dialog);
                TextView mname = dialog.findViewById(R.id.txt_mandobName);
                /*TextView oprice = dialog.findViewById(R.id.txt_oprice);
                TextView odetails = dialog.findViewById(R.id.txt_odetails);*/
                RatingBar rate = dialog.findViewById(R.id.txt_mandob_Rate);
                CardView callMandob = dialog.findViewById(R.id.mandobPhone);
                mname.setText(mList.get(position).getName());
                callMandob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int REQUEST_PHONE_CALL = 1;
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + " +2" + mList.get(position).getPhone()));
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        }
                        else
                        {
                            context.startActivity(intent);
                        }
                    }
                });
                float drate = Float.parseFloat(mList.get(position).getRate());
                rate.setRating(drate);
                /*mname.setText(mList.get(position).getMandobName());
                mphone.setText(mList.get(position).getMandobphone());
                oprice.setText(mList.get(position).getOrderPrice());
                odetails.setText(mList.get(position).getOrderDetails());*/
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = mList.get(position).getOfferId();
                        String delegateid = mList.get(position).getId();
                        CallApiChooseDelegate(id,delegateid);
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }


        });
    }

    public void RateMandob(String id, float getrating) {
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if (!isInternet) {
            Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("rate", ""+getrating);
                object.put("user", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RATINGS,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(context, "تم تقيم المندوب بنجاح", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 402) {
                        Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(context, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(context);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name ;
        RatingBar rate ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_delegaename);
            rate = itemView.findViewById(R.id.txt_mandob_Rate);
        }
    }
    private void CallApiChooseDelegate(String offer_id, String delegate_id) {
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if (!isInternet) {
            Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if(isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("delegate", delegate_id);
                object.put("offer", offer_id);
                object.put("token", SharedHelper.getKey(context,"token"));
                object.put("user", SharedHelper.getKey(context,"user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHOOSEDELEGATE,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(context, "لقد قمت بأختيار المندوب", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 402) {
                        Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(context, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(context);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }

    }
}
