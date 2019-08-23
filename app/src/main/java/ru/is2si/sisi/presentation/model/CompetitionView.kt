package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.domain.common.Competition

@Parcelize
class CompetitionView(
        val dataBegin: String,
        val dataEnd: String,
        val defaultCompetition: Boolean,
        val id: Int,
        val isActive: Boolean,
        val mainJudge: String,
        val mainSecretary: String,
        val nameCompetition: String,
        val nameLittel: String,
        val organizingAuthority: String,
        val resultAsFileTo: Boolean,
        val venue: String
) : Parcelable

fun Competition.asView() = CompetitionView(
        dataBegin = dataBegin,
        dataEnd = dataEnd,
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