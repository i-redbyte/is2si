package ru.is2si.sisi.domain

import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.di.data.UserResponse

interface AuthDataSource {

    fun authUserForPin(pinCode:String): Single<UserResponse>
}