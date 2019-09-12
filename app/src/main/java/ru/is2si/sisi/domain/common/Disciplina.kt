package ru.is2si.sisi.domain.common

class Disciplina(
        val codeDiscipline: String,
        val id: Int,
        val idVidSporta: Int,
        val name: String,
        val discipline: Discipline
)

class Discipline(
        val name: String
)