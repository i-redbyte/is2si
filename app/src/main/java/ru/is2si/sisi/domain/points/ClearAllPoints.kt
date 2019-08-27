package ru.is2si.sisi.domain.points

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import javax.inject.Inject

class ClearAllPoints @Inject constructor(
        private val pointDataSource: PointDataSource
) : CompletableUseCase<ClearAllPoints.Param>() {

    override fun execute(params: Param): Completable = pointDataSource.saveSelectPoint(params.point)

    class Param(val point: Point)
}