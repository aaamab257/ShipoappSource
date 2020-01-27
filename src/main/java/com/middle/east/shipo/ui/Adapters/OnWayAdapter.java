package com.middle.east.shipo.ui.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.middle.east.shipo.data.OnWayTraderOrdersData;
import com.middle.east.shipo.ui.Activities.DelegateAcceptedOrders;
import com.middle.east.shipo.ui.Activities.HomeActivity;
import com.middle.east.shipo.ui.Activities.MandobOrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OnWayAdapter extends RecyclerView.Adapter<OnWayAdapter.MyViewHolder> {
    Context mCon ;
    ArrayList<OnWayTraderOrdersData> mData ;
    RequestQueue requestQueue ;
    ConnectionHelper helper ;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    Dialog dialog ;

    public OnWayAdapter(Context mCon, ArrayList<OnWayTraderOrdersData> mData) {
        this.mCon = mCon;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.onway_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.ordername.setText(mData.get(position).getMandobName());
        holder.mandobName.setText(mData.get(position).getOrderDetails());
        if (mData.size() != 0 ){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**/
                    Intent orders = new Intent(mCon , DelegateAcceptedOrders.class);
                    orders.putExtra("id",mData.get(position).getOrderID());
                    mCon.startActivity(orders);
                }
            });
        }

    }

    private void CallApi(String id) {
        helper = new ConnectionHelper(mCon);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(mCon);
        if(!isInternet){
            Toast.makeText(mCon, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if(isInternet) {

            JSONObject object = new JSONObject();
            try {
                object.put("token", SharedHelper.getKey(mCon,"token"));
                object.put("user", SharedHelper.getKey(mCon,"user_id"));
                object.put("offer", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHANGEORDERSTAT,object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    dialog.dismiss();
                    Toast.makeText(mCon, "تم تأكيد استلام الشحنة", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 404) {
                        Toast.makeText(mCon, "خطأ فى البريد الإلكترونى او كلمة السر", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
                        Toast.makeText(mCon, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(mCon);
            requestQueue.add(jsonObjectRequest);
        }else {
            Toast.makeText(mCon, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    helper = new ConnectionHelper(mCon);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ordername  , mandobName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ordername = itemView.findViewById(R.id.txt_ordername);
            mandobName = itemView.findViewById(R.id.txt_orderplace);
        }
    }
}
