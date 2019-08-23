package ru.is2si.sisi.data.auth

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.is2si.sisi.data.result.CompetitionResultResponse

interface AuthApi {
    @GET("Api/TeamEntryAuthPin")
    fun authTeam(@Query("Pin") pin: String): Single<CompetitionResultResponse>
}