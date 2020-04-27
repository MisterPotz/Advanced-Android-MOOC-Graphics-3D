package com.bennyplo.android_mooc_graphics_3d.robot

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.View
import com.bennyplo.android_mooc_graphics_3d.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class AcceptingDrawView(context: Context) : View(context) {
    val robot = Robot()
    val sourceSet = mutableListOf<ConnectableObject>(robot.getMain()/*twoConnectedeCubes()*/)

    var globalSetup: (() -> Unit)? = null

    val relative1 = 0.4
    val relative2 = 1.0
    var global = 30.0
    val ascendGlobal = 0.4

//    val key = 20
//    val key2 = 25
//    val obj = sourceSet[0]
//    val obj2 = obj.halfLink(key).getOtherParent()!!
//    val obj3 = obj2.halfLink(key2).getOtherParent()!!
//    val link1 = obj.halfLink(key)
//    val link2 = obj2.halfLink(key2)
//    var view: AcceptingDrawView? = null

    init {
        robot.getMain().scaleModel(0.1,0.1,0.1, TransformationInfo.empty())

        robot.rotateLeftLegSilently( 30.0, 1000, AxesType.X)
        robot.rotateLowLegLeft(-30.0, 1000, AxesType.X)
        robot.rotateRightLeg(40.0, 2000, AxesType.X)
        robot.rotateLeftArmSilently(-45.0, 5000, AxesType.X)
        robot.rotateLowArmRight(90.0, 2000, AxesType.Y)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        robot.executeIfActive()
        resetAll()

        robot.getMain().rotateAxisGlobal(30.0, Coordinate(1.0,1.0,0.0,1.0))

        robot.getMain().translateGlobal(0.0,0.0, -1.2)
        robot.getMain().projectToGlobal(ProjectionBesicParameters(-1.0, 1.0, 1.0, -1.0, 1.0, 2.0), TransformationInfo.empty())

        robot.getMain().scaleGlobal(1500.0, 1500.0, 1500.0)
        robot.getMain().scaleGlobal(1.0,-1.0,1.0)
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