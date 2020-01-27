package com.middle.east.shipo.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.middle.east.shipo.R;
import com.middle.east.shipo.data.MyOrdersData;
import com.middle.east.shipo.data.TraderHistory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.middle.east.shipo.R.drawable.newlogo;

public class TraderHistoryAdapter extends RecyclerView.Adapter<TraderHistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<TraderHistory> mData;

    public TraderHistoryAdapter(Context context, ArrayList<TraderHistory> mData) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*holder.name.setText( mData.get(position).getDetails());
        holder.phone.setText(mData.get(position).getHistory() );
        holder.country.setText(mData.get(position).getPrice()+"جنيه");*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView user_img;
        TextView name, phone, country;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /*name = itemView.findViewById(R.id.user_Name);
            phone = itemView.findViewById(R.id.user_phone);
            country = itemView.findViewById(R.id.user_country);
            user_img = itemView.findViewById(R.id.user_image);*/
        }
    }
}
