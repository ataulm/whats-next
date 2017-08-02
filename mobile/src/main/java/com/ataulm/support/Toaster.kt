package com.ataulm.support

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

class Toaster private constructor(private val context: Context) {

    private var toast: Toast? = null

    private fun toast(message: String) {
        toast?.cancel()

        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast!!.show()
    }

    companion object {

        @SuppressLint("StaticFieldLeak") // it's application context, and yep I'm gross, esta bien.
        private var toaster: Toaster? = null

        fun init(context: Context) {
            if (toaster == null) {
                toaster = Toaster(context.applicationContext)
            }
        }

        fun display(message: String) {
            ensureInitialized()
            toaster!!.toast(message)
        }

        private fun ensureInitialized() {
            if (toaster == null) {
                throw IllegalStateException("Toaster.init(Context) must be called once before use")
            }
        }
    }
}
