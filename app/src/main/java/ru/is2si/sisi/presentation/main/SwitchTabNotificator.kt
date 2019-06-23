package ru.is2si.sisi.presentation.main

import io.reactivex.Observable
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

class SwitchTabNotificator @Inject constructor() {

    private val listeners: CopyOnWriteArrayList<(Tab) -> Unit> = CopyOnWriteArrayList()

    fun subscribeToNotifications(): Observable<Tab> {
        return Observable.create {
            val listener = it::onNext
            listeners.add(listener)
            it.setCancellable { listeners.remove(listener) }
        }
    }

    fun notify(tab: Tab) = listeners.forEach { it.invoke(tab) }

}