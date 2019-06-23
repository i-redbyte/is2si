package ru.is2si.sisi.data.network

import com.google.gson.annotations.SerializedName

internal class ErrorResponse(
    @SerializedName("no_code")
    val message: String?
)

internal typealias Error = ErrorResponse?