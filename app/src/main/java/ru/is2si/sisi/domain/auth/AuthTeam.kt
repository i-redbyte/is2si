package ru.is2si.sisi.domain.auth

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.result.CompetitionResult
import javax.inject.Inject

class AuthTeam @Inject constructor(
        private val authDataSource: AuthDataSource
) : SingleUseCase<CompetitionResult, AuthTeam.Param>() {

    override fun execute(params: Param): Single<CompetitionResult> =
            authDataSource.authTeam(params.pin)

    class Param(val pin: String)
}