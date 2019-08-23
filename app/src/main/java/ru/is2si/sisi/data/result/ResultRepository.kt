package ru.is2si.sisi.data.result

import io.reactivex.Single
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.result.CompetitionResult
import ru.is2si.sisi.domain.result.ResultDataSource
import javax.inject.Inject

internal class ResultRepository @Inject constructor(
        private val resultApi: ResultApi,
        private val network: Network
) : ResultDataSource {
    override fun getResult(): Single<List<CompetitionResult>> =
            network.prepareRequest(resultApi.getResult())
                    .map { it.map(CompetitionResultResponse::toCompetitionResult) }
}