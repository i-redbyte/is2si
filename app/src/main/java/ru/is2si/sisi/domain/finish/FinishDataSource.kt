package ru.is2si.sisi.domain.finish

import io.reactivex.Single

interface FinishDataSource {
    fun teamFinish(id: Int, pin: String): Single<Finish>
}