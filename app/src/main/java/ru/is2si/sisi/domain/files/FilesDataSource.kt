package ru.is2si.sisi.domain.files

import io.reactivex.Completable
import io.reactivex.Single

interface FilesDataSource {
    fun getFilesPath(): Single<List<String>>

    fun addFilePath(path: String): Completable

    fun uploadFile(file: String, pin:String, teamName:String): Completable
}