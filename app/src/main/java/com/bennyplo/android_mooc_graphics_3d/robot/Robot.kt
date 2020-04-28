package com.bennyplo.android_mooc_graphics_3d.robot

import android.drm.DrmRights
import android.util.Log
import com.bennyplo.android_mooc_graphics_3d.ActionExecutor
import com.bennyplo.android_mooc_graphics_3d.MediatorActionExecutor
import com.bennyplo.android_mooc_graphics_3d.ScriptExecutor
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


    val standardDelay = 30L

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
        val degrees = degreePerStep(degree, time)
        leftLegExecutor.postAction(time) {
            lowerBody.rotateLeg(false, degrees, axis)
        }
    }

    fun rotateLeftLegSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        leftLegExecutor.postAction(time) {
            lowerBody.rotateLegSilently(false, degrees, axis)
        }
    }

    fun rotateRightLeg(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        rightLegExecutor.postAction(time) {
            lowerBody.rotateLeg(true, degrees, axis)
        }
    }

    fun rotateRightLegSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        rightLegExecutor.postAction(time) {
            lowerBody.rotateLegSilently(true, degrees, axis)
        }
    }

    fun rotateLeftArm(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        leftArmExecutor.postAction(time) {
            body.rotateArm(degrees, axis, false)
        }
    }

    fun rotateLeftArmSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        leftArmExecutor.postAction(time) {
            body.rotateArmSilently(degrees, axis, false)
        }
    }

    fun rotateLowArmLeft(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        leftArmExecutor.postAction(time) {
            body.leftArm.rotateLower(degrees, axis)
        }
    }

    fun rotateLowArmRight(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        rightArmExecutor.postAction(time) {
            body.rightArm.rotateLower(degrees, axis)
        }
    }

    fun rotateLowLegLeft(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        leftLegExecutor.postAction(time) {
            lowerBody.leftLeg.rotateLower(degrees, axis)
        }
    }

    fun rotateLowLegRight(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
        rightArmExecutor.postAction(time) {
            lowerBody.rightLeg.rotateLower(degrees, axis)
        }
    }

    fun rotateRightArmSilently(degree: Double, time: Long, axis: AxesType) {
        val degrees = degreePerStep(degree, time)
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

    private fun ScriptExecutor.step(right: Boolean) {
        val stepForward = 210L
        val stepBack = 210L
        val degree = 70.0
        register(stepForward) {
            val currDegree = degreePerStep(degree, stepForward)
            lowerBody.rotateLegSilently(right, currDegree, AxesType.X)
        }
        register(stepBack) {
            val currDegree = degreePerStep(-degree, stepBack)
            lowerBody.rotateLegSilently(right, currDegree, AxesType.X)
        }
    }

    fun ScriptExecutor.walking() {
        step(false)
        step(true)
    }

    fun ScriptExecutor.raiseHandAndLower(right: Boolean) {
        val raise = 210L
        val down = 210L
        val degreeUpper = 40.0
        val degreeLower = 110.0
        val arm : Arm = if (right) body.rightArm else body.leftArm

        register(raise) {
            val currDegree1 = degreePerStep(degreeUpper, raise)
            body.rotateArmSilently(currDegree1, AxesType.X, right)
            val currDegree2 = degreePerStep(degreeLower, raise)
            arm.rotateLower(currDegree2, AxesType.X)
        }

        register(down) {
            val currDegree1 = degreePerStep(-degreeUpper, down)
            body.rotateArmSilently(currDegree1, AxesType.X, right)
            val currDegree2 = degreePerStep(-degreeLower, down)
            arm.rotateLower(currDegree2, AxesType.X)
        }
    }

    fun ScriptExecutor.raiseHandAndLowerZ(right: Boolean) {
        val raise = 210L
        val down = 210L
        val degreeUpper = 40.0
        val degreeLower = 90.0
        val arm : Arm = if (right) body.rightArm else body.leftArm
        val sign = if (right) 1 else -1

        register(raise) {
            val currDegree1 = degreePerStep(degreeUpper, raise)
            Log.i("Raising hands", "degree: $currDegree1")
            body.rotateArmSilently(sign * currDegree1, AxesType.Z, right)
            val currDegree2 = degreePerStep(degreeLower, raise)
            arm.rotateLower(sign * currDegree2, AxesType.Z)
        }

        register(down) {
            val currDegree1 = degreePerStep(degreeUpper, down)
            Log.i("Lowering hands", "degree: $currDegree1")

            body.rotateArmSilently(-sign * currDegree1, AxesType.Z, right)
            val currDegree2 = degreePerStep(degreeLower, down)
            arm.rotateLower(-sign * currDegree2, AxesType.Z)
        }
    }

    fun ScriptExecutor.raiseHandAndLowerEX1(toLeft: Boolean) {
        val raise = 210L
        val down = 210L
        val degreeUpper = 40.0
        val degreeLower = 90.0
        val leftArm = body.leftArm
        val rightArm = body.rightArm
//        val arm : Arm = if (right) body.rightArm else body.leftArm
//        val sign = if (right) 1 else -1
        val sign = if (toLeft) 1 else -1

        register(raise) {
            val currDegree1 = degreePerStep(degreeUpper, raise) * sign
            Log.i("Raising hands", "degree: $currDegree1")
            body.rotateArm(  currDegree1 , AxesType.EX1, true)

            Log.i("Raising hands", "degree: $currDegree1")
            body.rotateArm( currDegree1  , AxesType.EX1, false)

            val currDegree2 = degreePerStep(degreeLower, raise) * sign
            rightArm.rotateLower(  currDegree2 ,  AxesType.Z)
            leftArm.rotateLower(  currDegree2 , AxesType.Z)

        }

        register(down) {

            val currDegree1 = degreePerStep(degreeUpper, down) * sign
            Log.i("Raising hands", "degree: $currDegree1")
            body.rotateArm( - currDegree1 , AxesType.EX1, true)

            Log.i("Raising hands", "degree: $currDegree1")
            body.rotateArm( - currDegree1 , AxesType.EX1, false)

            val currDegree2 = degreePerStep(degreeLower, down) * sign
            rightArm.rotateLower ( -currDegree2 , AxesType.Z)
            leftArm.rotateLower(  -currDegree2 , AxesType.Z)
        }
    }

    fun ScriptExecutor.raisingHands() {
        raiseHandAndLower(true)
        raiseHandAndLower(false)
    }

    fun ScriptExecutor.raisingHandsZ() {
        raiseHandAndLowerZ(true)
        raiseHandAndLowerZ(false)
    }

    fun ScriptExecutor.raisingHandsEX1() {
        raiseHandAndLowerEX1(false)
        raiseHandAndLowerEX1(true)
    }

    fun degreePerStep(degree: Double, time: Long): Double {
        val wakeUps = time / standardDelay
        return degree / wakeUps
    }

    fun calcCurrent(currentStep: Long, degree: Double, time: Long): Double {
        val degreePerStep = degreePerStep(degree, time)
        return currentStep * degreePerStep
    }
}