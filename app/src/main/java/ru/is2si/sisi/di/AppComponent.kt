package ru.is2si.sisi.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.is2si.sisi.App
import ru.is2si.sisi.di.data.DataSourceModule
import ru.is2si.sisi.di.data.LocalDataModule
import ru.is2si.sisi.di.data.LocationModule
import ru.is2si.sisi.di.data.network.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class,
            AndroidSupportInjectionModule::class,
            LocalDataModule::class,
            NetworkModule::class,
            ScreenBindingModule::class,
            DataSourceModule::class,
            LocationModule::class
        ]
)

interface AppComponent : AndroidInjector<App> {
}