package com.bennyplo.android_mooc_graphics_3d.robot

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.Coordinate
import com.bennyplo.android_mooc_graphics_3d.HalfLink
import com.bennyplo.android_mooc_graphics_3d.scale

abstract class CubeLike(
        preSet: ((Array<Coordinate?>) -> Array<Coordinate?>)? = null,
        array: Array<Coordinate?> = cubeVertices().run { if (preSet != null) preSet(this) else this },
        setupPaint: Paint.() -> Unit) : ConnectableObject(
        array, setupPaint) {
    override fun rawDraw(canvas: Canvas) {
        global.apply {
            drawLinePairs(canvas, this, 5, 7, paint)
            drawLinePairs(canvas, this, 0, 1, paint)
            drawLinePairs(canvas, this, 1, 3, paint)
            drawLinePairs(canvas, this, 3, 2, paint)
            drawLinePairs(canvas, this, 2, 0, paint)
            drawLinePairs(canvas, this, 4, 5, paint)
            drawLinePairs(canvas, this, 7, 6, paint)
            drawLinePairs(canvas, this, 6, 4, paint)
            drawLinePairs(canvas, this, 0, 4, paint)
            drawLinePairs(canvas, this, 1, 5, paint)
            drawLinePairs(canvas, this, 2, 6, paint)
            drawLinePairs(canvas, this, 3, 7, paint)
        }
    }

    companion object {
        fun cubeVertices() = arrayOf<Coordinate?>(
                Coordinate(-1.0, -1.0, -1.0, 1.0),
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, -1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, -1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, -1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )
    }
}

class Head() : CubeLike(
        setupPaint = {
            color = Color.BLUE
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.3, 0.3, 0.3)
        }) {
    val headLink = 20

    init {
        addHalfLink(headLink, Coordinate(0.0, -0.3, 0.0, 1.0))
    }
}

class Neck() : CubeLike(
        setupPaint = {
            color = Color.MAGENTA
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.2, 0.15, 0.2)
        }) {
    val headLink = 20
    val bodyLink = 25

    init {
        addHalfLink(headLink, Coordinate(0.0, 0.15, 0.0, 1.0))
        addHalfLink(bodyLink, Coordinate(0.0, -0.15, 0.0, 1.0))
    }
}

/**
 * Can rotate arms around x axis, y axis and z axis
 */
class Body() : CubeLike(
        setupPaint = {
            color = Color.RED
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.5, 0.8, 0.2)
        }) {
    val neckLink = 25
    val leftArmLink = 30
    val rightArmLink = 31

    val leftArm = Arm(rightArm = false)
    val leftArmAxes = AxisSaver(1000, 1001, 1002, 999)

    val rightArm = Arm(rightArm = true)
    val rightArmAxes = AxisSaver(1005, 1006, 100, 997)

    val lowerBodyLink = 35

    init {
        addHalfLink(neckLink, Coordinate(0.0, 0.8, 0.0, 1.0))
        addHalfLink(leftArmLink, Coordinate(0.5, 0.6, 0.0, 1.0))
        addHalfLink(rightArmLink, Coordinate(-0.5, 0.6, 0.0, 1.0))

        addHalfLink(lowerBodyLink, Coordinate(0.0, -0.8, 0.0, 1.0))

        halfLink(leftArmLink).let {
            leftArmAxes.apply {
                addXTo(it)
                addYTo(it)
                addZTo(it)
                addEx1To(it)
            }
        }

        halfLink(rightArmLink).let {
            rightArmAxes.apply {
                addXTo(it)
                addYTo(it)
                addZTo(it)
                addEx1To(it)
            }
        }


        halfLink(rightArmLink).connectTo(rightArm.halfLink(rightArm.bodyLink))
        halfLink(leftArmLink).connectTo(leftArm.halfLink(leftArm.bodyLink))
    }

    fun rotateArm(theta: Double, axis: AxesType, right: Boolean) {
        val picked = pickHalfLinkAndAxes(axis, right)
        picked.first.rotateOtherAroundAxisModel(picked.second, - theta)
    }

    fun rotateArmSilently(theta: Double, axis: AxesType, right: Boolean) {
        val picked = pickHalfLinkAndAxes(axis, right)
        picked.first.rotateOtherSilentlyModel(picked.second, -theta)
    }

    fun pickHalfLinkAndAxes(axis: AxesType, right: Boolean): Pair<HalfLink, Int> {
        val halfLink = if (right) halfLink(rightArmLink)
        else halfLink(leftArmLink)

        val axes = if (right) rightArmAxes.axis[axis]!! else leftArmAxes.axis[axis]!!
        return Pair(halfLink, axes)
    }
}

class LowerBody() : CubeLike(
        setupPaint = {
            color = Color.MAGENTA
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.5, 0.18, 0.2)
        }) {
    val bodyLink = 35
    val leftLegLink = 40
    val rightLegLink = 41

    val leftLeg = Leg(false)
    val leftLegAxis = AxisSaver(1601,1602, 1603)
    val rightLeg = Leg(true)
    val rightLegAxis = AxisSaver(1610, 1620, 1621)

    init {
        addHalfLink(bodyLink, Coordinate(0.0, 0.18, 0.0, 1.0))
        addHalfLink(leftLegLink, Coordinate(0.25, -0.18, 0.0, 1.0))
        addHalfLink(rightLegLink, Coordinate(-0.25, -0.18, 0.0, 1.0))

        halfLink(leftLegLink).let {
            leftLegAxis.apply {
                addXTo(it)
                addYTo(it)
                addZTo(it)
            }
        }

        halfLink(rightLegLink).let {
            rightLegAxis.apply {
                addXTo(it)
                addYTo(it)
                addZTo(it)
            }
        }

        halfLink(leftLegLink).connectTo(leftLeg.halfLink(leftLeg.lowerBodyLink))
        halfLink(rightLegLink).connectTo(rightLeg.halfLink(rightLeg.lowerBodyLink))
    }

    /**
     * angle in degrees
     */
    fun rotateLegSilently(isRight: Boolean, degrees: Double, axis: AxesType) {
        val picked = pickHalfLinkAndAxes(axis, isRight)
        picked.first.rotateOtherSilentlyModel(picked.second, - degrees)
    }

    fun rotateLeg(isRight: Boolean, degrees: Double, axis: AxesType) {
        val picked = pickHalfLinkAndAxes(axis, isRight)
        picked.first.rotateOtherAroundAxisModel(picked.second, - degrees)
    }

    fun pickHalfLinkAndAxes(axis: AxesType, right: Boolean): Pair<HalfLink, Int> {
        val halfLink = if (right) halfLink(rightLegLink)
        else halfLink(leftLegLink)

        val axes = if (right) rightLegAxis.axis[axis]!! else leftLegAxis.axis[axis]!!
        return Pair(halfLink, axes)
    }
}

class Leg(val rightLeg: Boolean) : CubeLike(
        setupPaint = {
            color = if (rightLeg) Color.BLUE else Color.MAGENTA
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.2, 0.65, 0.2)
        }) {
    val lowerBodyLink = 120
    val lowerLegLink = 101

    val lowerLeg = LowerLeg()
    val lowerLegAxis = AxisSaver(500, 501, 502)
    val xAxis = 12

    init {
        addHalfLink(lowerBodyLink, Coordinate(0.0, 0.65, 0.0, 1.0))
        addHalfLink(lowerLegLink, Coordinate(0.0, -0.65, 0.0, 1.0))

        halfLink(lowerLegLink).let {
            lowerLegAxis.apply {
                addXTo(it)
                addYTo(it)
                addZTo(it)
            }
        }
        halfLink(lowerLegLink).addRotationAxis(xAxis, Coordinate(1.0, 0.0, 0.0, 1.0))
        halfLink(lowerLegLink).connectTo(lowerLeg.halfLink(lowerLeg.upperLegLink))
    }

    fun rotateLower(theta: Double, axis: AxesType) {
        halfLink(lowerLegLink).rotateOtherAroundAxisModel(lowerLegAxis.axis[axis]!!, - theta)
    }

    fun rotateLowerSilently(theta: Double, axis: AxesType) {
        halfLink(lowerLegLink).rotateOtherSilentlyModel(lowerLegAxis.axis[axis]!!, - theta)
    }
}


class LowerLeg() : CubeLike(
        setupPaint = {
            color = Color.BLUE
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.2, 0.7, 0.2)
        }) {
    val upperLegLink = 120
    val footLink = 125

    val foot = Foot()

    init {
        addHalfLink(upperLegLink, Coordinate(0.0, 0.7, 0.0, 1.0))
        addHalfLink(footLink, Coordinate(0.0, -0.7, 0.0, 1.0))

        halfLink(footLink).connectTo(foot.halfLink(foot.lowerLeg))
    }
}

class Foot() : CubeLike(
        setupPaint = {
            color = Color.BLUE
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.2, 0.1, 0.3)
        }) {
    val lowerLeg = 125

    init {
        addHalfLink(lowerLeg, Coordinate(0.0, 0.1, -0.1, 1.0))
    }
}

class Arm(val rightArm: Boolean) : CubeLike(
        setupPaint = {
            color = if (rightArm) Color.BLUE else Color.MAGENTA
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.2, 0.4, 0.2)
        }) {
    val bodyLink = 100
    val lowerArmLink = 101

    val lowerArm = LowerArm()
    val lowerArmAxes = AxisSaver(1500, 1501, 1502)

    init {
        var sign = if (rightArm) 1 else -1
        addHalfLink(bodyLink, Coordinate(0.2 * sign, 0.35, 0.0, 1.0))
        addHalfLink(lowerArmLink, Coordinate(0.0, -0.4, 0.0, 1.0))

        halfLink(lowerArmLink).let {
            lowerArmAxes.apply {
                addXTo(it)
                addYTo(it)
                addZTo(it)
            }
        }

        halfLink(lowerArmLink).connectTo(lowerArm.halfLink(lowerArm.upperArmLink))
    }

    fun rotateLower(theta: Double, axis: AxesType) {
        halfLink(lowerArmLink).rotateOtherAroundAxisModel(lowerArmAxes.axis[axis]!!, - theta)
    }

    fun rotateLowerSilently(theta: Double, axis: AxesType) {
        halfLink(lowerArmLink).rotateOtherSilentlyModel(lowerArmAxes.axis[axis]!!, - theta)
    }
}

class LowerArm : CubeLike(
        setupPaint = {
            color = Color.CYAN
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.2, 0.6, 0.2)
        }) {
    val upperArmLink = 101
    val handLink = 102

    val hand = Hand()

    init {
        addHalfLink(upperArmLink, Coordinate(0.0, 0.6, 0.0, 1.0))
        addHalfLink(handLink, Coordinate(0.0, -0.6, 0.0, 1.0))

        halfLink(handLink).connectTo(hand.halfLink(hand.lowerArm))
    }
}

class Hand : CubeLike(
        setupPaint = {
            color = Color.RED
            strokeWidth = 4f
            style = Paint.Style.STROKE
        },
        preSet = {
            scale(it, 0.22, 0.1, 0.2)
        }) {
    val lowerArm = 102

    init {
        addHalfLink(lowerArm, Coordinate(0.0, 0.1, -0.1, 1.0))
    }
}

