package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.domain.common.Team

@Parcelize
class TeamView(
        val id: Int,
        val teamIsActive: Boolean,
        val teamName: String,
        val teamOwner: String,
        val teamTerritory: String
) : Parcelable

fun Team.asView() = TeamView(
        id = id,
        teamIsActive = teamIsActive,
        teamName = teamName,
        teamOwner = teamOwner ?: "",
        teamTerritory = teamTerritory ?: ""
)
