package ru.is2si.sisi.presentation.model

import android.location.Location.distanceBetween
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.base.device.location.Location

@Parcelize
class LocationView(val latitude: Double, val longitude: Double) : Parcelable {

    fun metersDistanceTo(location: LocationView): Float {
        val result = FloatArray(1)
        distanceBetween(latitude, longitude, location.latitude, location.longitude, result)
        return result.first()
    }
}

fun Location.asView() = LocationView(latitude = latitude, longitude = longitude)

fun LocationView.asDomain() = Location(latitude = latitude, longitude = longitude)

