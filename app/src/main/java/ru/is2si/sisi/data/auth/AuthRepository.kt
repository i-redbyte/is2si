package ru.is2si.sisi.data.auth

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.base.extension.commit
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.data.result.toCompetitionResult
import ru.is2si.sisi.domain.auth.AuthDataSource
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.domain.result.CompetitionResultExpr
import ru.is2si.sisi.domain.result.EmptyCompetitionResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val authApi: AuthApi,
        private val serverUrlHolder: ServerUrlHolder,
        private val network: Network,
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : AuthDataSource {

    override fun authTeam(pin: String): Single<CompetitionResult> =
            network.prepareRequest(authApi.authTeam(pin))
                    .map { it.toCompetitionResult() }
                    .doOnSuccess(::saveTeam)

    override fun setServerUrl(url: String): Completable =
            Completable.fromAction { serverUrlHolder.serverUrl = url }

    override fun getServerUrl(): Single<String> = Single.fromCallable { serverUrlHolder.serverUrl }

    override fun getSaveTeam(): Single<CompetitionResultExpr> = Single.fromCallable {
        val currentTeam = sharedPreferences.getString(CURRENT_TEAM, null)
        if (currentTeam == null)
            EmptyCompetitionResult
        else
            gson.fromJson(currentTeam, CompetitionResult::class.java)
    }

    override fun saveTeam(team: CompetitionResultExpr) {
        sharedPreferences.commit { putString(CURRENT_TEAM, gson.toJson(team)) }
    }

    override fun logout(): Completable = Completable.fromAction {
        sharedPreferences.commit { remove(CURRENT_TEAM) }
    }

    companion object {
        const val CURRENT_TEAM = "$APPLICATION_ID.CURRENT_TEAM"
    }
}