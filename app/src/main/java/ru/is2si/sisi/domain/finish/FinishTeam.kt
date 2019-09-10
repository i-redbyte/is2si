package ru.is2si.sisi.domain.finish

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.finish.FinishTeam.Param
import javax.inject.Inject

class FinishTeam @Inject constructor(
        private val finishDataSource: FinishDataSource
) : SingleUseCase<Finish, Param>() {

    override fun execute(params: Param): Single<Finish> =
            finishDataSource.teamFinish(params.id, params.pin)

    class Param(val id: Int, val pin: String)
}