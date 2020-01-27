package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.middle.east.shipo.R;
import com.middle.east.shipo.ui.Adapters.MyOrdersAdapter;

public class MyOrders extends AppCompatActivity {
    RecyclerView rechis ;
    MyOrdersAdapter adapter ;
    ProgressBar probar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        IntViews();
    }

    private void IntViews() {
        probar = findViewById(R.id.progBar);
        rechis = findViewById(R.id.recHistory);
        /*adapter = new MyOrdersAdapter() ;
        rechis.setLayoutManager(new LinearLayoutManager(this));
        rechis.setAdapter(adapter);
        probar.setVisibility(View.GONE);*/
    }
}
