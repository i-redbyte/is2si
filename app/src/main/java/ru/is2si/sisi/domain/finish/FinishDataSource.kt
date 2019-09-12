package ru.is2si.sisi.domain.finish

import io.reactivex.Completable
import io.reactivex.Single

interface FinishDataSource {
    fun teamFinish(id: Int, pin: String): Single<Finish>

    fun saveDataTimeFinish(dataTimeFinish: String): Completable

    fun getDataTimeFinish(): Single<String>
}