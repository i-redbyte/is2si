package ru.is2si.sisi.domain.auth

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class GetTeamPin @Inject constructor(
        private val authDataSource: AuthDataSource
) : SingleUseCase<String, None>() {

    override fun execute(params: None): Single<String> = authDataSource.getTeamPin()

}