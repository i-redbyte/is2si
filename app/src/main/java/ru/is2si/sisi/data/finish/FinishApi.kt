package ru.is2si.sisi.data.finish

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FinishApi {
    @GET("Api/TeamEntryFinish/")
    fun teamFinish(
            @Query("id") id: Int,
            @Query("pin") pin: String
    ): Single<FinishResponse>

}