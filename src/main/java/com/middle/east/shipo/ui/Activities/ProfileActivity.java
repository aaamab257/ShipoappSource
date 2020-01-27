package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageuser  , back;
    TextView uname , uphone , uarea , uemail ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        intViews();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void intViews() {
        back = findViewById(R.id.img_back);
        imageuser = findViewById(R.id.img_user);
        uname = findViewById(R.id.user_name);
        uphone = findViewById(R.id.user_phone);
        uarea = findViewById(R.id.user_area);
        uemail = findViewById(R.id.user_email);




        SharedHelper.getKey(this , "user_imageURL");
        uname.setText(SharedHelper.getKey(this , "first_name") +""+SharedHelper.getKey(this , "last_name"));
        uphone.setText(SharedHelper.getKey(this , "phone"));
        uarea.setText(SharedHelper.getKey(this , "area"));
        Picasso.with(this).load(SharedHelper.getKey(this , "user_imageURL")).into(imageuser);

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
