package com.bennyplo.android_mooc_graphics_3d

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import kotlin.math.cos
import kotlin.math.sin

fun cube(): DrawableObject {
    val cube_vertices //the vertices of a 3D cube
            : Array<Coordinate?> = run {
        val arr = Array<Coordinate?>(size = 8) { null }
        arr[0] = Coordinate(-1.0, -1.0, -1.0, 1.0)
        arr[1] = Coordinate(-1.0, -1.0, 1.0, 1.0)
        arr[2] = Coordinate(-1.0, 1.0, -1.0, 1.0)
        arr[3] = Coordinate(-1.0, 1.0, 1.0, 1.0)
        arr[4] = Coordinate(1.0, -1.0, -1.0, 1.0)
        arr[5] = Coordinate(1.0, -1.0, 1.0, 1.0)
        arr[6] = Coordinate(1.0, 1.0, -1.0, 1.0)
        arr[7] = Coordinate(1.0, 1.0, 1.0, 1.0)
        arr
    }

    return object : DrawableObject(cube_vertices) {
        override fun drawOnCanvas(canvas: Canvas) {
            global.apply {
                drawLinePairs(canvas, this, 5, 7, paint)
                drawLinePairs(canvas, this, 0, 1, paint)
                drawLinePairs(canvas, this, 1, 3, paint)
                drawLinePairs(canvas, this, 3, 2, paint)
                drawLinePairs(canvas, this, 2, 0, paint)
                drawLinePairs(canvas, this, 4, 5, paint)
                drawLinePairs(canvas, this, 7, 6, paint)
                drawLinePairs(canvas, this, 6, 4, paint)
                drawLinePairs(canvas, this, 0, 4, paint)
                drawLinePairs(canvas, this, 1, 5, paint)
                drawLinePairs(canvas, this, 2, 6, paint)
                drawLinePairs(canvas, this, 3, 7, paint)
            }
        }
    }
}

fun circle(): DrawableObject {
    val circleVertices: Array<Coordinate?> = Array(100) {
        Coordinate(0.6 * cos(Math.toRadians(360 / 100.0 * it)), 0.6 * sin(Math.toRadians(360 / 100.0 * it)), 1.0, 1.0)
    }

    return object : DrawableObject(circleVertices) {
        override fun drawOnCanvas(canvas: Canvas) {
            for (i in 0 until global.size - 1) {
                drawLinePairs(canvas, global, i, i + 1, paint)
            }
            drawLinePairs(canvas, global, 0, global.lastIndex, paint)
        }
    }
}

/**
 * находится на
 * arr[0] = Coordinate(0.95, -0.4, -0.15, 1.0)
 * arr[1] = Coordinate(0.95, -0.4, 0.15, 1.0)
 * arr[2] = Coordinate(0.95, 0.4, 0.15, 1.0)
 * arr[3] = Coordinate(0.95, 0.4, -0.15, 1.0)
 */
fun entrance(): DrawableObject {
    val entranceVertices: Array<Coordinate?> = kotlin.run {
        val arr: Array<Coordinate?> = Array(4) { null }
        arr[0] = Coordinate(-1.0, -0.5, 1.0, 1.0)
        arr[1] = Coordinate(1.0, -0.5, 1.0, 1.0)
        arr[2] = Coordinate(1.0, 0.5, 1.0, 1.0)
        arr[3] = Coordinate(-1.0, 0.5, 1.0, 1.0)
        arr
    }

    return object : DrawableObject(entranceVertices) {
        override val paint = Paint().apply { color = Color.BLUE; strokeWidth = 5.0f; style = Paint.Style.STROKE }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun drawOnCanvas(canvas: Canvas) {
            global.apply {
                drawLinePairs(canvas, this, 0, 1, paint)
                drawLinePairs(canvas, this, 1, 2, paint)
                drawLinePairs(canvas, this, 2, 3, paint)
                drawLinePairs(canvas, this, 3, 0, paint)
            }
        }
    }
}