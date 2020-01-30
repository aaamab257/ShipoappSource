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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.middle.east.shipo.data.MyOrdersData;
import com.middle.east.shipo.ui.Activities.ContactUs;
import com.middle.east.shipo.ui.Activities.MandobActivity;
import com.middle.east.shipo.ui.Activities.MandobOrderDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyOrdersData> mData;
    RequestQueue requestQueue;
    ConnectionHelper helper;
    Boolean isInternet;
    CustomDialog customDialog;
    final int SPLASH_DISPLAY_LENGTH = 1000;
    Handler handler;
    String id;
    String user_id;
    CardView rateing;
    RatingBar mBar;
    CardView rr;
    float getrating;
    TextView text;
    CardView cancle;
    CardView done;
    CardView ssearch;
    TextView canceling , donning;

    public MyOrdersAdapter(Context context, ArrayList<MyOrdersData> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText("اسم الشحنة: " + mData.get(position).getName());
        holder.price.setText("سعر الشحنة: " + mData.get(position).getPrice());
        holder.fee.setText("رسوم الشحن: " + mData.get(position).getFee());
        holder.place.setText("مكان التوصيل: " + mData.get(position).gettCity());
        holder.status.setText(mData.get(position).getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.order_details_view);
                rr = dialog.findViewById(R.id.btn_rr);
                ssearch = dialog.findViewById(R.id.btn_seachfroNew);
                canceling = dialog.findViewById(R.id.canceling);
                rateing = dialog.findViewById(R.id.rate_btn);
                mBar = dialog.findViewById(R.id.rate_mandob);
                donning = dialog.findViewById(R.id.donning);
                if (mData.get(position).getId() != null){
                    id = mData.get(position).getId();

                }
                if (mData.get(position).getUser_id() != null){
                    user_id = mData.get(position).getUser_id();
                }



                ImageView close = dialog.findViewById(R.id.close_dialog);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                text = dialog.findViewById(R.id.txt_status);
                TextView name = dialog.findViewById(R.id.txt_oname);
                TextView price = dialog.findViewById(R.id.txt_oprice);
                TextView fee = dialog.findViewById(R.id.txt_ofee);
                TextView details = dialog.findViewById(R.id.txt_odetails);
                TextView from = dialog.findViewById(R.id.addressf);
                TextView to = dialog.findViewById(R.id.address2);
                TextView datetime = dialog.findViewById(R.id.txt_otime);
                name.setText(mData.get(position).getName());
                details.setText(mData.get(position).getName());
                price.setText(mData.get(position).getPrice());
                fee.setText(mData.get(position).getFee());
                from.setText(mData.get(position).getFromCity() + "," + mData.get(position).getFromArea() + "," + mData.get(position).getFromAddress());
                to.setText(mData.get(position).gettCity() + "," + mData.get(position).gettArea() + "," + mData.get(position).gettAddress());
                datetime.setText(mData.get(position).getDate() + "," + mData.get(position).getTime());
                done = dialog.findViewById(R.id.btn_confirm);
                cancle = dialog.findViewById(R.id.mandobcancle);
                CardView call = dialog.findViewById(R.id.traderCall);
                //CardView send  = dialog.findViewById(R.id.btn_confirmsend);
                Log.e("STATUS", mData.get(position).getStatus());
                //text.setText("استلام");
                if (id != null){
                    handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            // do your stuff - don't create a new runnable here!
                            CheckOrderStatus(id);
                            handler.postDelayed(this, 5000);

                        }
                    };
                    handler.post(runnable);
                }






                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int REQUEST_PHONE_CALL = 1;
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + " +2" + mData.get(position).getPhone()));
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            }
        });
    }

    private void CheckOrderStatus(final String id) {
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
                object.put("offer", id);
                //object.put("token", SharedHelper.getKey(this , "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CHECKORDERSTATUS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    /*if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }*/
                    String mes = response.optString("status");
                    if (mes.equals("1")) {
                        cancle.setVisibility(View.VISIBLE);
                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallCancledApi(id);
                            }
                        });
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallReceiveApi(id);
                                text.setText(" تم تسليم الشحنة");
                                cancle.setVisibility(View.GONE);
                            }
                        });
                    } else if (mes.equals("2")) {
                        text.setText(" تم تسليم الشحنة");
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CallConfirmApi(id);
                                done.setVisibility(View.GONE);
                            }
                        });
                    } else if (mes.equals("3")) {
                        done.setVisibility(View.GONE);
                        cancle.setVisibility(View.GONE);
                        donning.setVisibility(View.VISIBLE);
                        handler = new Handler();
                        Runnable runnable3 = new Runnable() {
                            @Override
                            public void run() {
                                // do your stuff - don't create a new runnable here!
                                checkRating(id, user_id);
                                handler.postDelayed(this, 1000);

                            }
                        };
                        handler.post(runnable3);
                    } else if (mes.equals("4")) {

                        done.setVisibility(View.GONE);
                        cancle.setVisibility(View.GONE);
                        rateing.setVisibility(View.GONE);
                        mBar.setVisibility(View.GONE);
                        ssearch.setVisibility(View.VISIBLE);
                        canceling.setVisibility(View.VISIBLE);
                        ssearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                context.startActivity(new Intent(context , MandobActivity.class));
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

    private void checkRating(final String id, final String user_id) {
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
                object.put("user", user_id);
                object.put("offer_id", id);
                Log.e("IDS", user_id + id);
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
                    if (mes.equals("0")) {
                        rr.setVisibility(View.GONE);
                        mBar.setVisibility(View.VISIBLE);
                        rateing.setVisibility(View.VISIBLE);
                        rateing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //int noofstars = mBar.getNumStars();
                                getrating = mBar.getRating();
                                RateMandob(id, getrating, user_id);
                                mBar.setVisibility(View.GONE);
                                rateing.setVisibility(View.GONE);
                            }
                        });
                    } else if (mes.equals("1")) {
                        rr.setVisibility(View.GONE);
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

    public void RateMandob(String id, float getrating, String user_id) {
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
                object.put("rate", "" + getrating);
                object.put("user", user_id);
                object.put("from_id", SharedHelper.getKey(context, "user_id"));
                object.put("offer_id", id);
                Log.e("RATEING", "" + getrating + user_id + SharedHelper.getKey(context, "user_id") + id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RATINGS, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(context, "تم تقيم التاجر بنجاح", Toast.LENGTH_SHORT).show();

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

    private void CallReceiveApi(String id) {
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
                object.put("offer", id);
                object.put("user", SharedHelper.getKey(context, "user_id"));
                object.put("token", SharedHelper.getKey(context, "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RECEIVEORDER, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(context, "تم استلام الشحنة", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 404) {
                        Toast.makeText(context, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
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

    private void CallCancledApi(final String id) {
        final Dialog dialog2 = new Dialog(context);
        dialog2.setContentView(R.layout.cancle_offer);
        ImageView close = dialog2.findViewById(R.id.close_dialog);
        CardView cancel = dialog2.findViewById(R.id.btn_cancel);
        final EditText reason = dialog2.findViewById(R.id.ed_reason);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String re = reason.getText().toString();
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
                        object.put("offer", id);
                        object.put("reason", re);
                        object.put("user", SharedHelper.getKey(context, "user_id"));
                        object.put("token", SharedHelper.getKey(context, "token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCELORDER, object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (customDialog.isShowing()) {
                                customDialog.dismiss();
                            }
                            Toast.makeText(context, "تم إلغاء تسليم الشحنة", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (customDialog.isShowing()) customDialog.dismiss();
                            NetworkResponse response = error.networkResponse;
                            if (response.statusCode == 404) {
                                Toast.makeText(context, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                            } else if (response.statusCode == 402) {
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
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
        Window window = dialog2.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void CallConfirmApi(String id) {
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
                object.put("offer", id);
                object.put("user", SharedHelper.getKey(context, "user_id"));
                object.put("token", SharedHelper.getKey(context, "token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CONFIRMORDER, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                    Toast.makeText(context, "تم تسليم الشحنة", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    NetworkResponse response = error.networkResponse;
                    if (response.statusCode == 404) {
                        Toast.makeText(context, "خطأ فى الشبكة", Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 402) {
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
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, fee, place, status;
        ImageView oImage;
        CardView oAccept;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            fee = itemView.findViewById(R.id.order_fee);
            place = itemView.findViewById(R.id.order_place);
            price = itemView.findViewById(R.id.order_price);
            oImage = itemView.findViewById(R.id.order_image);
            oAccept = itemView.findViewById(R.id.order_accept);
            status = itemView.findViewById(R.id.order_status);
        }
    }
}
