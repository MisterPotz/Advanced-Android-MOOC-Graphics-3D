package com.bennyplo.android_mooc_graphics_3d.robot

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.View
import com.bennyplo.android_mooc_graphics_3d.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.lang.Math.abs

class AcceptingDrawView(context: Context) : View(context) {
    val robot = Robot()
    val sourceSet = mutableListOf<ConnectableObject>(robot.getMain()/*twoConnectedeCubes()*/)

    val script1 = ScriptExecutor(MediatorActionExecutor(standardDelay = robot.standardDelay))
    val script2 = ScriptExecutor(MediatorActionExecutor(standardDelay = robot.standardDelay))
    val script3 = ScriptExecutor(MediatorActionExecutor(standardDelay = robot.standardDelay))
    val script4 = ScriptExecutor(MediatorActionExecutor(standardDelay = robot.standardDelay))

    var globalSetup: (() -> Unit)? = null

    val relative1 = 0.4
    val relative2 = 1.0
    val limit = 30.0
    var global = -30.0
    var step = -0.5
    val stepLimit = 0.5

//    val key = 20
//    val key2 = 25
//    val obj = sourceSet[0]
//    val obj2 = obj.halfLink(key).getOtherParent()!!
//    val obj3 = obj2.halfLink(key2).getOtherParent()!!
//    val link1 = obj.halfLink(key)
//    val link2 = obj2.halfLink(key2)
//    var view: AcceptingDrawView? = null

    init {
        robot.getMain().scaleModel(0.1, 0.1, 0.1, TransformationInfo.empty())

        script1.registerAll {
            robot.apply {
                walking()
            }
        }

        script2.registerAll {
            robot.apply {
                raisingHands()
            }
        }

        script3.registerAll {
            robot.apply {
                raisingHandsZ()
            }
        }

        script4.registerAll {
            robot.apply {
                raisingHandsEX1()
            }
        }

        script1.startExec { rewind() }
        script2.startExec { launchOtherAfter(script3)/*launchOtherAfter(script3)*/ }

        script3.bindCallback { launchOtherAfter(script4) }

        script4.bindCallback { launchOtherAfter(script2) }

//        script3.bindCallback { script2.justStart() }
//        robot.rotateLeftLegSilently(30.0, 1000, AxesType.X)
//        robot.rotateLowLegLeft(-30.0, 1000, AxesType.X)
//        robot.rotateRightLeg(40.0, 2000, AxesType.X)
//        robot.rotateLeftArmSilently(-45.0, 5000, AxesType.X)
//        robot.rotateLowArmRight(90.0, 2000, AxesType.Y)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        /*robot.executeIfActive()*/
        script1.execIfActive()
        script2.execIfActive()
        resetAll()

        robot.getMain().rotateAxisGlobal(global, Coordinate(0.0, 1.0, 0.0, 1.0))
        global = global - step
        if (abs(global) > abs(limit)) {
            if (global > 0) {
                global = limit
                step =  stepLimit
            } else {
                global = - limit
                step = - stepLimit
            }
        }
//        robot.getMain().rotateAxisGlobal(30.0, Coordinate(1.0, 1.0, 0.0, 1.0))

        robot.getMain().translateGlobal(0.0, 0.0, -1.1)
        robot.getMain().projectToGlobal(ProjectionBesicParameters(-1.0, 1.0, 1.0, -1.0, 1.0, 2.0), TransformationInfo.empty())

        robot.getMain().scaleGlobal(1800.0, 1800.0, 1500.0)
        robot.getMain().scaleGlobal(1.0, -1.0, 1.0)
        placeInCenter()
        drawAll(canvas)
        super.onDraw(canvas)
    }

    // Reset to model level
    fun resetAll() {
        sourceSet.forEach {
            it.modelToGlobal(TransformationInfo.empty())
        }
    }

    fun DrawableObject.placeInCenter() {
        val height = height
        val width = width
        val ycenter = height / 2.0
        val xcenter = width / 2.0
        // draw_cube_vertices = scale(draw_cube_vertices, 100.0, 100.0, 100.0)
        translateGlobal(xcenter, ycenter, 2.0)
    }

    fun inverseY() {
        sourceSet.forEach {
            it.scaleGlobal(1.0, -1.0, 1.0)
        }
    }

    fun placeInCenter() {
        sourceSet.forEach {
            it.placeInCenter()
        }
    }

    fun drawAll(canvas: Canvas) {
        sourceSet.forEach {
            it.drawOnCanvas(canvas)
        }
    }
}