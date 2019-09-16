package ru.is2si.sisi

import androidx.appcompat.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import ru.is2si.sisi.base.report.ExceptionHandler
import ru.is2si.sisi.di.AppComponent
import ru.is2si.sisi.di.AppModule
import ru.is2si.sisi.di.DaggerAppComponent

class App : DaggerApplication() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.reportLogAndHandler())
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext, this))
            .build()
        return appComponent
    }
}