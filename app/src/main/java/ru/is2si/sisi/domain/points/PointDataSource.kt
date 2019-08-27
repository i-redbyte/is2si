package ru.is2si.sisi.domain.points

import io.reactivex.Completable
import io.reactivex.Single

interface PointDataSource {
    fun getPoints(competitionId: Int): Single<List<Point>>

    fun saveAllPoints(points: List<Point>)

    fun getAllSavePoints(): Single<List<Point>>

    fun saveSelectPoint(point: Point): Completable

    fun getSelectPoints(): Single<List<Point>>

    fun removeSelectPoint(point: Point): Completable

    fun clearAllPoints(): Completable
}