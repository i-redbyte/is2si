package ru.is2si.sisi.domain.auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.domain.result.CompetitionResultExpr
import ru.is2si.sisi.domain.result.CompetitionResult

interface AuthDataSource {

    fun authTeam(pin: String): Single<CompetitionResult>

    fun getSaveTeam(): Single<CompetitionResultExpr>

    fun saveTeam(team: CompetitionResultExpr)

    fun setServerUrl(url: String): Completable

    /**
     * @return is server url
     */
    fun getServerUrl(): Single<String>
}