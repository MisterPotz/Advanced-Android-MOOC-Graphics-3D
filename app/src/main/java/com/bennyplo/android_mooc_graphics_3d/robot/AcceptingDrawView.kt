package com.bennyplo.android_mooc_graphics_3d.robot

import android.content.Context
import android.graphics.Canvas
import android.view.View
import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.DrawableObject
import com.bennyplo.android_mooc_graphics_3d.TransformationInfo

class AcceptingDrawView(context: Context) : View(context) {
    val sourceSet = mutableListOf<ConnectableObject>()

    var globalSetup : (() -> Unit)? = null


    override fun onDraw(canvas: Canvas) {
        resetAll()

        globalSetup?.invoke()

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