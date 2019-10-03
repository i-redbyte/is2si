package ru.is2si.sisi.data.points

import com.google.gson.annotations.SerializedName
import ru.is2si.sisi.domain.points.Point
import ru.is2si.sisi.presentation.model.LocationView

/**
 * r0 - максимальный радиус от точки,
 * r5 - минимальный (самый близкий) радиус
 * */
class PointResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("a")
        val latitude: Double,
        @SerializedName("b")
        val longitude: Double,
        @SerializedName("color0")
        val colorMax: String,
        @SerializedName("color1")
        val color1: String?,
        @SerializedName("color2")
        val color2: String?,
        @SerializedName("color3")
        val color3: String?,
        @SerializedName("color4")
        val color4: String?,
        @SerializedName("color5")
        val colorMin: String?,
        @SerializedName("colorNo0")
        val colorNo0: String,
        @SerializedName("Competition")
        val competition: Int,
        @SerializedName("PointBall")
        val pointBall: Int,
        @SerializedName("PointBall1")
        val pointBall1: String,
        @SerializedName("PointBall2")
        val pointBall2: String,
        @SerializedName("PointBall3")
        val pointBall3: String,
        @SerializedName("PointBall4")
        val pointBall4: String,
        @SerializedName("PointBall5")
        val pointBall5: String,
        @SerializedName("PointName")
        val pointName: Int,
        @SerializedName("PointNameStr")
        val pointNameStr: String,
        @SerializedName("r0")
        val maxRadius: Double,
        @SerializedName("r1")
        val r1: String,
        @SerializedName("r2")
        val r2: String,
        @SerializedName("r3")
        val r3: String,
        @SerializedName("r4")
        val r4: String,
        @SerializedName("r5")
        val minRadius: String
)

fun PointResponse.toPoint() = Point(
        id = id,
        location = LocationView(latitude, longitude),
        colorMax = colorMax,
        colorMin = colorMin,
        maxRadius = maxRadius,
        minRadius = minRadius,
        pointBall = pointBall,
        color1 = color1,
        color2 = color2,
        color3 = color3,
        color4 = color4,
        colorNo0 = colorNo0,
        competition = competition,
        pointBall1 = pointBall1,
        pointBall2 = pointBall2,
        pointBall3 = pointBall3,
        pointBall4 = pointBall4,
        pointBall5 = pointBall5,
        pointName = pointName,
        pointNameStr = pointNameStr,
        r1 = r1,
        r2 = r2,
        r3 = r3,
        r4 = r4
)