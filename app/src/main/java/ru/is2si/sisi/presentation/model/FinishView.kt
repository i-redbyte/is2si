package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.domain.finish.Finish

@Parcelize
class FinishView(
        val dataTimeFinish: LocalDateTime
) : Parcelable

fun Finish.asView() = FinishView(
        dataTimeFinish = dataTimeFinish
)