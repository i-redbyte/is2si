package ru.is2si.sisi.domain.auth

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class Logout @Inject constructor(
        private val authDataSource: AuthDataSource
) : CompletableUseCase<None>() {
    override fun execute(params: None): Completable = authDataSource.logout()

}