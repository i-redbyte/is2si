package ru.is2si.sisi.domain.points

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import javax.inject.Inject

class RemoveSelectPoint @Inject constructor(
        private val pointDataSource: PointDataSource
) : CompletableUseCase<RemoveSelectPoint.Param>() {

    override fun execute(params: Param): Completable = pointDataSource.removeSelectPoint(params.point)

    class Param(val point: Point)
}