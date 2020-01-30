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
import com.middle.east.shipo.ui.Activities.MandobOrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DelegateAdapters extends RecyclerView.Adapter<DelegateAdapters.MyViewHolder> {
    Context context;
    ArrayList<DelegatesInfo> mList;
    Dialog dialog;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;
    Handler handler , handler2;
    String iddelegate, offerIID;
    int delay = 10000;
    TextView nOrders;
    CardView on;
    CardView deleca;
    CustomDialog customDialog;
    CardView rateing ;
    RatingBar mBar ;
    CardView rr ;
    String status ;
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
        iddelegate = mList.get(position).getId();
        offerIID = mList.get(position).getOfferId();
        status = SharedHelper.getKey(context , "status");
        Log.e("IDS",iddelegate+offerIID);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.order_onway);
                TextView text_done = dialog.findViewById(R.id.txt_orderdone);
                 rr = dialog.findViewById(R.id.btn_rr);
                deleca = dialog.findViewById(R.id.btn_canceldele);
                on = dialog.findViewById(R.id.btn_mandobDone);
                Log.e("status",status);
                if(status.equals("3")){
                    on.setVisibility(View.GONE);
                    text_done.setVisibility(View.VISIBLE);
                    deleca.setVisibility(View.GONE);
                    handler2 = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            checkRating(offerIID,iddelegate);
                            handler2.postDelayed(this, 1000);

                        }
                    };
// start it with:
                    handler2.post(runnable);
                }else {
                    handler = new Handler();
                    Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {
                            // do your stuff - don't create a new runnable here!
                            checkIf(offerIID);
                            //checkRating(offerIID,iddelegate);
                            handler.postDelayed(this, 5000);

                        }
                    };
// start it with:
                    handler.post(runnable1);
                }




                 rateing = dialog.findViewById(R.id.rate_btn);
                 mBar = dialog.findViewById(R.id.rate_mandob);




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
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
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
                /*on.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        on.setVisibility(View.GONE);
                        deleca.setVisibility(View.VISIBLE);
                        String id = mList.get(position).getOfferId();
                        String delegateid = mList.get(position).getId();
                        //SharedHelper.putKey(context, "deleID", delegateid);
                        CallApiChooseDelegate(id, delegateid);
                    }
                });*/
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }


        });
    }
    private void checkRating(final String id, final String user_id) {
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if (!isInternet) {
            Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if(isInternet) {
            /*customDialog.setCancelable(false);
            customDialog.show();*/
            JSONObject object = new JSONObject();
            try {
                object.put("user", user_id);
                object.put("offer_id",id);
                Log.e("IDS",user_id + id );
                //object.put("token", SharedHelper.getKey(this , "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHECKRATE, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    /*if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }*/
                    String mes = response.optString("message");
                    if(mes.equals("0")){
                        rr.setVisibility(View.GONE);
                        mBar.setVisibility(View.VISIBLE);
                        rateing.setVisibility(View.VISIBLE);
                        rateing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //int noofstars = mBar.getNumStars();
                                float getrating = mBar.getRating();
                                RateMandob(getrating);
                            }
                        });

                    }else if(mes.equals("1")){
                        //rr.setVisibility(View.GONE);
                        //rr.setVisibility(View.GONE);
                        mBar.setVisibility(View.GONE);
                        rateing.setVisibility(View.GONE);
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
    private void checkIf(final String offerIID) {
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if (!isInternet) {
            Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if(isInternet) {
            /*customDialog.setCancelable(false);
            customDialog.show();*/
            JSONObject object = new JSONObject();
            try {
                object.put("delegate", iddelegate);
                object.put("offer",offerIID);
                //object.put("token", SharedHelper.getKey(this , "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHECKTRADERS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    /*if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }*/
                    String mes = response.optString("message");
                    if(mes.equals("0")){
                        on.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                on.setVisibility(View.GONE);
                                deleca.setVisibility(View.VISIBLE);
                                CallApiChooseDelegate(offerIID, iddelegate);
                            }
                        });
                    }else if(mes.equals("1")){
                        deleca.setVisibility(View.VISIBLE);
                        on.setVisibility(View.GONE);
                        deleca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CancelApi(offerIID, SharedHelper.getKey(context,"user_id"));
                            }
                        });
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

    public void RateMandob(float getrating) {
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if (!isInternet) {
            Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if (isInternet) {

            JSONObject object = new JSONObject();
            try {
                object.put("rate", "" + getrating);
                object.put("user", iddelegate);
                object.put("from_id", SharedHelper.getKey(context,"user_id"));
                object.put("offer_id", offerIID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RATINGS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(context, "تم تقيم المندوب بنجاح", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 402) {
                        Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(context, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
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
        TextView name;
        RatingBar rate;

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
        if (isInternet) {
            /*customDialog.setCancelable(false);
            customDialog.show();*/
            JSONObject object = new JSONObject();
            try {
                object.put("delegate", delegate_id);
                object.put("offer", offer_id);
                object.put("token", SharedHelper.getKey(context, "token"));
                object.put("user", SharedHelper.getKey(context, "user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHOOSEDELEGATE, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    /*if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }*/
                    Toast.makeText(context, "لقد قمت بأختيار المندوب", Toast.LENGTH_SHORT).show();
                    deleca.setVisibility(View.VISIBLE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    /*if (customDialog.isShowing()) customDialog.dismiss();*/
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 402) {
                        Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(context, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(context);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }

    }

    private void CancelApi(String offerIID, String user_id) {
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        if (!isInternet) {
            Toast.makeText(context, "لا يوجد اتصال بالإنترنت", Toast.LENGTH_SHORT).show();
        }
        if (isInternet) {
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("offer", offerIID);
                object.put("token", SharedHelper.getKey(context, "token"));
                object.put("user", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(context, "تم إلغاء المندوب", Toast.LENGTH_SHORT).show();
                    SharedHelper.putKey(context , "deleID", null);

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
        } else {
            Toast.makeText(context, "الرجاء التأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helper = new ConnectionHelper(context);
                    isInternet = helper.isConnectingToInternet();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}
