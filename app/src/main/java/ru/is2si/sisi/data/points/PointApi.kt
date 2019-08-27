package ru.is2si.sisi.data.points

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PointApi {
    @GET("Api/pointCompetition/")
    fun getPoints(@Query("Id") id:Int): Single<List<PointResponse>>
}