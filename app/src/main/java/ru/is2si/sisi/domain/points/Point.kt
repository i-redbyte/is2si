package ru.is2si.sisi.domain.points

import ru.is2si.sisi.presentation.model.LocationView

class Point(
        val id: Int,
        val location: LocationView,
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
        val minRadius: String
)