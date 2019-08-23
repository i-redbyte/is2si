package ru.is2si.sisi.domain.auth

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.result.CompetitionResultExpr
import javax.inject.Inject

class GetSaveTeam @Inject constructor(
        private val authDataSource: AuthDataSource
) : SingleUseCase<CompetitionResultExpr, None>() {

    override fun execute(params: None): Single<CompetitionResultExpr> =
            authDataSource.getSaveTeam()

}