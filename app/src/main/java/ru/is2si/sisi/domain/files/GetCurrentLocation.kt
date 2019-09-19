package ru.is2si.sisi.domain.files

import io.reactivex.Single
import ru.is2si.sisi.base.device.location.Location
import ru.is2si.sisi.base.device.location.LocationDataSource
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class GetCurrentLocation @Inject constructor(
        private val locationDataSource: LocationDataSource
) : SingleUseCase<Location, None>() {

    override fun execute(params: None): Single<Location> = locationDataSource.getLocation()

}