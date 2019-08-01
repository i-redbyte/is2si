package ru.is2si.sisi.data.auth

import android.content.SharedPreferences
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.base.extension.commit

class ServerUrlHolder constructor(
        private val sharedPreferences: SharedPreferences,
        private val defaultServerUrl: String
) {

    var serverUrl: String
        get() = sharedPreferences.getString(SERVER_URL, defaultServerUrl)!!
        set(value) = sharedPreferences.commit { putString(SERVER_URL, value) }

    private companion object {
        const val SERVER_URL = "$APPLICATION_ID.SERVER_URL"
    }

}