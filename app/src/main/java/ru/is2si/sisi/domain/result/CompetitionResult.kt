package ru.is2si.sisi.domain.result

class CompetitionResult(
        val id: Int,
        val bally: String,
        val disciplina: Disciplina?,
        val group: String,
        val idCompetition: IdCompetition?,
        val idTeam: IdTeam?,
        val isRemovalEntry: Boolean,
        val klassDistancii: String,
        val penaltyBally: String,
        val pinkod: String,
        val placeEntry: Int,
        val removalEntry: Int,
        val resultBally: String
)