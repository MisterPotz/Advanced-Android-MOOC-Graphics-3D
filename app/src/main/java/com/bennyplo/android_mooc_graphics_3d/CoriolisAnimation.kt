package com.bennyplo.android_mooc_graphics_3d

import android.app.Activity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun launchCoriolis(drawView: DrawView, activity: Activity) {
    var angleX : Double = 0.0
    var angleY : Double = 25.0
    var angleZ : Double = 0.0
    GlobalScope.launch {
        delay(1000)
        while(true) {
            println("In loop, angleX: $angleX")
            println("In loop, angleY: $angleY")
            println("In loop, angleZ: $angleZ")

            activity.runOnUiThread {
                drawView.setup = {
//                    rotateX(angleX)
//                    rotateY(angleY)
//                    rotateZ(angleZ)
                }
            }
            angleX += 0.2
            angleY += 0.04
            angleZ += 0.005
            if (angleX >= 360) angleX = 0.0;
            if (angleY >= 360) angleY = 0.0;
            if (angleZ >= 360) angleY = 0.0;

            delay(16)
        }
    }
}