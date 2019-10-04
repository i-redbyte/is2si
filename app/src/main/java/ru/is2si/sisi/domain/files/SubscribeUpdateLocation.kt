package ru.is2si.sisi.domain.files

import io.reactivex.Observable
import ru.is2si.sisi.base.device.location.Location
import ru.is2si.sisi.base.device.location.LocationDataSource
import ru.is2si.sisi.domain.ObservableUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class SubscribeUpdateLocation @Inject constructor(
    private val locationDataSource: LocationDataSource
) : ObservableUseCase<Location, None>() {

    override fun execute(params: None): Observable<Location> = locationDataSource.subscribeUpdateLocation()

}