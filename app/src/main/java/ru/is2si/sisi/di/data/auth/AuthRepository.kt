package ru.is2si.sisi.di.data.auth

import io.reactivex.Single
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.di.data.UserResponse
import ru.is2si.sisi.domain.AuthDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val authApi: AuthApi,
        private val network: Network
) : AuthDataSource {

    override fun authUserForPin(pinCode: String): Single<UserResponse> {
        // TODO: RB 2019-06-02
        return Single.just(UserResponse(name = "LENIN"))
    }

}