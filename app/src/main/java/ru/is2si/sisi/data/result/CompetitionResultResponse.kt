package ru.is2si.sisi.data.result

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.domain.result.CompetitionResult

// TODO: Red_byte 2019-08-02 Make internal class
class CompetitionResultResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("Bally")
        val bally: String,
        @SerializedName("DisciplinaResponse")
        val disciplina: DisciplinaResponse?,
        @SerializedName("Group")
        val group: String?,
        @SerializedName("IdCompetitionResponse")
        val idCompetition: IdCompetitionResponse,
        @SerializedName("IdTeamResponse")
        val idTeam: IdTeamResponse?,
        @SerializedName("isRemovalEntry")
        val isRemovalEntry: Boolean,
        @SerializedName("KlassDistancii")
        val klassDistancii: String?,
        @SerializedName("PenaltyBally")
        val penaltyBally: String,
        @SerializedName("Pinkod")
        val pinkod: String,
        @SerializedName("PlaceEntry")
        val placeEntry: Int,
        @SerializedName("RemovalEntry")
        val removalEntry: Int,
        @SerializedName("ResultBally")
        val resultBally: String
)

fun CompetitionResultResponse.toCompetitionResult() = CompetitionResult(
        id = id,
        bally = bally,
        disciplina = disciplina?.toDisciplina(),
        group = group ?: "",
        idCompetition = idCompetition.toIdCompetition(),
        idTeam = idTeam?.toIdTeam(),
        isRemovalEntry = isRemovalEntry,
        klassDistancii = klassDistancii ?: "",
        penaltyBally = penaltyBally,
        pinkod = pinkod,
        placeEntry = placeEntry,
        removalEntry = removalEntry,
        resultBally = resultBally
)