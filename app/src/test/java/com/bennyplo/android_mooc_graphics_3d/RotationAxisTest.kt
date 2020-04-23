package com.bennyplo.android_mooc_graphics_3d

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class RotationAxisTest {
    @Test
    fun globalRotation() {
        val axis = RotationAxis(Coordinate(1.0,0.5,0.0, 1.0))

        axis.rotateGlobal(90.0, Coordinate(1.0,0.0,0.0, 1.0))

        val expectedGlobal = Coordinate(1.0, 0.0, 0.5, 1.0)

        assertEquals(expectedGlobal, axis.global)
    }

    @Test
    fun modelRotation() {
        val axis = RotationAxis(Coordinate(1.0,0.5,0.0, 1.0))

        axis.rotateModel(90.0, Coordinate(1.0,0.0,0.0, 1.0))

        val expectedModel = Coordinate(1.0, 0.0, 0.5, 1.0)

        assertEquals(expectedModel, axis.model)
        assertEquals(expectedModel, axis.global)
    }
}