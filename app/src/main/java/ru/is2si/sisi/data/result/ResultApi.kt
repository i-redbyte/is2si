package ru.is2si.sisi.data.result

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ResultApi {
    @GET("Api/monitor/")
    fun getResult(): Single<List<CompetitionResultResponse>>

    @GET("Api/monitor/")
    fun getResults(@Query("Id") competitionId: Int): Single<List<CompetitionResultResponse>>
}