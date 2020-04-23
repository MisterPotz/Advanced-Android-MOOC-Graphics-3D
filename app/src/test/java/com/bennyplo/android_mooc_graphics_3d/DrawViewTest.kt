package com.bennyplo.android_mooc_graphics_3d

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class DrawViewTest {
    @Test
    fun matrix_getAt() {
        val matrix = DrawView.getIdentityMatrix()
        assertTrue(matrix.getAt(0) == 1.0)
        assertTrue(matrix.getAt(1) == 0.0)
        assertTrue(matrix.getAt(6) == 0.0)
        assertTrue(matrix.getAt(6) == 0.0)
        assertTrue(matrix.getAt(10) == 1.0)
    }

    @Test
    fun matrix_setAt() {
        val matrix = DrawView.getIdentityMatrix()
        assertTrue(matrix.getAt(0) == 1.0)
        matrix.setAt(0, 0.0)
        assertTrue(matrix.getAt(0) == 0.0)
    }

    @Test
    fun matrix_multiplyAt() {
        val matrix = DrawView.getIdentityMatrix()
        assertTrue(matrix.getAt(0) == 1.0)
        matrix.multiplyAt(0, 5.0)
        assertTrue(matrix.getAt(0) == 5.0)
        assertTrue(matrix.getAt(1) == 0.0)
        matrix.multiplyAt(1, 5.0)
        assertTrue(matrix.getAt(1) == 5.0)
    }
}