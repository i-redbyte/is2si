package ru.is2si.sisi.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.base.rx.RxSchedulersProvider
import javax.inject.Singleton

@Module
class AppModule(
        private val appContext: Context,
        private val application: Application
) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = appContext

    @Provides
    @Singleton
    fun provideRxSchedulers(): RxSchedulers = RxSchedulersProvider()

}
