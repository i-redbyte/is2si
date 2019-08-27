package ru.is2si.sisi.domain.result

import org.threeten.bp.LocalDateTime
import ru.is2si.sisi.domain.common.Competition
import ru.is2si.sisi.domain.common.Disciplina
import ru.is2si.sisi.domain.common.Team

sealed class CompetitionResultExpr

class CompetitionResult(
        val id: Int,
        val bally: String,
        val disciplina: Disciplina?,
        val group: String,
        val competition: Competition?,
        val team: Team?,
        val isRemovalEntry: Boolean,
        val klassDistancii: String,
        val penaltyBally: String,
        val pinkod: String,
        val placeEntry: Int,
        val removalEntry: Int,
        val resultBally: String,
        val dataTimeFinish: LocalDateTime?
) : CompetitionResultExpr()

object EmptyCompetitionResult : CompetitionResultExpr()

