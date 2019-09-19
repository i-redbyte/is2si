package ru.is2si.sisi.di.data

import dagger.Binds
import dagger.Module
import ru.is2si.sisi.base.device.location.LocationCalculator
import ru.is2si.sisi.base.device.location.LocationDataSource
import ru.is2si.sisi.data.device.location.LocationCalculatorImpl
import ru.is2si.sisi.data.device.location.LocationRepository
import ru.is2si.sisi.di.common.PerFeature

@Module
internal interface LocationModule {

    @PerFeature
    @Binds
    fun provideLocationDataSource(locationRepository: LocationRepository): LocationDataSource

    @PerFeature
    @Binds
    fun provideLocationCalculator(locationCalculator: LocationCalculatorImpl): LocationCalculator

}