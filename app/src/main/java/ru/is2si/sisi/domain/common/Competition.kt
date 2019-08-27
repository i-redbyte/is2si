package ru.is2si.sisi.domain.common

import org.threeten.bp.LocalDateTime

class Competition(
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
        val venue: String,
        val emergencyPhone: String,
        val shtrafBallyNorma: Double
)