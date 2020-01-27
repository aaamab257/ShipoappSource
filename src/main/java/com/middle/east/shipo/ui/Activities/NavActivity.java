package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.R;

public class NavActivity extends AppCompatActivity {
    CardView  registerbtn ;
    Animation RightSwipe , LeftSwipe , animLogo ;
    Button loginbtn;
    ImageView logoAnim ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        IntView(); //connection between xml and java
        RightSwipe = AnimationUtils.loadAnimation(NavActivity.this, R.anim.right_slide);
        logoAnim.startAnimation(RightSwipe);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goTreader = new Intent(NavActivity.this,UserType.class);
                goTreader.putExtra("user",1);
                SharedHelper.putKey(NavActivity.this , "type","1");//login
                startActivity(goTreader);
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goTreader = new Intent(NavActivity.this,UserType.class);
                goTreader.putExtra("user",0);
                SharedHelper.putKey(NavActivity.this , "type","0");//register
                startActivity(goTreader);
            }
        });
    }


    private void IntView() {
        logoAnim = findViewById(R.id.logo_img);
        loginbtn = findViewById(R.id.btn_login2);
        registerbtn = findViewById(R.id.btn_regis2);
    }
    @Override
    protected void onResume() {
        super.onResume();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LanguageHelper.ChangeLang(getResources(),"ar");
    }
}
