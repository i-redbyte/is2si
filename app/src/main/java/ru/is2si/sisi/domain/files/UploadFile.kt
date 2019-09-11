package ru.is2si.sisi.domain.files

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.files.UploadFile.Params
import javax.inject.Inject

class UploadFile @Inject constructor(
        private val filesDataSource: FilesDataSource
) : CompletableUseCase<Params>() {
    override fun execute(params: Params): Completable = filesDataSource
            .uploadFile(params.filePath, params.pin, params.teamName, params.type)

    class Params(
            val filePath: String,
            val pin: String,
            val teamName: String,
            val type: String
    )
}