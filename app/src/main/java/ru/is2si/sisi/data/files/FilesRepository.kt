package ru.is2si.sisi.data.files

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.is2si.sisi.BuildConfig.APPLICATION_ID
import ru.is2si.sisi.base.extension.commit
import ru.is2si.sisi.base.extension.typeTokenOf
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.domain.files.FilesDataSource
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject

class FilesRepository @Inject constructor(
        private val filesApi: FilesApi,
        private val network: Network,
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : FilesDataSource {
    private val filesTypeToken: Type by lazy { typeTokenOf<List<String>>() }

    override fun uploadFile(
            file: String,
            pin: String,
            teamName: String,
            type: String
    ): Completable {
        return network.prepareRequest(filesApi.uploadFile(
                type,
                pin,
                teamName,
                getMultipart(file, type)
        ))
    }

    private fun getMultipart(filePath: String, type: String): MultipartBody.Part {
        val file = File(filePath)
        val fileReqBody =
                if (type == TYPE_PHOTOS)
                    RequestBody.create(MediaType.parse("image/jpg"), file)
                else {
                    RequestBody.create(MediaType.parse("file/*"), file)
                }
        return MultipartBody.Part.createFormData("document", file.name, fileReqBody)
    }

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
        const val TYPE_PHOTOS = "Fotos"
        const val TYPE_TRACK = "Tracks"
    }
}