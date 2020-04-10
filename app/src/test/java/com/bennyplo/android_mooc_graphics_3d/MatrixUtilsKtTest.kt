package com.bennyplo.android_mooc_graphics_3d

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs

class MatrixUtilsKtTest {

    @Test
    fun rotateAxisTest() {
        val coordinate = Array<Coordinate?>(1) { Coordinate(3.0,4.0,2.0, 0.0) }
        val anotherCoordinate = rotateAxis(coordinate, 60.0, Coordinate(0.0,1.0,0.0, 0.0))
        val some = anotherCoordinate[0]
        assertTrue(some!!.x.isAbout(3.23205))
        assertTrue(some.y.isAbout(4.0))
        assertTrue(some.z.isAbout(-1.59807))
        assertTrue(some.w.isAbout(0.0))
    }

    fun Double.isAbout(target : Double) : Boolean {
        if (target == 0.0 || this == 0.0) {
            return abs(this - target) <= 0.001
        }
        return abs(this / target).run { this in 0.99..1.01 }
    }
}