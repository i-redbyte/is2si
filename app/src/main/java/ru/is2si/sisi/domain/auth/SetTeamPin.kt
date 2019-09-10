package ru.is2si.sisi.domain.auth

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.auth.SetTeamPin.Params
import javax.inject.Inject

class SetTeamPin @Inject constructor(
        private val authDataSource: AuthDataSource
) : CompletableUseCase<Params>() {

    override fun execute(params: Params): Completable = authDataSource.setTeamPin(params.pin)

    class Params(val pin: String)

}