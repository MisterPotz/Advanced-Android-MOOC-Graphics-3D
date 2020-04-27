package com.bennyplo.android_mooc_graphics_3d.robot

import com.bennyplo.android_mooc_graphics_3d.ActionExecutor
import com.bennyplo.android_mooc_graphics_3d.MediatorActionExecutor
import kotlinx.coroutines.*

/**
 * Looks in our direction. - x > 0 - is left side
 * x < 0 - is right side
 */
class Robot {
    val head = Head()
    val neck = Neck()
    val body = Body()
    val lowerBody = LowerBody()


    val standardDelay = 16L

    private val leftLegExecutor = MediatorActionExecutor(standardDelay)
    private val rightLegExecutor = MediatorActionExecutor(standardDelay)
    private val leftArmExecutor = MediatorActionExecutor(standardDelay)
    private val rightArmExecutor = MediatorActionExecutor(standardDelay)

    init {
        body.halfLink(body.neckLink).connectTo(neck.halfLink(neck.bodyLink))
        neck.halfLink(neck.headLink).connectTo(head.halfLink(head.headLink))
        body.halfLink(body.lowerBodyLink).connectTo(lowerBody.halfLink(lowerBody.bodyLink))
    }

    fun getMain(): CubeLike {
        return body
    }

    // TODO нужно сделать что-то типа post, чтобы собирать все изменения и постить

    fun rotateLeftLeg(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        leftLegExecutor.postAction(time) {
            lowerBody.rotateLeg(false, degrees, axis)
        }
    }

    fun rotateLeftLegSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        leftLegExecutor.postAction(time) {
            lowerBody.rotateLegSilently(false, degrees, axis)
        }
    }


    fun rotateRightLeg(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        rightLegExecutor.postAction(time) {
            lowerBody.rotateLeg(true, degrees, axis)
        }
    }

    fun rotateRightLegSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        rightLegExecutor.postAction(time) {
            lowerBody.rotateLegSilently(true, degrees, axis)
        }
    }

    fun rotateLeftArm(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        leftArmExecutor.postAction(time) {
            body.rotateArm(degrees, axis, false)
        }
    }

    fun rotateLeftArmSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        leftArmExecutor.postAction(time) {
            body.rotateArmSilently(degrees, axis, false)
        }
    }

    fun rotateLowArmLeft(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        leftArmExecutor.postAction(time) {
            body.leftArm.rotateLower(degrees, axis)
        }
    }

    fun rotateLowArmRight(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        rightArmExecutor.postAction(time) {
            body.rightArm.rotateLower(degrees, axis)
        }
    }

    fun rotateLowLegLeft(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        leftLegExecutor.postAction(time) {
            lowerBody.leftLeg.rotateLower(degrees, axis)
        }
    }

    fun rotateLowLegRight(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        rightArmExecutor.postAction(time) {
            lowerBody.rightLeg.rotateLower(degrees, axis)
        }
    }

    fun rotateRightArmSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = calculateDegreePerStep(degree, time)
        rightArmExecutor.postAction(time) {
            body.rotateArmSilently(degrees, axis, true)
        }
    }

    fun executeIfActive() {
        rightLegExecutor.execute()
        leftLegExecutor.execute()
        leftArmExecutor.execute()
        rightArmExecutor.execute()
    }

    fun calculateDegreePerStep(degree: Double, time: Long): Double {
        val wakeUps = time / standardDelay
        return degree / wakeUps
    }
}