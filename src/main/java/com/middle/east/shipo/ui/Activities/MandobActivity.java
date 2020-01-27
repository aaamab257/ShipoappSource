package com.middle.east.shipo.ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.R;
import com.middle.east.shipo.Services.MyOrderNotifications;
import com.middle.east.shipo.Services.NewOrderAdded;
import com.middle.east.shipo.app.Config;
import com.middle.east.shipo.ui.Fragments.MyOrders;
import com.middle.east.shipo.ui.Fragments.NewOrder;
import com.middle.east.shipo.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

public class MandobActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager vPager ;
    TabLayout tabLayout ;
    ImageView noti , logOut , refresh  ;
    String  who = "" ;
    ImageView nave ;
    private static final String TAG = MandobActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandob);
        IntiView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_home);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MandobActivity.this , MandobActivity.class));
                finish();
            }
        });
        /*noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MandobActivity.this , Notifications.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MandobActivity.this , NavActivity.class));
                SharedHelper.putKey(MandobActivity.this ,"isLoged","false");
                MandobActivity.this.finish();
            }
        });
        */
    }
    private void displayFirebaseRegId() {

        String regId = SharedHelper.getKey(this , "regId");

        Log.e("TOKENs", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)){

        }// txtRegId.setText("Firebase Reg Id: " + regId);
        else{

        }
            //txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LanguageHelper.ChangeLang(getResources(),"ar");
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void IntiView() {
        //nave = findViewById(R.id.img_nav);
        refresh = findViewById(R.id.refresh);
        logOut = findViewById(R.id.log_out);
        noti = findViewById(R.id.Notifications);
        vPager = findViewById(R.id.viewPager);
        setupViewPager(vPager);
        tabLayout  = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vPager);
        setupNavView();
    }

    private void setupNavView() {

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyOrders(), "طلباتى");
        adapter.addFragment(new NewOrder(), "طلبات جديدة");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {

        }/*else if (id == R.id.nav_lang) {

        }*/ else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(MandobActivity.this, AboutUs.class));

        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(MandobActivity.this, ContactUs.class));

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MandobActivity.this, ProfileActivity.class));

        } else if (id == R.id.nav_myorders) {
            startActivity(new Intent(MandobActivity.this, MyOrdersTrader.class));

        } else if (id == R.id.nav_logout) {
            SharedHelper.putKey(MandobActivity.this ,"isLoged","");
            SharedHelper.putKey(MandobActivity.this ,"isType","");
            startActivity(new Intent(MandobActivity.this, NavActivity.class));
            finish();
        } else if (id == R.id.nav_mynoti) {
            startActivity(new Intent(MandobActivity.this, Notifications.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LanguageHelper.ChangeLang(getResources(),"ar");
        //startActivity(new Intent(MandobActivity.this , NotificationService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanguageHelper.ChangeLang(getResources(),"ar");
        //startActivity(new Intent(MandobActivity.this , NotificationService.class));
    }



    @Override
    protected void onStop() {
        super.onStop();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }
}