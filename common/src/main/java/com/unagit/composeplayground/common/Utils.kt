package com.unagit.composeplayground.common

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

fun Context.drawable(@DrawableRes res: Int?): Drawable? = res?.let { ContextCompat.getDrawable(this, res) }

