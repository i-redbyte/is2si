@file:Suppress("NOTHING_TO_INLINE")

package ru.is2si.sisi.base.extension

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

inline fun Context.appSettingsIntent() = Intent().apply {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    data = Uri.fromParts("package", packageName, null)
}

inline fun Context.openLocation(latitude: Double, longitude: Double, address: String) = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:<$latitude>,<$longitude>?q=<$latitude>,<$longitude>($address)")
)

inline fun Context.openNotificationSettings() = Intent().also {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            it.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            it.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 -> {
            it.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            it.putExtra("app_package", packageName)
            it.putExtra("app_uid", applicationInfo.uid)
        }
        else -> {
            it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            it.addCategory(Intent.CATEGORY_DEFAULT)
            it.data = Uri.parse("package:$packageName")
        }
    }
}

inline fun Context.compatDrawable(@DrawableRes drawableResId: Int): Drawable =
        ContextCompat.getDrawable(this, drawableResId)!!

@ColorInt
inline fun Context.compatColor(@ColorRes colorResId: Int): Int =
        ContextCompat.getColor(this, colorResId)

inline fun Context.compatColorStateList(@ColorRes colorResId: Int): ColorStateList =
        ContextCompat.getColorStateList(this, colorResId)!!

@ColorInt
inline fun Fragment.compatColor(@ColorRes colorResId: Int): Int =
        requireContext().compatColor(colorResId)

inline fun Fragment.compatDrawable(@DrawableRes drawableResId: Int): Drawable =
        requireContext().compatDrawable(drawableResId)

inline fun Fragment.compatColorStateList(@ColorRes colorResId: Int): ColorStateList =
        ContextCompat.getColorStateList(requireContext(), colorResId)!!

inline fun Context.selectableItemBackgroundDrawable(): Drawable {
    val outValue = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    return compatDrawable(outValue.resourceId)
}
