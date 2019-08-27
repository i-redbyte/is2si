package ru.is2si.sisi.base.extension

import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

fun String?.toLocalDateTimeOrNull(): LocalDateTime? = if (this != null) OffsetDateTime.parse(this).toLocalDateTime() else null

fun String.toLocalDateTime(): LocalDateTime = OffsetDateTime.parse(this).toLocalDateTime()

fun LocalDateTime.getDateTimeOfPattern(pattern:String ="dd LLLL YYYY, HH:mm") = DateTimeFormatter.ofPattern(pattern)
        .format(this)

fun LocalDateTime.getDateOfPattern(pattern:String ="dd LLLL") = DateTimeFormatter.ofPattern(pattern)
        .format(this)

fun LocalDateTime.getTimeOfPattern(pattern:String ="HH:mm") = DateTimeFormatter.ofPattern(pattern)
        .format(this)
