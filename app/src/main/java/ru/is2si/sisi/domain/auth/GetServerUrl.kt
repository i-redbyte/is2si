package ru.is2si.sisi.domain.auth

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase
import javax.inject.Inject

class GetServerUrl @Inject constructor(
        private val authDataSource: AuthDataSource
) : SingleUseCase<String, UseCase.None>() {

    override fun execute(params: UseCase.None): Single<String> = authDataSource.getServerUrl()

}