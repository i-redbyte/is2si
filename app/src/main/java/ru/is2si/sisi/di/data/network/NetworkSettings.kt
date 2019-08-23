package ru.is2si.sisi.di.data.network

import ru.is2si.sisi.BuildConfig
import ru.is2si.sisi.base.network.NetworkSettings

object NetworkSettingsFactory {

    fun create(): NetworkSettings = object : NetworkSettings {
        @Suppress("ConstantConditionIf")
        override val baseUrl: String = "http://46.173.219.44:8000/"

        override val readTimeout: Long = 30
        override val connectionTimeout: Long = 30

        override val detectLocationTimeout: Long = 15

        override val logRequests: Boolean = BuildConfig.DEBUG
    }

}