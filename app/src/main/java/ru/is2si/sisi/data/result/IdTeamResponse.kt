package ru.is2si.sisi.data.result

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.domain.result.IdTeam

class IdTeamResponse(
        @SerializedName("id")
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

fun IdTeamResponse.toIdTeam() = IdTeam(
        id = id,
        teamIsActive = teamIsActive,
        teamName = teamName,
        teamOwner = teamOwner ?: "",
        teamTerritory = teamTerritory ?: ""
)