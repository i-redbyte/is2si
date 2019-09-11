package ru.is2si.sisi.domain.files

import io.reactivex.Completable
import ru.is2si.sisi.domain.CompletableUseCase
import ru.is2si.sisi.domain.files.SaveFilePathToQueue.Params
import javax.inject.Inject

class SaveFilePathToQueue @Inject constructor(
        private val filesDataSource: FilesDataSource
) : CompletableUseCase<Params>() {

    override fun execute(params: Params): Completable =
            filesDataSource.addFilePath(params.filePath)

    class Params(val filePath: String)
}