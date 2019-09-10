package ru.is2si.sisi.domain.auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.domain.result.CompetitionResultExpr

interface AuthDataSource {

    fun authTeam(pin: String): Single<CompetitionResult>

    fun getSaveTeam(): Single<CompetitionResultExpr>

    fun saveTeam(team: CompetitionResultExpr)

    fun logout(): Completable

    fun setServerUrl(url: String): Completable

    fun setTeamPin(pin: String): Completable

    fun getTeamPin(): Single<String>

    /**
     * @return is server url
     */
    fun getServerUrl(): Single<String>
}