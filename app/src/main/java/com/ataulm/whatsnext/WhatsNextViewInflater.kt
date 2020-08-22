package com.ataulm.whatsnext

import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
import androidx.appcompat.widget.*
import androidx.collection.ArrayMap
import com.google.android.material.theme.MaterialComponentsViewInflater
import java.lang.reflect.Constructor

/**
 * Copied bits and pieces from [AppCompatViewInflater] since some of the functions are final/private.
 * This was to allow us to hook into the `createView` for _any_ view, not just the AppCompat ones or custom ones.
 */
class WhatsNextViewInflater : MaterialComponentsViewInflater() {

    private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    private val sClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")
    private val sConstructorMap = ArrayMap<String, Constructor<out View>>()
    private val mConstructorArgs = arrayOfNulls<Any>(2)

    private fun <T : View> T.applySetClipToOutline(context: Context, attrs: AttributeSet?): T {
        val a = context.obtainStyledAttributes(attrs, R.styleable.View, 0, 0)
        clipToOutline = a.getBoolean(R.styleable.View_clipToOutline, false)
        a.recycle()
        return this
    }

    override fun createTextView(context: Context, attrs: AttributeSet): AppCompatTextView {
        return super.createTextView(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createImageView(context: Context, attrs: AttributeSet): AppCompatImageView {
        return super.createImageView(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createButton(context: Context, attrs: AttributeSet): AppCompatButton {
        return super.createButton(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createEditText(context: Context, attrs: AttributeSet): AppCompatEditText {
        return super.createEditText(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createSpinner(context: Context, attrs: AttributeSet): AppCompatSpinner {
        return super.createSpinner(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createImageButton(context: Context, attrs: AttributeSet): AppCompatImageButton {
        return super.createImageButton(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createCheckBox(context: Context, attrs: AttributeSet): AppCompatCheckBox {
        return super.createCheckBox(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createRadioButton(context: Context, attrs: AttributeSet): AppCompatRadioButton {
        return super.createRadioButton(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createCheckedTextView(context: Context, attrs: AttributeSet): AppCompatCheckedTextView {
        return super.createCheckedTextView(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createAutoCompleteTextView(context: Context, attrs: AttributeSet?): AppCompatAutoCompleteTextView {
        return super.createAutoCompleteTextView(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createMultiAutoCompleteTextView(context: Context, attrs: AttributeSet): AppCompatMultiAutoCompleteTextView {
        return super.createMultiAutoCompleteTextView(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createRatingBar(context: Context, attrs: AttributeSet): AppCompatRatingBar {
        return super.createRatingBar(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createSeekBar(context: Context, attrs: AttributeSet): AppCompatSeekBar {
        return super.createSeekBar(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createToggleButton(context: Context, attrs: AttributeSet): AppCompatToggleButton {
        return super.createToggleButton(context, attrs).applySetClipToOutline(context, attrs)
    }

    override fun createView(context: Context, passedName: String, attrs: AttributeSet): View? {
        var name = passedName
        if (name == "view") {
            name = attrs.getAttributeValue(null, "class")
        }

        try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs

            if (-1 == name.indexOf('.')) {
                for (i in sClassPrefixList.indices) {
                    val view = createViewByPrefix(context, name, sClassPrefixList[i])
                    if (view != null) {
                        return view.applySetClipToOutline(context, attrs)
                    }
                }
                return null
            } else {
                return createViewByPrefix(context, name, null)?.applySetClipToOutline(context, attrs)
            }
        } catch (e: Exception) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }

    @Throws(ClassNotFoundException::class, InflateException::class)
    private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
        var constructor = sConstructorMap[name]

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                val clazz = Class.forName(
                        if (prefix != null) prefix + name else name,
                        false,
                        context.classLoader).asSubclass(View::class.java)

                constructor = clazz.getConstructor(*sConstructorSignature)
                sConstructorMap[name] = constructor!!
            }
            constructor.isAccessible = true
            return constructor.newInstance(*mConstructorArgs)
        } catch (e: Exception) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null
        }
    }
}
