package ru.is2si.sisi.base.device.location

import io.reactivex.Single
import ru.is2si.sisi.domain.ThrowExceptions

interface LocationDataSource {
    @ThrowExceptions(
            LocationPermissionDeniedException::class,
            IncorrectLocationSettingsException::class,
            TimeoutDetectLocationException::class
    )
    fun getLocation(): Single<Location>
}

