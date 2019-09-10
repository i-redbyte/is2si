package ru.is2si.sisi.data.finish

import io.reactivex.Single
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.finish.Finish
import ru.is2si.sisi.domain.finish.FinishDataSource
import javax.inject.Inject

class FinishRepository @Inject constructor(
        private val finishApi: FinishApi,
        private val network: Network
) : FinishDataSource {

    override fun teamFinish(id: Int, pin: String): Single<Finish> =
            network.prepareRequest(finishApi.teamFinish(id, pin))
                    .map(FinishResponse::toFinish)
}