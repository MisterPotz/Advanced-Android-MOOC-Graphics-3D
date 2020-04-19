package com.bennyplo.android_mooc_graphics_3d.robot

import android.content.Context
import android.graphics.Canvas
import android.view.View
import com.bennyplo.android_mooc_graphics_3d.*

class TestingHalfLinksView(context: Context?) : View(context, null) {
    private val sourceSet: List<DrawableObject> = listOf(twoConnectedeCubes())

    var setup: (TestingHalfLinksView.() -> Unit)? = null

    init {
        invalidate() //update the view
    }


    override fun onDraw(canvas: Canvas) { //draw objects on the screen
        resetAll()
        scale(100.0)

        setup?.invoke(this)

        drawAll(canvas)
        super.onDraw(canvas)
    }

    fun resetAll() {
        sourceSet.forEach {
            it.restore()
        }
    }

    fun drawAll(canvas: Canvas) {
        sourceSet.forEach {
            it.drawOnCanvas(canvas)
        }
    }

    fun shear(hx: Double, hy: Double) {
        sourceSet.forEach {
            it.shear(hx, hy)
        }
    }

    fun rotateX(xTheta: Double) {
        sourceSet.forEach {
            it.rotateX(xTheta)
        }
    }

    fun rotateY(yTheta: Double) {
        sourceSet.forEach {
            it.rotateY(yTheta)
        }
    }

    fun rotateZ(zTheta: Double) {
        sourceSet.forEach {
            it.rotateZ(zTheta)
        }
    }

    fun rotateAxis(theta: Double, axis: Coordinate) {
        sourceSet.forEach {
            it.rotateAxis(theta, axis)
        }
    }

    fun scale(times: Double) {
        sourceSet.forEach {
            it.scale(times)
        }
    }

    fun DrawableObject.placeInCenter() {
        val height = height
        val width = width
        val ycenter = height / 2.0
        val xcenter = width / 2.0
        // draw_cube_vertices = scale(draw_cube_vertices, 100.0, 100.0, 100.0)
        translate(xcenter, ycenter, 2.0)
    }

    fun placeInCenter() {
        sourceSet.forEach {
            it.placeInCenter()
        }
    }

    fun translate(dx: Double, dy: Double, dz: Double) {
        sourceSet.forEach {
            it.translate(dx, dy, dz)
        }
    }
}