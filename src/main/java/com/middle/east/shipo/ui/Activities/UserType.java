package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.R;

public class UserType extends AppCompatActivity {
    RelativeLayout user0 , user1 ;
    TextView text ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        IntViews();
        String type = SharedHelper.getKey(this , "type");
        if(type.equals("0")){
            text.setText("تسجيل حساب جديد كـ");
            user1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goTreader = new Intent(UserType.this, EnterPhone.class);
                    SharedHelper.putKey(UserType.this , "user","1");//Trader
                    startActivity(goTreader);
                }
            });
            user0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goTreader = new Intent(UserType.this, EnterPhone.class);
                    SharedHelper.putKey(UserType.this , "user","0");//Mandob
                    startActivity(goTreader);
                }
            });
        }else if(type.equals("1")){
            text.setText("تسجيل دخول كـ");
            user0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goTreader = new Intent(UserType.this,LoginScreen.class);
                    SharedHelper.putKey(UserType.this , "user","0");//Mandob
                    startActivity(goTreader);
                }
            });
            user1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goTreader = new Intent(UserType.this,LoginScreen.class);
                    SharedHelper.putKey(UserType.this , "user","1");//Mandob
                    startActivity(goTreader);
                }
            });
        }


    }

    private void IntViews() {
        user0 = findViewById(R.id.btn_mandob);
        user1 = findViewById(R.id.btn_trader);
        text = findViewById(R.id.txt_type);
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
