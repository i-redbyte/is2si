package ru.is2si.sisi.data.common

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.domain.common.Disciplina

class DisciplinaResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("CodeDiscipline")
        val codeDiscipline: String?,
        @SerializedName("IdVidSporta")
        val idVidSporta: Int,
        @SerializedName("Name")
        val name: String?
)

fun DisciplinaResponse.toDisciplina() = Disciplina(
        id = id,
        codeDiscipline = codeDiscipline ?: "",
        idVidSporta = idVidSporta,
        name = name ?: ""
)