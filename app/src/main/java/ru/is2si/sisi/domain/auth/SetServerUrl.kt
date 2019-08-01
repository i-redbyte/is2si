package ru.is2si.sisi.domain.auth

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.auth.SetServerUrl.*
import javax.inject.Inject

class SetServerUrl @Inject constructor(
        private val authDataSource: AuthDataSource
) : CompletableUseCase<Params>() {

    override fun execute(params: Params): Completable = authDataSource.setServerUrl(params.baseUrl)

    class Params(val baseUrl: String)

}