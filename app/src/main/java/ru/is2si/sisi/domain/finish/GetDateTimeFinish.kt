package ru.is2si.sisi.domain.finish

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class GetDateTimeFinish @Inject constructor(
        private val finishDataSource: FinishDataSource
) : SingleUseCase<String, None>() {

    override fun execute(params: None): Single<String> = finishDataSource.getDataTimeFinish()
}