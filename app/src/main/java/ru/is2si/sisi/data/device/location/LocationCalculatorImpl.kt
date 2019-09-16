package ru.is2si.sisi.data.device.location

import android.location.Location
import ru.is2si.sisi.base.device.location.LocationCalculator
import javax.inject.Inject

internal class LocationCalculatorImpl @Inject constructor() : LocationCalculator {

    override fun metersFromTo(
            fromLatitude: Double,
            fromLongitude: Double,
            toLatitude: Double,
            toLongitude: Double
    ): Double {
        val result = FloatArray(1)
        Location.distanceBetween(fromLatitude, fromLongitude, toLatitude, toLongitude, result)
        return result[0].toDouble()
    }

}