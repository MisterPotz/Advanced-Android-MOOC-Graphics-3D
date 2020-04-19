package com.bennyplo.android_mooc_graphics_3d.robot

import android.graphics.Canvas
import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.Coordinate
import com.bennyplo.android_mooc_graphics_3d.DrawableObject
import com.bennyplo.android_mooc_graphics_3d.HalfLink

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
        override fun drawOnCanvas(canvas: Canvas) {
            verticesToDraw.apply {
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

fun twoConnectedeCubes(): ConnectableObject {
    val obj1 = head()
    val obj2 = head()
    val key = 20
    obj1.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 0.0))
    obj2.addHalfLink(key, Coordinate(0.0,1.0,0.0,0.0))

    // connecting two objects
    obj1.halfLink(key).connectTo(obj2.halfLink(key))

    return obj1
}