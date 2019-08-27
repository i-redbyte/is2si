package ru.is2si.sisi.di.data

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import ru.is2si.sisi.data.auth.AuthApi
import ru.is2si.sisi.data.auth.AuthRepository
import ru.is2si.sisi.data.auth.ServerUrlHolder
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.data.points.PointApi
import ru.is2si.sisi.data.points.PointRepository
import ru.is2si.sisi.data.result.ResultApi
import ru.is2si.sisi.data.result.ResultRepository
import ru.is2si.sisi.domain.auth.AuthDataSource
import ru.is2si.sisi.domain.points.PointDataSource
import ru.is2si.sisi.domain.result.ResultDataSource
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(
            authApi: AuthApi,
            urlHolder: ServerUrlHolder,
            network: Network,
            sharedPreferences: SharedPreferences,
            gson: Gson

    ): AuthDataSource = AuthRepository(authApi, urlHolder, network, sharedPreferences, gson)

    @Provides
    @Singleton
    fun provideResultDataSource(
            resultApi: ResultApi,
            network: Network
    ): ResultDataSource = ResultRepository(resultApi, network)

    @Provides
    @Singleton
    fun providePointDataSource(
            pointApi: PointApi,
            sharedPreferences: SharedPreferences,
            gson: Gson,
            network: Network
    ): PointDataSource = PointRepository(
            network = network,
            pointApi = pointApi,
            sharedPreferences = sharedPreferences,
            gson = gson
    )


}