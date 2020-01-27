package com.middle.east.shipo.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.middle.east.shipo.Helper.LanguageHelper;
import com.middle.east.shipo.Helper.SharedHelper;
import com.middle.east.shipo.R;

public class SplashScreen extends AppCompatActivity {
    ImageView bg ;
    TextView shipo , Eg ;
    Animation RightSwipe , LeftSwipe , animLogo ;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        LanguageHelper.ChangeLang(getResources(),"ar");
        /*SharedHelper.putKey(LoginScreen.this ,"isLoged","true");
        SharedHelper.putKey(LoginScreen.this ,"isType","1");*/

        bg = findViewById(R.id.image_background);
        shipo = findViewById(R.id.txt_shipo);
        Eg = findViewById(R.id.txt_EG);
        RightSwipe = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.right_slide);
        shipo.startAnimation(RightSwipe);

        LeftSwipe = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.left_slide);
        Eg.startAnimation(LeftSwipe);
        animLogo = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.translate);
        bg.startAnimation(RightSwipe);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(SharedHelper.getKey(SplashScreen.this ,"isLoged").equals("true")){
                    if (SharedHelper.getKey(SplashScreen.this ,"isType").equals("0")){
                        startActivity(new Intent(SplashScreen.this  , MandobActivity.class));
                        finish();
                    }else if (SharedHelper.getKey(SplashScreen.this ,"isType").equals("1")){
                        startActivity(new Intent(SplashScreen.this  , HomeActivity.class));
                        finish();
                    }
                }else {
                    Intent intent = new Intent(SplashScreen.this, NavActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            SplashScreen.this, bg, ViewCompat.getTransitionName(bg));
                    startActivity(intent, options.toBundle());
                    finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);

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
