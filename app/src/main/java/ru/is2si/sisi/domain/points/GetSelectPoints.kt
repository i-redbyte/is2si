package ru.is2si.sisi.domain.points

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import javax.inject.Inject

class GetSelectPoints @Inject constructor(
        private val pointDataSource: PointDataSource
) : SingleUseCase<List<Point>, None>() {

    override fun execute(params: None): Single<List<Point>> =
            pointDataSource.getSelectPoints()
}