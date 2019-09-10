package ru.is2si.sisi.data.result

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.base.extension.toLocalDateTimeOrNull
import ru.is2si.sisi.data.common.*
import ru.is2si.sisi.domain.result.CompetitionResult

// TODO: Red_byte 2019-08-02 Make internal class
class CompetitionResultResponse(
        @SerializedName("teamId")
        val id: Int,
        @SerializedName("Bally")
        val bally: String,
        @SerializedName("Disciplina")
        val disciplina: DisciplinaResponse?,
        @SerializedName("Group")
        val group: String?,
        @SerializedName("Competition")
        val competition: CompetitionResponse?,
        @SerializedName("Team")
        val team: TeamResponse?,
        @SerializedName("isRemovalEntry")
        val isRemovalEntry: Boolean,
        @SerializedName("KlassDistancii")
        val klassDistancii: String?,
        @SerializedName("PenaltyBally")
        val penaltyBally: String,
        @SerializedName("Pinkod")
        val pinkod: String?,
        @SerializedName("PlaceEntry")
        val placeEntry: Int,
        @SerializedName("RemovalEntry")
        val removalEntry: Int?,
        @SerializedName("ResultBally")
        val resultBally: String,
        @SerializedName("DataTimeFinish")
        val dataTimeFinish: String?
)

fun CompetitionResultResponse.toCompetitionResult() = CompetitionResult(
        id = id,
        bally = bally,
        disciplina = disciplina?.toDisciplina(),
        group = group ?: "",
        competition = competition?.toCompetition(),
        team = team?.toTeam(),
        isRemovalEntry = isRemovalEntry,
        klassDistancii = klassDistancii ?: "",
        penaltyBally = penaltyBally,
        pinkod = pinkod ?: "",
        placeEntry = placeEntry,
        removalEntry = removalEntry ?: 0,
        resultBally = resultBally,
        dataTimeFinish = dataTimeFinish.toLocalDateTimeOrNull()
)