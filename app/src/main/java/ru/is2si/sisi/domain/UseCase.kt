package ru.is2si.sisi.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface UseCase<out OutputT, in InputT> {
    fun execute(params: InputT): OutputT

    object None
}

abstract class SingleUseCase<T, in InputT> : UseCase<Single<T>, InputT> {
    abstract override fun execute(params: InputT): Single<T>
}

abstract class ObservableUseCase<T, in InputT> : UseCase<Observable<T>, InputT> {
    abstract override fun execute(params: InputT): Observable<T>
}

abstract class CompletableUseCase<in InputT> : UseCase<Completable, InputT> {
    abstract override fun execute(params: InputT): Completable
}