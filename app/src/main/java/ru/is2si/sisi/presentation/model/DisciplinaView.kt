package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.domain.common.Disciplina

@Parcelize
class DisciplinaView(
        val codeDiscipline: String,
        val id: Int,
        val idVidSporta: Int,
        val name: String
) : Parcelable

fun Disciplina.asView() = DisciplinaView(
        codeDiscipline = codeDiscipline,
        id = id,
        idVidSporta = idVidSporta,
        name = name
)