package ru.is2si.sisi.domain.points

import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import javax.inject.Inject

class SaveSelectPoint @Inject constructor(
        private val pointDataSource: PointDataSource
) : SingleUseCase<List<Point>, SaveSelectPoint.Params>() {

    override fun execute(params: Params): Single<List<Point>> =
            pointDataSource.getAllSavePoints()
                    .map { it.filter { point -> point.pointName == params.pointName } }
                    .flatMap {
                        val p = it.firstOrNull()
                        Completable.fromAction {
                            if (p != null)
                                pointDataSource.saveSelectPoint(p)
                            else
                                throw RuntimeException("Точка не найдена!")
                        }
                        pointDataSource.getSelectPoints()
                    }
    //.doOnComplete{pointDataSource.getSelectPoints()}

    class Params(val pointName: Int)
}