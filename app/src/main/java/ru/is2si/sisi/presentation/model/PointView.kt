package ru.is2si.sisi.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.is2si.sisi.domain.points.Point

@Parcelize
class PointView(
        val id: Int,
        var location: LocationView,
        val colorMax: String,
        val color1: String?,
        val color2: String?,
        val color3: String?,
        val color4: String?,
        val colorMin: String?,
        val colorNo0: String,
        val competition: Int,
        val pointBall: Int,
        val pointBall1: String,
        val pointBall2: String,
        val pointBall3: String,
        val pointBall4: String,
        val pointBall5: String,
        val pointName: Int,
        val pointNameStr: String,
        val maxRadius: Double,
        val r1: String,
        val r2: String,
        val r3: String,
        val r4: String,
        var minRadius: Double
) : Parcelable

fun Point.asView() = PointView(
        id = id,
        location = location,
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

fun PointView.asDomain() = Point(
        id = id,
        location = location,
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