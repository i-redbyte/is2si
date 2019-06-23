package ru.is2si.sisi.base.extension

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

private fun isPermissionsGranted(activity: Activity, vararg permissions: String): Boolean =
        permissions.all { ContextCompat.checkSelfPermission(activity, it) == PERMISSION_GRANTED }

private fun isRequestedPermissionsGranted(grantResults: IntArray): Boolean =
        grantResults.all { it == PERMISSION_GRANTED }

private fun isShouldRequestPermissionRationale(
        activity: Activity,
        vararg permissions: String
): Boolean {
    permissions.forEach {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it))
            return true
    }
    return false
}

fun Fragment.beforeRequestPermissions(
        requestCode: Int,
        vararg permissions: String
): BeforeRequestPermissionResult = beforeRequestPermissions(requestCode, false, *permissions)

fun Fragment.beforeRequestPermissions(
        requestCode: Int,
        force: Boolean,
        vararg permissions: String
): BeforeRequestPermissionResult {
    if (isPermissionsGranted(requireActivity(), *permissions)) {
        return BeforeRequestPermissionResult.AlreadyGranted
    } else {
        if (force.not()) {
            if (isShouldRequestPermissionRationale(requireActivity(), *permissions)) {
                return BeforeRequestPermissionResult.ShowRationale
            }
        }
        requestPermissions(permissions, requestCode)
        return BeforeRequestPermissionResult.Requested
    }
}

fun Fragment.afterRequestPermissions(
        permissions: Array<String>,
        grantResults: IntArray
): AfterRequestPermissionsResult = when {
    isRequestedPermissionsGranted(grantResults) -> AfterRequestPermissionsResult.Granted
    isShouldRequestPermissionRationale(requireActivity(), *permissions).not() -> AfterRequestPermissionsResult.NeverAskAgain
    else -> AfterRequestPermissionsResult.Denied
}

sealed class BeforeRequestPermissionResult {
    object AlreadyGranted : BeforeRequestPermissionResult()
    object ShowRationale : BeforeRequestPermissionResult()
    object Requested : BeforeRequestPermissionResult()
}

sealed class AfterRequestPermissionsResult {
    object Granted : AfterRequestPermissionsResult()
    object NeverAskAgain : AfterRequestPermissionsResult()
    object Denied : AfterRequestPermissionsResult()
}