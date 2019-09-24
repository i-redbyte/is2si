package ru.is2si.sisi.data.points

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.base.extension.commit
import ru.is2si.sisi.base.extension.typeTokenOf
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.points.Point
import ru.is2si.sisi.domain.points.PointDataSource
import java.lang.reflect.Type
import javax.inject.Inject

class PointRepository @Inject constructor(
        private val pointApi: PointApi,
        private val network: Network,
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : PointDataSource {

    private val pointsTypeToken: Type by lazy { typeTokenOf<List<Point>>() }

    override fun getPoints(competitionId: Int): Single<List<Point>> =
            network.prepareRequest(pointApi.getPoints(competitionId))
                    .map {
                        it.map { pointResponse -> pointResponse.toPoint() }
                    }
                    .doOnSuccess(::saveAllPoints)

    override fun saveAllPoints(points: List<Point>) {
        sharedPreferences.commit { putString(ALL_POINTS, gson.toJson(points)) }
    }

    override fun getAllSavePoints(): Single<List<Point>> = Single.fromCallable {
        val allPoints = sharedPreferences.getString(ALL_POINTS, "")
        return@fromCallable gson.fromJson(allPoints, pointsTypeToken) ?: listOf<Point>()
    }

    override fun saveSelectPoint(point: Point): Single<List<Point>> = Single.fromCallable {
        val points: MutableSet<Point> = getSelectPoint().toMutableSet()
        if (points.find { it.id == point.id } == null) {
            points.add(point)
            sharedPreferences.commit { putString(SELECT_POINTS, gson.toJson(points)) }
        } else {
            throw PointAlreadyExistsException("Точка уже добавлена!")
        }
        return@fromCallable points.toList()
    }

    override fun getSelectPoints(): Single<List<Point>> = Single.fromCallable {
        val points = sharedPreferences.getString(SELECT_POINTS, "")
        return@fromCallable gson.fromJson(points, pointsTypeToken) ?: listOf<Point>()
    }

    override fun removeSelectPoint(point: Point): Single<List<Point>> = Single.fromCallable {
        val points = mutableListOf<Point>()
        getSelectPoint().forEach {
            if (it.id != point.id)
                points.add(it)
        }
        sharedPreferences.commit { putString(SELECT_POINTS, gson.toJson(points)) }
        return@fromCallable points.toList()
    }

    override fun clearAllPoints(): Completable = Completable.fromAction {
        sharedPreferences.commit { remove(ALL_POINTS) }
        sharedPreferences.commit { remove(SELECT_POINTS) }
    }

    private fun getSelectPoint(): List<Point> {
        val selectPoints = sharedPreferences.getString(SELECT_POINTS, "")
        return gson.fromJson(selectPoints, pointsTypeToken) ?: listOf()
    }

    companion object {
        private const val ALL_POINTS = "$APPLICATION_ID.ALL_POINTS"
        private const val SELECT_POINTS = "$APPLICATION_ID.SELECT_POINTS"
    }
}