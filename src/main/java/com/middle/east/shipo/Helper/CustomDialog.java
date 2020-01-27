package com.middle.east.shipo.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.middle.east.shipo.R;

public class CustomDialog extends ProgressDialog {

    public CustomDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setIndeterminate(true);
        setMessage("الرجاء الانتظار");
//        setContentView(R.layout.custom_dialog);
    }

}
