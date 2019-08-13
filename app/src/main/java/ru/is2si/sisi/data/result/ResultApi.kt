package ru.is2si.sisi.data.result

import io.reactivex.Single
import retrofit2.http.GET

interface ResultApi {
    @GET("Api/monitor/")
    fun getResult(): Single<List<CompetitionResultResponse>>
}