package ru.is2si.sisi.data.files

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.base.extension.commit
import ru.is2si.sisi.base.extension.typeTokenOf
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.files.FilesDataSource
import java.lang.reflect.Type
import javax.inject.Inject

class FilesRepository @Inject constructor(
        private val filesApi: FilesApi,
        private val network: Network,
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : FilesDataSource {
    private val filesTypeToken: Type by lazy { typeTokenOf<List<String>>() }

    override fun addFilePath(path: String): Completable = Completable.fromAction {
        val paths: MutableSet<String> = getPaths().toMutableSet()
        paths.add(path)
        sharedPreferences.commit { putString(ALL_FILES_PATH, gson.toJson(paths)) }
    }

    override fun getFilesPath(): Single<List<String>> = Single.fromCallable {
        val paths = sharedPreferences.getString(ALL_FILES_PATH, "")
        return@fromCallable gson.fromJson(paths, filesTypeToken) ?: listOf<String>()
    }

    private fun getPaths(): List<String> {
        val selectPoints = sharedPreferences.getString(ALL_FILES_PATH, "")
        return gson.fromJson(selectPoints, filesTypeToken) ?: listOf()
    }

    companion object {
        private const val ALL_FILES_PATH = "$APPLICATION_ID.ALL_FILES_PATH"
    }
}