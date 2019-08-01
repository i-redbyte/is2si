package ru.is2si.sisi.domain.result

class IdCompetition(
        val dataBegin: String,
        val dataEnd: String,
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
)