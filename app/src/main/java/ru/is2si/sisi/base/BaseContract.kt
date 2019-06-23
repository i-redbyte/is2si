package ru.is2si.sisi.base


interface BaseContract {

    interface View

    interface Presenter {
        fun start()
        fun stop()
    }

}
