package ru.is2si.sisi.presentation.model

import android.location.Location.distanceBetween
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.base.device.location.Location

// TODO: Red_byte 2019-10-04 var for test
@Parcelize
class LocationView(var latitude: Double, var longitude: Double) : Parcelable {

    fun metersDistanceTo(location: LocationView): Float {
        val result = FloatArray(1)
        distanceBetween(latitude, longitude, location.latitude, location.longitude, result)
        return result.first()
    }
}

fun Location.asView() = LocationView(latitude = latitude, longitude = longitude)

fun LocationView.asDomain() = Location(latitude = latitude, longitude = longitude)

