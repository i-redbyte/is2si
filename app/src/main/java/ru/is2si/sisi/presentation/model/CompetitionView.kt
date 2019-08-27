package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.domain.common.Competition

@Parcelize
class CompetitionView(
        val dataBegin: LocalDateTime,
        val dataEndNorm: LocalDateTime,
        val dataEndMax: LocalDateTime,
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
        dataEndNorm = dataEndNorm,
        dataEndMax = dataEndMax,
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

