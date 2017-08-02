package com.ataulm.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public final class Toaster {

    @SuppressLint("StaticFieldLeak") // it's application context, and yep I'm gross, esta bien.
    private static Toaster toaster;

    private final Context context;

    private Toast toast;

    static void init(Context context) {
        if (toaster == null) {
            toaster = new Toaster(context.getApplicationContext());
        }
    }

    public static void display(String message) {
        ensureInitialized();
        toaster.toast(message);
    }

    private static void ensureInitialized() {
        if (toaster == null) {
            throw new IllegalStateException("Toaster.init(Context) must be called once before use");
        }
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
