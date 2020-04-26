package com.bennyplo.android_mooc_graphics_3d.robot

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.Coordinate
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

    val lowerBodyLink = 35
//    val leftLegLink = 40
//    val rightLegLink = 41

    init {
        addHalfLink(neckLink, Coordinate(0.0, 0.8, 0.0, 1.0))
        addHalfLink(leftArmLink, Coordinate(0.5, 0.6, 0.0, 1.0))
        addHalfLink(rightArmLink, Coordinate(-0.5, 0.6, 0.0, 1.0))

        addHalfLink(lowerBodyLink, Coordinate(0.0, -0.8, 0.0, 1.0))
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
    val rightLeg = Leg(true)

    init {
        addHalfLink(bodyLink, Coordinate(0.0, 0.18, 0.0, 1.0))
        addHalfLink(leftLegLink, Coordinate(0.25, -0.18, 0.0, 1.0))
        addHalfLink(rightLegLink, Coordinate(-0.25, -0.18, 0.0, 1.0))

        halfLink(leftLegLink).addRotationAxis(leftLeg.xAxis, Coordinate(1.0,0.0,0.0,1.0))
        halfLink(rightLegLink).addRotationAxis(rightLeg.xAxis, Coordinate(1.0,0.0,0.0,1.0))

        halfLink(leftLegLink).connectTo(leftLeg.halfLink(leftLeg.lowerBodyLink))
        halfLink(rightLegLink).connectTo(rightLeg.halfLink(rightLeg.lowerBodyLink))
    }

    /**
     * angle in degrees
     */
    fun raiseOnlyHigh(isRight: Boolean, degrees: Double) {
        val legToRaise = if (isRight) rightLeg else leftLeg
        val legLink = if (isRight) rightLegLink else leftLegLink
        halfLink(legLink).rotateOtherSilentlyModel(legToRaise.xAxis, -degrees)
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

    val xAxis = 12

    init {
        addHalfLink(lowerBodyLink, Coordinate(0.0, 0.65, 0.0, 1.0))
        addHalfLink(lowerLegLink, Coordinate(0.0, -0.65, 0.0, 1.0))
        halfLink(lowerLegLink).addRotationAxis(xAxis, Coordinate(1.0, 0.0, 0.0, 1.0))
        halfLink(lowerLegLink).connectTo(lowerLeg.halfLink(lowerLeg.upperLegLink))
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

    init {
        var sign = if (rightArm) 1 else -1
        addHalfLink(bodyLink, Coordinate(0.2 * sign, 0.35, 0.0, 1.0))
        addHalfLink(lowerArmLink, Coordinate(0.0, -0.4, 0.0, 1.0))

        halfLink(lowerArmLink).connectTo(lowerArm.halfLink(lowerArm.upperArmLink))
    }

//    fun rotateLowerArm()
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

