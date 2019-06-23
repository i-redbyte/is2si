package ru.is2si.sisi.di.data

import dagger.Module
import dagger.Provides
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.di.data.auth.AuthApi
import ru.is2si.sisi.di.data.auth.AuthRepository
import ru.is2si.sisi.domain.AuthDataSource
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(
            authApi: AuthApi,
            network: Network
    ): AuthDataSource = AuthRepository(authApi, network)


}