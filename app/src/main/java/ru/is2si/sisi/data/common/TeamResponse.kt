package ru.is2si.sisi.data.common

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.domain.common.Team

class TeamResponse(
        @SerializedName("teamId")
        val id: Int,
        @SerializedName("TeamIs_Active")
        val teamIsActive: Boolean,
        @SerializedName("TeamName")
        val teamName: String,
        @SerializedName("TeamOwner")
        val teamOwner: String?,
        @SerializedName("TeamTerritory")
        val teamTerritory: String?
)

fun TeamResponse.toTeam() = Team(
        id = id,
        teamIsActive = teamIsActive,
        teamName = teamName,
        teamOwner = teamOwner ?: "",
        teamTerritory = teamTerritory ?: ""
)