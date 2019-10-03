package ru.is2si.sisi.data.device.location

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.lang.ref.WeakReference

class WeakLocationCallback(locationCallback: LocationCallback) : LocationCallback() {

    private val weakLocationCallback = WeakReference<LocationCallback>(locationCallback)
    override fun onLocationResult(result: LocationResult) {
        super.onLocationResult(result)
        weakLocationCallback.get()?.onLocationResult(result)
    }
}
