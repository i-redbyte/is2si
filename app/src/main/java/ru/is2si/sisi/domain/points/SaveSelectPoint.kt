package ru.is2si.sisi.domain.points

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import javax.inject.Inject

class SaveSelectPoint @Inject constructor(
    private val pointDataSource: PointDataSource
) : SingleUseCase<List<Point>, SaveSelectPoint.Params>() {

    override fun execute(params: Params): Single<List<Point>> =
        pointDataSource.getAllSavePoints()
            .flatMap {
                val p = it.firstOrNull { point -> point.pointName == params.pointName }
                pointDataSource.saveSelectPoint(p ?: throw RuntimeException("Точка не найдена!")) // TODO: Red_byte 2019-08-28 change custom Exception
                    .flatMap { points -> Single.just(points) }
            }

    class Params(val pointName: Int)
}