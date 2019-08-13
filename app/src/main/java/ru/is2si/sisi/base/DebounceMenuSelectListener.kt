package ru.is2si.sisi.base

import android.os.SystemClock
import android.view.MenuItem

class DebounceMenuSelectListener(
        private val delayMs: Long = 800,
        private val callBack: MenuSelectCallback
) {

    private var selectTime: Long = SystemClock.elapsedRealtime()

    fun onSelected(item: MenuItem): Boolean {
        val currentTime = SystemClock.elapsedRealtime()
        return if (currentTime - selectTime >= delayMs) {
            selectTime = currentTime
            callBack.onMenuSelected(item)
        } else {
            return true
        }
    }

}

interface MenuSelectCallback {
    fun onMenuSelected(item: MenuItem): Boolean
}