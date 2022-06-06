package com.neobrahma.customview.views.helper

import android.graphics.PointF
import kotlin.math.*

fun convertValueToDegree(value: Float) = value * Math.PI / 180
fun convertValueToDegree(value: Int) = value * Math.PI / 180
fun convertRadianToDegree(value: Double) = value * 180 / Math.PI

/**
 * angle : value in degree
 */
fun computePositionOnCircle(angle: Double, pointStart: PointF, pointCenter: PointF): PointF {
    val vectorX = (pointStart.x - pointCenter.x)
    val vectorY = (pointStart.y - pointCenter.y)

    val x: Double = vectorX * cos(angle) + vectorY * sin(angle) + pointCenter.x
    val y: Double = -vectorX * sin(angle) + vectorY * cos(angle) + pointCenter.y

    return PointF(round(x).toFloat(), round(y).toFloat())
}

fun computeAngleAOB(pointCenter: PointF, pointA: PointF, pointB: PointF): Double {
    val distanceAO = computeDistanceSegment(pointA, pointCenter)
    val distanceBO = computeDistanceSegment(pointB, pointCenter)
    val angle =
        (((pointA.x - pointCenter.x) * (pointB.x - pointCenter.x)) + ((pointA.y - pointCenter.y) * (pointB.y - pointCenter.y))) / (distanceAO * distanceBO)
    return convertRadianToDegree(acos(angle.toDouble()))
}

fun computePositionPointInSegment(p1: PointF, p2: PointF, ratio: Float): PointF {
    val x = p1.x + (ratio * (p2.x - p1.x))
    val y = p1.y + (ratio * (p2.y - p1.y))
    return PointF(x, y)
}

fun computeMiddleSegment(p1: PointF, p2: PointF): PointF {
    val x = (p1.x + p2.x) / 2
    val y = (p1.y + p2.y) / 2
    return PointF(x, y)
}

fun computeDistanceSegment(p1: PointF, p2: PointF) =
    sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))

/**
 * angle : value in degree
 */
fun computeAdjacent(angle: Double, Hypotenuse: Float) = (Hypotenuse * cos(angle.toFloat()))