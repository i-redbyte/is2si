package ru.is2si.sisi.domain.points

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import javax.inject.Inject

class RemoveSelectPoint @Inject constructor(
    private val pointDataSource: PointDataSource
) : SingleUseCase<List<Point>, RemoveSelectPoint.Param>() {

    override fun execute(params: Param): Single<List<Point>> =
        pointDataSource.removeSelectPoint(params.point)

    class Param(val point: Point)
}