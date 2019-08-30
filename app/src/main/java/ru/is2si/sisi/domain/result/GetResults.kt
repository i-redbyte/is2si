package ru.is2si.sisi.domain.result

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.result.GetResults.Params
import javax.inject.Inject

class GetResults @Inject constructor(
        private val resultDataSource: ResultDataSource
) : SingleUseCase<List<CompetitionResult>, Params>() {

    override fun execute(params: Params): Single<List<CompetitionResult>> =
            resultDataSource.getResults(params.competitionId)

    class Params(val competitionId: Int)
}