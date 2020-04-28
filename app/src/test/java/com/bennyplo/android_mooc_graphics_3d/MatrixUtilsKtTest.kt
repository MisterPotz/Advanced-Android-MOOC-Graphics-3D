package com.bennyplo.android_mooc_graphics_3d


import android.graphics.Canvas
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs

class MatrixUtilsKtTest {

    @Test
    fun rotateAxisTest() {
        val coordinate = Array<Coordinate?>(1) { Coordinate(3.0, 4.0, 2.0, 1.0) }
        val anotherCoordinate = rotateAxis(coordinate, 60.0, Coordinate(0.0, 1.0, 0.0, 1.0))
        val some = anotherCoordinate[0]
        assertTrue(some!!.x.isAbout(3.23205))
        assertTrue(some.y.isAbout(4.0))
        assertTrue(some.z.isAbout(-1.59807))
        assertTrue(some.w.isAbout(1.0))
    }

    @Test
    fun translateTest() {
        val coordinate = Array<Coordinate?>(1) { Coordinate(1.0, 1.0, 1.0, 1.0) }
        val expected = Array<Coordinate?>(1) { Coordinate(2.0, 1.0, 2.0, 1.0) }
        val translated = translate(coordinate, 1.0, 0.0, 1.0)
        assertArrayEquals(expected, translated)
    }

    fun Double.isAbout(target: Double): Boolean {
        if (target == 0.0 || this == 0.0) {
            return abs(this - target) <= 0.001
        }
        return abs(this / target).run { this in 0.99..1.01 }
    }

    @Test
    fun project() {
        val coordinate: Array<Coordinate?> = arrayOf(Coordinate(0.0, 0.0, -1.0, 1.0))
        val newCoordinate = project(coordinate, -1.0, 1.0, 1.0, -1.0, 1.0, 10.0)
//        newCoordinate[0]!!.Normalise()
        assertArrayEquals(arrayOf(Coordinate(0.0, 0.0, -1.0, 1.0)), newCoordinate)

        val endCoordinate: Array<Coordinate?> = arrayOf(Coordinate(0.0, 0.0, -10.0, 1.0))
        val newEndCoordinate = project(endCoordinate, -1.0, 1.0, 1.0, -1.0, 1.0, 10.0)
        assertArrayEquals(arrayOf(Coordinate(0.0, 0.0, 1.0, 1.0)), newEndCoordinate)

    }

    @Test
    fun projectReverse() {
        val coordinate: Array<Coordinate?> = arrayOf(Coordinate(0.0, 0.0, 1.0, 1.0))
        val newCoordinate = project(coordinate, -1.0, 1.0, 1.0, -1.0, -1.0, 10.0)

//        newCoordinate[0]!!.Normalise()
        assertArrayEquals(arrayOf(Coordinate(0.0, 0.0, -1.0, 1.0)), newCoordinate)

        val endCoordinate: Array<Coordinate?> = arrayOf(Coordinate(0.0, 0.0, -10.0, 1.0))
        val newEndCoordinate = project(endCoordinate, -1.0, 1.0, 1.0, -1.0, -1.0, 10.0)
        assertArrayEquals(arrayOf(Coordinate(0.0, 0.0, 1.0, 1.0)), newEndCoordinate)
    }

    @Test
    fun projectScaled() {
        val coordinate: Array<Coordinate?> = arrayOf(Coordinate(100.0, 100.0, -100.0, 1.0))
        val newCoordinate = project(coordinate, 100.0, -100.0, -100.0, 100.0, -100.0, 100.0)
//        newCoordinate[0]!!.Normalise()
        assertArrayEquals(arrayOf(Coordinate(1.0, 1.0, 1.0, 1.0)), newCoordinate)

//        val endCoordinate : Array<Coordinate?> = arrayOf(Coordinate(0.0, 0.0, -10.0, 1.0))
//        val newEndCoordinate = project(endCoordinate, -1.0, 1.0, 1.0, -1.0, 1.0, 10.0)
//        assertArrayEquals(arrayOf(Coordinate(0.0, 0.0,1.0,1.0)), newEndCoordinate)
    }


    @Test
    fun scaleTo100AndProject() {
        val coordinate: Array<Coordinate?> = arrayOf(Coordinate(1.0, 1.0, -100.0, 1.0))
        val newCoordinate = project(coordinate, 1.0, -1.0, -1.0, 1.0, -1.0, 1.0)
//        newCoordinate[0]!!.Normalise()
        assertArrayEquals(arrayOf(Coordinate(0.01, 0.01, 0.01, 1.0)), newCoordinate)

//        val endCoordinate : Array<Coordinate?> = arrayOf(Coordinate(0.0, 0.0, -10.0, 1.0))
//        val newEndCoordinate = project(endCoordinate, -1.0, 1.0, 1.0, -1.0, 1.0, 10.0)
//        assertArrayEquals(arrayOf(Coordinate(0.0, 0.0,1.0,1.0)), newEndCoordinate)
    }

    @Test
    fun rotatePointAxis() {
        val coordinate: Array<Coordinate?> = arrayOf(Coordinate( 1.0, 1.0, 0.0, 1.0))

        val axis = Coordinate(1.0, 1.0, 0.0, 1.0)
        val new = rotateAxis(coordinate, 90.0, axis)

        assertArrayEquals(coordinate, new)
    }
}