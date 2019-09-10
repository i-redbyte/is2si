package ru.is2si.sisi.data.finish


import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.base.extension.toLocalDateTime
import ru.is2si.sisi.domain.finish.Finish

class FinishResponse(
        @SerializedName("DataTimeFinish")
        val dataTimeFinish: String
)

fun FinishResponse.toFinish() = Finish(
        dataTimeFinish = dataTimeFinish.toLocalDateTime()
)