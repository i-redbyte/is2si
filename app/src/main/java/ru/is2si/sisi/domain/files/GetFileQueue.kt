package ru.is2si.sisi.domain.files

import io.reactivex.Single
import ru.is2si.sisi.domain.SingleUseCase
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.points.PointDataSource
import javax.inject.Inject

class GetFileQueue @Inject constructor(
        private val filesDataSource: FilesDataSource
) : SingleUseCase<List<String>, None>() {

    override fun execute(params: None): Single<List<String>> =
            filesDataSource.getFilesPath()
}