package com.middle.east.shipo.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.middle.east.shipo.R;
import com.middle.east.shipo.data.PeopleData;
import com.middle.east.shipo.ui.Activities.DelegateINFO;
import com.middle.east.shipo.ui.Activities.RatingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> {
    ArrayList<PeopleData> mData ;
    private Context mcon;

    public PeopleAdapter(ArrayList<PeopleData> mData, Context mcon) {
        this.mData = mData;
        this.mcon = mcon;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getPeoplename());
        if (mData.get(position).getPeopleImg() == null){
            Picasso.with(mcon).load(R.drawable.newlogo).into(holder.pimg);
        }else {
            Picasso.with(mcon).load("http://shipo.3codeit.com/"+mData.get(position).getPeopleImg()).into(holder.pimg);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(mcon , DelegateINFO.class);
                profile.putExtra("id",mData.get(position).getPeopleid());
                profile.putExtra("image","http://shipo.3codeit.com/"+mData.get(position).getPeopleImg());
                mcon.startActivity(profile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name ;
        ImageView pimg ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.people_name);
            pimg = itemView.findViewById(R.id.people_img);
        }
    }
}
