package com.bennyplo.android_mooc_graphics_3d.robot

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

    val leftLeg = Leg(false)
    val rightLeg = Leg(true)

    init {
        body.halfLink(body.neckLink).connectTo(neck.halfLink(neck.bodyLink))
        neck.halfLink(neck.headLink).connectTo(head.halfLink(head.headLink))
        body.halfLink(body.lowerBodyLink).connectTo(lowerBody.halfLink(lowerBody.bodyLink))

        body.halfLink(body.rightArmLink).connectTo(rightArm.halfLink(rightArm.bodyLink))
        body.halfLink(body.leftArmLink).connectTo(leftArm.halfLink(leftArm.bodyLink))

        lowerBody.halfLink(lowerBody.leftLegLink).connectTo(leftLeg.halfLink(leftLeg.lowerBodyLink))
        lowerBody.halfLink(lowerBody.rightLegLink).connectTo(rightLeg.halfLink(rightLeg.lowerBodyLink))
    }

    fun getMain(): CubeLike {
        return body
    }
}