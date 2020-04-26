package com.bennyplo.android_mooc_graphics_3d.robot

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.bennyplo.android_mooc_graphics_3d.*

// simple cube head
fun head(): ConnectableObject {
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

    return object : ConnectableObject(cube_vertices) {
        override fun rawDraw(canvas: Canvas) {
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

fun pseudoHead(): ConnectableObject {
    val cube_vertices //the vertices of a 3D cube
            : Array<Coordinate?> = run {
        val arr = Array<Coordinate?>(size = 8) { null }
        arr[0] = Coordinate(-0.5, -0.5, -0.5, 1.0)
        arr[1] = Coordinate(-1.0, -1.0, 1.0, 1.0)
        arr[2] = Coordinate(-1.0, 1.0, -1.0, 1.0)
        arr[3] = Coordinate(-1.0, 1.0, 1.0, 1.0)
        arr[4] = Coordinate(1.0, -1.0, -1.0, 1.0)
        arr[5] = Coordinate(1.0, -1.0, 1.0, 1.0)
        arr[6] = Coordinate(1.0, 1.0, -1.0, 1.0)
        arr[7] = Coordinate(1.0, 1.0, 1.0, 1.0)
        arr
    }

    return object : ConnectableObject(cube_vertices) {
        override val paint: Paint
            get() = super.paint.apply { color = Color.BLUE }

        override fun rawDraw(canvas: Canvas) {
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

fun littleHead(): ConnectableObject {
    val cube_vertices //the vertices of a 3D cube
            : Array<Coordinate?> = run {
        val arr = Array<Coordinate?>(size = 8) { null }
        arr[0] = Coordinate(-0.5, -0.5, -0.5, 1.0)
        arr[1] = Coordinate(-1.0, -1.0, 1.0, 1.0)
        arr[2] = Coordinate(-1.0, 1.0, -1.0, 1.0)
        arr[3] = Coordinate(-1.0, 1.0, 1.0, 1.0)
        arr[4] = Coordinate(1.0, -1.0, -1.0, 1.0)
        arr[5] = Coordinate(0.5, -0.5, 0.5, 1.0)
        arr[6] = Coordinate(1.0, 1.0, -1.0, 1.0)
        arr[7] = Coordinate(1.0, 1.0, 1.0, 1.0)
        arr
    }

    return object : ConnectableObject(cube_vertices) {
        override val paint: Paint
            get() = super.paint.apply { color = Color.GREEN }

        override fun rawDraw(canvas: Canvas) {
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

/**
 * upper part - half-links: 20
 * lower part - half-links: 20
 * lower part - half-links - axes: 20 (rotate upper part independently)
 */
fun twoConnectedeCubes(): ConnectableObject {
    val obj1 = head()
    val obj2 = head()
    val obj3 = head()
    val key = 20
    val key2 = 25
    // Making half links to connect obj1 and obj2
    obj1.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))
    obj2.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))

    // Making half links to connect obj2 and obj3
    obj2.addHalfLink(key2, Coordinate(1.0, 0.0, 0.0, 1.0))
    obj3.addHalfLink(key2, Coordinate(-1.0, 0.0, 0.0, 1.0))

    // connecting obj1 and obj2 via pre-set halflinks
    obj1.halfLink(key).connectTo(obj2.halfLink(key))
    obj2.halfLink(key2).connectTo(obj3.halfLink(key2))

    // Adding rotational axis to obj1's halflink to be able rotate children
    obj1.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))
    obj2.halfLink(key2).addRotationAxis(key2, Coordinate(1.0,0.0,0.0,1.0))

    // scaling model vertices to be able to see smthng at all
//    obj1.scaleModel(100.0, 100.0, 100.0, TransformationInfo.empty())
    return obj1
}