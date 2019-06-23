package ru.is2si.sisi.di.data

import com.google.gson.annotations.SerializedName

class UserResponse(
        @SerializedName("name")
        val name: String
)