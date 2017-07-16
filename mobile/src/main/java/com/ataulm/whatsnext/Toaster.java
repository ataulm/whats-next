package com.ataulm.whatsnext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public final class Toaster {

    @SuppressLint("StaticFieldLeak") // it's application context, and yep I'm gross, esta bien.
    private static Toaster toaster;

    private final Context context;
    private Toast toast;

    static void init(Context context) {
        toaster = new Toaster(context.getApplicationContext());
    }

    public static void display(String message) {
        toaster.toast(message);
    }

    private Toaster(Context context) {
        this.context = context;
    }

    private void toast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
