package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.domain.common.Disciplina
import ru.is2si.sisi.domain.common.Discipline

@Parcelize
class DisciplinaView(
        val codeDiscipline: String,
        val id: Int,
        val idVidSporta: Int,
        val name: String,
        val discipline: DisciplineView
) : Parcelable

fun Disciplina.asView() = DisciplinaView(
        codeDiscipline = codeDiscipline,
        id = id,
        idVidSporta = idVidSporta,
        name = name,
        discipline = discipline.asView()
)

@Parcelize
class DisciplineView(
        val groupName: String
) : Parcelable

fun Discipline.asView() = DisciplineView(
        groupName = name
)