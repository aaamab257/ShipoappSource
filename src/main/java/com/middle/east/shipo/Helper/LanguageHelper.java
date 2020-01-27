package com.middle.east.shipo.Helper;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {
    public static void ChangeLang(Resources resources , String locale1){
        Configuration Config ;
        Config = new Configuration(resources.getConfiguration());

        switch (locale1){
            case "ar" :
                Locale locale = new Locale(locale1);
                Config.locale = new Locale("ar");
                Config.setLayoutDirection(locale);
                break;
        }
        resources.updateConfiguration(Config,resources.getDisplayMetrics());
    }
}
