package ru.is2si.sisi.data.finish

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.base.extension.commit
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.finish.Finish
import ru.is2si.sisi.domain.finish.FinishDataSource
import ru.tinkoff.decoro.BuildConfig.APPLICATION_ID
import javax.inject.Inject

class FinishRepository @Inject constructor(
        private val finishApi: FinishApi,
        private val network: Network,
        private val sharedPreferences: SharedPreferences
) : FinishDataSource {

    override fun teamFinish(id: Int, pin: String): Single<Finish> =
            network.prepareRequest(finishApi.teamFinish(id, pin))
                    .map(FinishResponse::toFinish)

    override fun saveDataTimeFinish(dataTimeFinish: String): Completable = Completable.fromAction {
        sharedPreferences.commit { putString(DATE_TIME_FINISH_TEAM, dataTimeFinish) }
    }

    override fun getDataTimeFinish(): Single<String> = Single.fromCallable {
        sharedPreferences.getString(DATE_TIME_FINISH_TEAM, "")
    }

    companion object {
        const val DATE_TIME_FINISH_TEAM = "$APPLICATION_ID.DATE_TIME_FINISH_TEAM"
    }

}