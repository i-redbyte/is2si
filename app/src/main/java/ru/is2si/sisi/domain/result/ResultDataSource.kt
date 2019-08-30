package ru.is2si.sisi.domain.result

import io.reactivex.Single

interface ResultDataSource {

    fun getResult(): Single<List<CompetitionResult>>

    fun getResults(competitionId: Int): Single<List<CompetitionResult>>
}