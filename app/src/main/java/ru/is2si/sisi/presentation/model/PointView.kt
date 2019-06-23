package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PointView(
        val name: Int,
        val value: Int
) : Parcelable