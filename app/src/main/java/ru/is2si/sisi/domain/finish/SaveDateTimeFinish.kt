package ru.is2si.sisi.domain.finish

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.finish.SaveDateTimeFinish.Params
import javax.inject.Inject

class SaveDateTimeFinish @Inject constructor(
        private val finishDataSource: FinishDataSource
) : CompletableUseCase<Params>() {

    override fun execute(params: Params): Completable =
            finishDataSource.saveDataTimeFinish(params.dateTimeFinish)

    class Params(val dateTimeFinish: String)

}