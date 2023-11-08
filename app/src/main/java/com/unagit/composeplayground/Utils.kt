package com.unagit.composeplayground

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

