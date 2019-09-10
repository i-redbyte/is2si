package ru.is2si.sisi.domain.auth

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.points.PointDataSource
import ru.is2si.sisi.domain.result.CompetitionResult
import javax.inject.Inject

class AuthTeam @Inject constructor(
        private val authDataSource: AuthDataSource,
        private val pointDataSource: PointDataSource
) : SingleUseCase<CompetitionResult, AuthTeam.Param>() {
    private val NO_ID = -1
    override fun execute(params: Param): Single<CompetitionResult> =
            authDataSource.authTeam(params.pin)
                    .flatMap {
                        val result = it
                        pointDataSource.getPoints(it.competition?.id ?: NO_ID)
                                .flatMap { Single.just(result) }
                    }.doOnSuccess {
                        authDataSource.setTeamPin(params.pin)
                    }

    class Param(val pin: String)

}