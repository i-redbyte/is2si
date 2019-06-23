package ru.is2si.sisi.base.rx

import io.reactivex.Scheduler

interface RxSchedulers {
    val ui: Scheduler
    val io: Scheduler
    val computation: Scheduler
}