package com.bennyplo.android_mooc_graphics_3d.robot

import com.bennyplo.android_mooc_graphics_3d.ActionExecutor
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
    val leftArm = Arm(rightArm = false)
    val rightArm = Arm(rightArm = true)

    val standardDelay = 16L

    private val leftLegRaiserExecutor = ActionExecutor(standardDelay)

    init {
        body.halfLink(body.neckLink).connectTo(neck.halfLink(neck.bodyLink))
        neck.halfLink(neck.headLink).connectTo(head.halfLink(head.headLink))
        body.halfLink(body.lowerBodyLink).connectTo(lowerBody.halfLink(lowerBody.bodyLink))

        body.halfLink(body.rightArmLink).connectTo(rightArm.halfLink(rightArm.bodyLink))
        body.halfLink(body.leftArmLink).connectTo(leftArm.halfLink(leftArm.bodyLink))
    }

    fun getMain(): CubeLike {
        return body
    }

    // TODO нужно сделать что-то типа post, чтобы собирать все изменения и постить

    fun rotateLeftLeg(degree : Double, time: Long) {
        val wakeUps = time / standardDelay
        val degreeAtStep = degree / wakeUps
        leftLegRaiserExecutor.startExecuting(time) {
            lowerBody.raiseOnlyHigh(false, degreeAtStep)
        }
    }

    fun executeIfActive() {
        leftLegRaiserExecutor.executeIfActive()
    }
}