package ru.is2si.sisi.base.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io

class RxSchedulersProvider : RxSchedulers {
    override val ui: Scheduler = mainThread()
    override val io: Scheduler = io()
    override val computation: Scheduler = computation()
}