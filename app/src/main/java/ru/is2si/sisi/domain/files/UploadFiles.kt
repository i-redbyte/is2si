package ru.is2si.sisi.domain.files

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.files.UploadFiles.Params
import javax.inject.Inject

class UploadFiles @Inject constructor(
        private val filesDataSource: FilesDataSource
) : CompletableUseCase<Params>() {
    override fun execute(params: Params): Completable = filesDataSource
            .uploadFiles(params.filePaths, params.pin, params.teamName, params.type)

    class Params(
            val filePaths: List<String>,
            val pin: String,
            val teamName: String,
            val type: String
    )
}