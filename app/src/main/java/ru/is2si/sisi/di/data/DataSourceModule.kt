package ru.is2si.sisi.di.data

import dagger.Module
import dagger.Provides
import ru.is2si.sisi.data.auth.AuthApi
import ru.is2si.sisi.data.auth.AuthRepository
import ru.is2si.sisi.data.auth.ServerUrlHolder
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.data.result.ResultApi
import ru.is2si.sisi.data.result.ResultRepository
import ru.is2si.sisi.domain.auth.AuthDataSource
import ru.is2si.sisi.domain.result.ResultDataSource
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(
            authApi: AuthApi,
            urlHolder: ServerUrlHolder,
            network: Network
    ): AuthDataSource = AuthRepository(authApi, urlHolder, network)

    @Provides
    @Singleton
    fun provideResultDataSource(
            resultApi: ResultApi,
            network: Network
    ): ResultDataSource = ResultRepository(resultApi, network)


}