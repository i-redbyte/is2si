package ru.is2si.sisi.domain.result

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class GetResult @Inject constructor(
        private val resultDataSource: ResultDataSource
) : SingleUseCase<List<CompetitionResult>, None>() {

    override fun execute(params: None): Single<List<CompetitionResult>> =
            resultDataSource.getResult()
}