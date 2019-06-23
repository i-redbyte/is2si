package ru.is2si.sisi.base.extension

import android.content.res.Resources
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension

fun Int.dp(): Int {
    val displayMetrics = Resources.getSystem().displayMetrics
    return applyDimension(COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics).toInt()
}