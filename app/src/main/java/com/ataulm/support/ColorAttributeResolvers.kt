package com.ataulm.support

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources

/**
 * If it's possible to use a [ColorStateList] instead of a color int,
 * then use [Context.resolveColorStateListAttribute] instead.
 */
@ColorInt
fun Context.resolveColorAttribute(@AttrRes attr: Int): Int {
    return resolveColorStateListAttribute(attr).defaultColor
}

fun Context.resolveColorStateListAttribute(@AttrRes attr: Int): ColorStateList {
    return TypedValue().let { value ->
        theme.resolveAttribute(attr, value, true)
        when {
            value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT -> {
                // This is a color hex value. Use value.data
                ColorStateList.valueOf(value.data)
            }
            value.resourceId != 0 -> {
                AppCompatResources.getColorStateList(this, value.resourceId)
            }
            else -> {
                throw Resources.NotFoundException("Couldn't find ${resources.getResourceName(attr)} in theme.")
            }
        }
    }
}
