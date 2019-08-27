package ru.is2si.sisi.domain.points

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class ClearAllPoints @Inject constructor(
    private val pointDataSource: PointDataSource
) : CompletableUseCase<None>() {

    override fun execute(params: None): Completable = pointDataSource.clearAllPoints()
}