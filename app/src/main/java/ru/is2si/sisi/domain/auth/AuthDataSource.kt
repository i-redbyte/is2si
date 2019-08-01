package ru.is2si.sisi.domain.auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.di.data.UserResponse

interface AuthDataSource {

    fun setServerUrl(url: String): Completable

    /**
     * @return is server url
     */
    fun getServerUrl(): Single<String>
}