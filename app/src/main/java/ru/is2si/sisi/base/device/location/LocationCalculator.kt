package ru.is2si.sisi.base.device.location

interface LocationCalculator {
    fun metersFromTo(
            fromLatitude: Double,
            fromLongitude: Double,
            toLatitude: Double,
            toLongitude: Double
    ): Double
}