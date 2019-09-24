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
    override fun execute(params: Param): Single<CompetitionResult> =
            authDataSource.authTeam(params.pin)
                    .flatMap {
                        val result = it
                        pointDataSource.getPoints(result.competition?.id
                                ?: throw GetPointsNullException("Не удалось получить точки! ID соревнования null"))
                                .flatMap { Single.just(result) }
                    }

    class Param(val pin: String)
}