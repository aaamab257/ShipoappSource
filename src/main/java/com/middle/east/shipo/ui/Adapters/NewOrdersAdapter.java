package com.middle.east.shipo.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.data.NewOrderData;
import com.middle.east.shipo.ui.Activities.MandobOrderDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.MyViewHolder>{
    Context context ;
    ArrayList<NewOrderData> mData ;

    public NewOrdersAdapter(Context context, ArrayList<NewOrderData> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_order_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText("اسم الشحنة: " + mData.get(position).getOrderName() );
        holder.price.setText("سعر الشحنة: " + mData.get(position).getPrice()  );
        holder.fee.setText("رسوم الشحن: " + mData.get(position).getFee()  );
        holder.place.setText("مكان التوصيل: " + mData.get(position).getOrdercity2() );
        holder.placeto.setText("مكان الاستلام: " + mData.get(position).getOrdercityf() );
        /*if(mData.get(position).getOrder_image().equals("Image")){
            Picasso.with(context).load(R.drawable.newlogo).into(holder.oImage);
        }else {
            Picasso.with(context).load(mData.get(position).getOrder_image()).into(holder.oImage);
        }*/
        holder.oAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to = new Intent(context , MandobOrderDetails.class);
                to.putExtra("name",mData.get(position).getOrderName());
                to.putExtra("price",mData.get(position).getPrice());
                to.putExtra("fee",mData.get(position).getFee());
                to.putExtra("phone",mData.get(position).getOrderPhone());
                to.putExtra("details",mData.get(position).getOrderDetails());
                to.putExtra("oid",mData.get(position).getId());
                to.putExtra("time",mData.get(position).getHistory()+" / "+mData.get(position).getTime());
                to.putExtra("cityf",mData.get(position).getOrdercityf());
                to.putExtra("areaf",mData.get(position).getOrderareaf());
                to.putExtra("addressf",mData.get(position).getOrderaddressf());
                to.putExtra("city2",mData.get(position).getOrdercity2());
                to.putExtra("area2",mData.get(position).getOrderarea2());
                to.putExtra("address2",mData.get(position).getOrderaddress2());
                /*SharedHelper.putKey(context,"Done","");*/
                context.startActivity(to);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name , price , fee , place , placeto  ;
        ImageView oImage ;
        CardView oAccept ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            placeto = itemView.findViewById(R.id.order_placeto);
            name = itemView.findViewById(R.id.order_name);
            fee = itemView.findViewById(R.id.order_fee);
            place = itemView.findViewById(R.id.order_place);
            price = itemView.findViewById(R.id.order_price);
            oImage = itemView.findViewById(R.id.order_image);
            oAccept = itemView.findViewById(R.id.order_accept);
        }
    }
}
