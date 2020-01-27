package com.middle.east.shipo.ui.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.middle.east.shipo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {
    String [] names = {"أحمد" , "مصعب" , "يوسف","ابراهيم","خالد","محمد","سيد","حسين","سعيد"};
    String [] Country = {"القاهرة" , "المنصورة" , "القاهرة","السويس","بورسعيد","مصر الجديدة","مدينة نصر","المقطم","الجيزة"};
    @NonNull
    @Override
    public AdsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsAdapter.MyViewHolder holder, int position) {
        holder.user_name.setText(names[position]);
        holder.ads_country.setText(Country[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage ;
        TextView user_name , ads_details , ads_country;
        CardView details;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.img_user);
            user_name = itemView.findViewById(R.id.text_user_name);
            ads_details = itemView.findViewById(R.id.text_ads_details);
            ads_country = itemView.findViewById(R.id.text_country_name);
            details = itemView.findViewById(R.id.card_btn_details);

        }
    }
}
