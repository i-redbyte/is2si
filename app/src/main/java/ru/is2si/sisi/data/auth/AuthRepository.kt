package ru.is2si.sisi.data.auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.auth.AuthDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val authApi: AuthApi,
        private val serverUrlHolder: ServerUrlHolder,
        private val network: Network
) : AuthDataSource {

    override fun setServerUrl(url: String): Completable =
            Completable.fromAction { serverUrlHolder.serverUrl = url }

    override fun getServerUrl(): Single<String> = Single.fromCallable { serverUrlHolder.serverUrl }

}