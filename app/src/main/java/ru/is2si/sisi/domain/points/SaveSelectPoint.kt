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
                        val p = it.firstOrNull { point -> point.pointNameStr == params.pointName }
                        pointDataSource.saveSelectPoint(p
                                ?: throw PointNotFoundException("Точка не найдена!"))
                                .flatMap { points -> Single.just(points) }
                    }

    class Params(val pointName: String)
}