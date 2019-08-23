package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.domain.result.CompetitionResult

@Parcelize
class CompetitionResultView(
        val id: Int,
        val bally: String,
        val disciplina: DisciplinaView?,
        val group: String,
        val competition: CompetitionView?,
        val team: TeamView?,
        val isRemovalEntry: Boolean,
        val klassDistancii: String,
        val penaltyBally: String,
        val pinkod: String,
        val placeEntry: Int,
        val removalEntry: Int,
        val resultBally: String
) : Parcelable

fun CompetitionResult.asView() = CompetitionResultView(
        id = id,
        bally = bally,
        disciplina = disciplina?.asView(),
        group = group ?: "",
        competition = competition?.asView(),
        team = team?.asView(),
        isRemovalEntry = isRemovalEntry,
        klassDistancii = klassDistancii ?: "",
        penaltyBally = penaltyBally,
        pinkod = pinkod,
        placeEntry = placeEntry,
        removalEntry = removalEntry,
        resultBally = resultBally
)