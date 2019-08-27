package ru.is2si.sisi.data.common

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.base.extension.toLocalDateTime
import ru.is2si.sisi.domain.common.Competition

class CompetitionResponse(
        @SerializedName("DataBegin")
        val dataBegin: String,
        @SerializedName("DataEndNorm")
        val dataEndNorm: String,
        @SerializedName("DataEndMax")
        val dataEndMax: String,
        @SerializedName("DefaultCompetition")
        val defaultCompetition: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_Active")
        val isActive: Boolean,
        @SerializedName("MainJudge")
        val mainJudge: String,
        @SerializedName("MainSecretary")
        val mainSecretary: String,
        @SerializedName("NameCompetition")
        val nameCompetition: String,
        @SerializedName("NameLittel")
        val nameLittel: String,
        @SerializedName("OrganizingAuthority")
        val organizingAuthority: String,
        @SerializedName("ResultAsFileTo")
        val resultAsFileTo: Boolean,
        @SerializedName("Venue")
        val venue: String
)

fun CompetitionResponse.toCompetition() = Competition(
        dataBegin = dataBegin.toLocalDateTime(),
        dataEndNorm = dataEndNorm.toLocalDateTime(),
        dataEndMax = dataEndMax.toLocalDateTime(),
        defaultCompetition = defaultCompetition,
        id = id,
        isActive = isActive,
        mainJudge = mainJudge,
        mainSecretary = mainSecretary,
        nameCompetition = nameCompetition,
        nameLittel = nameLittel,
        organizingAuthority = organizingAuthority,
        resultAsFileTo = resultAsFileTo,
        venue = venue
)
