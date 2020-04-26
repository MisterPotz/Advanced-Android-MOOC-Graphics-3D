package com.bennyplo.android_mooc_graphics_3d

import android.content.Context
import android.graphics.Canvas
import android.view.View

class DrawView(context: Context?) : View(context, null) {
    private val sourceSet: List<DrawableObject> = listOf(cube())

    var setup: (DrawView.() -> Unit)? = null

    init {
        invalidate() //update the view
    }


    override fun onDraw(canvas: Canvas) { //draw objects on the screen
        resetAll()
        // translating lower so object will be captured in frustum
        rotateAxis(65.0, Coordinate(1.0,0.0,0.0,1.0))
        translate(0.0,0.0, -2.0)
        project(1.0,-1.0,-1.0, 1.0, 1.0, 1.1)

        scale(100.0, 100.0, 100.0)
//        rotateAxis(30.0, Coordinate(1.0,1.0,0.0,0.0))

        placeInCenter()

        drawAll(canvas)
        super.onDraw(canvas)
    }


    fun drawAll(canvas: Canvas) {
        sourceSet.forEach {
            it.drawOnCanvas(canvas)
        }
    }

    fun shear(hx: Double, hy: Double) {
        sourceSet.forEach {
            it.shearGlobal(hx, hy)
        }
    }

    fun resetAll() {
        sourceSet.forEach {
            it.localToGlobal()
        }
    }

    fun rotateAxis(theta: Double, axis: Coordinate) {
        sourceSet.forEach {
            it.rotateAxisGlobal(theta, axis)
        }
    }

    fun scale(times: Double, dy : Double, dz: Double) {
        sourceSet.forEach {
            it.scaleGlobal(times, dy,dz)
        }
    }

    fun project(left: Double, right: Double, top: Double, bottom: Double, near: Double, far: Double) {
        sourceSet.forEach {
            it.projectGlobal(left, right, top, bottom, near, far)
        }
    }

    fun project(near: Double, far: Double, fov: Double) {
        sourceSet.forEach {
            it.projectGlobal(near, far, fov)
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
            it.translateGlobal(dx, dy, dz)
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