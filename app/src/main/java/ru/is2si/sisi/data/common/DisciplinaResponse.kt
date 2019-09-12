package ru.is2si.sisi.data.common

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.domain.common.Disciplina
import ru.is2si.sisi.domain.common.Discipline

class DisciplinaResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("CodeDiscipline")
        val codeDiscipline: String?,
        @SerializedName("IdVidSporta")
        val idVidSporta: Int,
        @SerializedName("Name")
        val name: String?,
        @SerializedName("Discipline")
        val discipline: DisciplineResponse
)

fun DisciplinaResponse.toDisciplina() = Disciplina(
        id = id,
        codeDiscipline = codeDiscipline ?: "",
        idVidSporta = idVidSporta,
        name = name ?: "",
        discipline = discipline.toDiscipline()
)

class DisciplineResponse(
        @SerializedName("Name")
        val name: String
)

fun DisciplineResponse.toDiscipline() = Discipline(
        name = name
)
