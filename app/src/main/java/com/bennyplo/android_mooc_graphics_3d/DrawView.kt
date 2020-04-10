package com.bennyplo.android_mooc_graphics_3d

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class DrawView(context: Context?) : View(context, null) {
    private val sourceSet: List<DrawableObject> = listOf(cube(), entrance(), circle())

    var setup: (DrawView.() -> Unit)? = null

    init {
        invalidate() //update the view
    }


    override fun onDraw(canvas: Canvas) { //draw objects on the screen
        resetAll()
        scale(100.0)

        setup?.invoke(this)

        drawAll(canvas)
        super.onDraw(canvas)
        invalidate()
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

    fun resetAll() {
        sourceSet.forEach {
            it.restore()
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

    companion object {
        fun getIdentityMatrix(): Array<DoubleArray> { //return an 4x4 identity matrix
            val matrix = Array(4) { DoubleArray(4) }

            for (i in matrix.indices) {
                for (g in matrix[i].indices) {
                    if (i == g) {
                        matrix[i][g] = 1.0;
                    } else {
                        matrix[i][g] = 0.0;
                    }
                }
            }
            return matrix
        }
    }
}