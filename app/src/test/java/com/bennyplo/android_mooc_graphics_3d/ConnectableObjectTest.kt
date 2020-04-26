package com.bennyplo.android_mooc_graphics_3d

import android.graphics.Canvas
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.exp


class ConnectableObjectTest {
    fun square(): ConnectableObject {
        val cube_vertices //the vertices of a 3D cube
                : Array<Coordinate?> = run {
            val arr = Array<Coordinate?>(size = 4) { null }
            arr[0] = Coordinate(-1.0, -1.0, 1.0, 1.0)
            arr[1] = Coordinate(-1.0, 1.0, 1.0, 1.0)
            arr[2] = Coordinate(1.0, 1.0, 1.0, 1.0)
            arr[3] = Coordinate(1.0, -1.0, 1.0, 1.0)
            arr
        }

        return object : ConnectableObject(cube_vertices, {}) {
            override fun rawDraw(canvas: Canvas) {
                local.apply {
                    drawLinePairs(canvas, this, 0, 1, paint)
                    drawLinePairs(canvas, this, 1, 2, paint)
                    drawLinePairs(canvas, this, 2, 3, paint)
                    drawLinePairs(canvas, this, 3, 1, paint)
                }
            }
        }
    }

    fun assertLocal(vertices: Array<Coordinate?>) {
        val expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )

        assertArrayEquals(expected, vertices)
    }

    @Test
    fun testNoActionsDefault() {
        val obj = square()
        val expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )

        assertArrayEquals(expected, obj.model)
    }

    @Test
    fun translationSingle() {
        val obj = square().apply {
            translateGlobal(1.0, 0.0, 1.0)
        }
        val expected = arrayOf(
                Coordinate(0.0, -1.0, 2.0, 1.0),
                Coordinate(0.0, 1.0, 2.0, 1.0),
                Coordinate(2.0, 1.0, 2.0, 1.0),
                Coordinate(2.0, -1.0, 2.0, 1.0)
        )
        assertArrayEquals(expected, obj.global)
    }

    @Test
    fun translationWithinConnection() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        val modelExpected1 = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )
        val model2Expected = arrayOf(
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(-1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )

        assertArrayEquals(modelExpected1, obj.model)
        assertArrayEquals(model2Expected, obj2.model)

        assertArrayEquals(modelExpected1, obj.global)
        assertArrayEquals(model2Expected, obj2.global)

        val expectedJoint = Coordinate(0.0, 1.0, 0.0, 1.0)
        assertEquals(expectedJoint, obj.halfLink(key).model)
        assertEquals(expectedJoint, obj2.halfLink(key).model)
        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)
    }

    @Test
    fun scaleSingleModel() {
        val obj = square()

        obj.scaleModel(20.0, 20.0, 20.0, TransformationInfo.empty())

        val modelExpected = arrayOf(
                Coordinate(-20.0, -20.0, 20.0, 1.0),
                Coordinate(-20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, -20.0, 20.0, 1.0)
        )
        assertArrayEquals(modelExpected, obj.model)
    }

    @Test
    fun scaleSingleGlobal() {
        val obj = square()

        obj.scaleGlobal(20.0, 20.0, 20.0)

        val modelExpected = arrayOf(
                Coordinate(-20.0, -20.0, 20.0, 1.0),
                Coordinate(-20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, -20.0, 20.0, 1.0)
        )
        assertArrayEquals(modelExpected, obj.global)
    }

    @Test
    fun rotateSingle() {
        val obj = square()
        obj.rotateAxisGlobal(90.0, Coordinate(1.0, 0.0, 0.0, 0.0))
        val modelExpected = arrayOf(
                Coordinate(-1.0, -1.0, -1.0, 1.0),
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, -1.0, 1.0)
        )

        assertArrayEquals(modelExpected, obj.global)
        assertLocal(obj.local)
    }

    @Test
    fun scaleConnectedGlobal() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.scaleGlobal(20.0, 20.0, 20.0)
        // изменение в моделе должны сразу писаться в глобал
        val global1Expected = arrayOf(
                Coordinate(-20.0, -20.0, 20.0, 1.0),
                Coordinate(-20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, -20.0, 20.0, 1.0)
        )
        val global2Expected = arrayOf(
                Coordinate(-20.0, 20.0, 20.0, 1.0),
                Coordinate(-20.0, 60.0, 20.0, 1.0),
                Coordinate(20.0, 60.0, 20.0, 1.0),
                Coordinate(20.0, 20.0, 20.0, 1.0)
        )
        val model1Expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0))

        val model2Expected = arrayOf(
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(-1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )
        // Asserting array vertices
        assertArrayEquals(global1Expected, obj.global)
        assertArrayEquals(global2Expected, obj2.global)

        assertArrayEquals(model1Expected, obj.model)
        assertArrayEquals(model2Expected, obj2.model)
        // ASserting scaling affection on joint
        val expectedJoint = Coordinate(0.0, 20.0, 0.0, 1.0)

        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)
    }

    @Test
    fun scaleConnectedModel() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.scaleModel(20.0, 20.0, 20.0, TransformationInfo.empty())
        // изменение в моделе должны сразу писаться в глобал
        val model1Expected = arrayOf(
                Coordinate(-20.0, -20.0, 20.0, 1.0),
                Coordinate(-20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, 20.0, 20.0, 1.0),
                Coordinate(20.0, -20.0, 20.0, 1.0)
        )
        val model2Expected = arrayOf(
                Coordinate(-20.0, 20.0, 20.0, 1.0),
                Coordinate(-20.0, 60.0, 20.0, 1.0),
                Coordinate(20.0, 60.0, 20.0, 1.0),
                Coordinate(20.0, 20.0, 20.0, 1.0)
        )
        // Asserting array vertices
        assertArrayEquals(model1Expected, obj.global)
        assertArrayEquals(model2Expected, obj2.global)

        assertArrayEquals(model1Expected, obj.model)
        assertArrayEquals(model2Expected, obj2.model)

        // ASserting scaling affection on joint
        val expectedJoint = Coordinate(0.0, 20.0, 0.0, 1.0)

        assertEquals(expectedJoint, obj.halfLink(key).model)
        assertEquals(expectedJoint, obj2.halfLink(key).model)

        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)
    }

    @Test
    fun scaleAndTranslateConnectedGlobal() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.scaleGlobal(20.0, 20.0, 20.0)
        obj.translateGlobal(500.0, 500.0, 0.0)
        // изменение в моделе должны сразу писаться в глобал
        val global1Expected = arrayOf(
                Coordinate(480.0, 480.0, 20.0, 1.0),
                Coordinate(480.0, 520.0, 20.0, 1.0),
                Coordinate(520.0, 520.0, 20.0, 1.0),
                Coordinate(520.0, 480.0, 20.0, 1.0)
        )
        val global2Expected = arrayOf(
                Coordinate(480.0, 520.0, 20.0, 1.0),
                Coordinate(480.0, 560.0, 20.0, 1.0),
                Coordinate(520.0, 560.0, 20.0, 1.0),
                Coordinate(520.0, 520.0, 20.0, 1.0)
        )
        // Asserting array vertices
        assertArrayEquals(global1Expected, obj.global)
        assertArrayEquals(global2Expected, obj2.global)

        // ASserting scaling affection on joint
        val expectedJoint = Coordinate(500.0, 520.0, 0.0, 1.0)

        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)
    }

    @Test
    fun translateAsWholeGlobal() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key)
                .connectTo(obj2.halfLink(key))

        val model1Expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0))

        val model2Expected = arrayOf(
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(-1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )

        obj.translateGlobal(1000.0, 1000.0, 0.0)

        val global1Expected1 = arrayOf(
                Coordinate(999.0, 999.0, 1.0, 1.0),
                Coordinate(999.0, 1001.0, 1.0, 1.0),
                Coordinate(1001.0, 1001.0, 1.0, 1.0),
                Coordinate(1001.0, 999.0, 1.0, 1.0)
        )
        val global2Expected = arrayOf(
                Coordinate(999.0, 1001.0, 1.0, 1.0),
                Coordinate(999.0, 1003.0, 1.0, 1.0),
                Coordinate(1001.0, 1003.0, 1.0, 1.0),
                Coordinate(1001.0, 1001.0, 1.0, 1.0)
        )

        val expectedJoint = Coordinate(1000.0, 1001.0, 0.0, 1.0)

        assertArrayEquals(global1Expected1, obj.global)
        assertArrayEquals(global2Expected, obj2.global)

        assertArrayEquals(model1Expected, obj.model)
        assertArrayEquals(model2Expected, obj2.model)

        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)

        val expectedModelJoint = Coordinate(0.0, 1.0, 0.0, 1.0)
        assertEquals(expectedModelJoint, obj.halfLink(key).model)
        assertEquals(expectedModelJoint, obj2.halfLink(key).model)
    }

    @Test
    fun rotateAsWholeGlobal() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        val model1Expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0))

        val model2Expected = arrayOf(
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(-1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )

        obj.rotateAxisGlobal(90.0, Coordinate(1.0, 0.0, 0.0, 0.0))

        val global1Expected1 = arrayOf(
                Coordinate(-1.0, -1.0, -1.0, 1.0),
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, -1.0, 1.0)
        )
        val global2Expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, -1.0, 3.0, 1.0),
                Coordinate(1.0, -1.0, 3.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )

        assertArrayEquals(global1Expected1, obj.global)
        assertArrayEquals(global2Expected, obj2.global)

        assertArrayEquals(model1Expected, obj.model)
        assertArrayEquals(model2Expected, obj2.model)

        val expectedJoint = Coordinate(0.0, 0.0, 1.0, 1.0)

        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)

        val expectedModelJoint = Coordinate(0.0, 1.0, 0.0, 1.0)
        assertEquals(expectedModelJoint, obj.halfLink(key).model)
        assertEquals(expectedModelJoint, obj2.halfLink(key).model)
    }

    @Test
    fun rotateAndTranslateAsSingleGlobal() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        val model1Expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0))

        val model2Expected = arrayOf(
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(-1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )

        obj.rotateAxisGlobal(90.0, Coordinate(1.0, 0.0, 0.0, 0.0))
        obj.translateGlobal(10.0, 0.0, 0.0)

        val global1Expected1 = arrayOf(
                Coordinate(9.0, -1.0, -1.0, 1.0),
                Coordinate(9.0, -1.0, 1.0, 1.0),
                Coordinate(11.0, -1.0, 1.0, 1.0),
                Coordinate(11.0, -1.0, -1.0, 1.0)
        )
        val global2Expected = arrayOf(
                Coordinate(9.0, -1.0, 1.0, 1.0),
                Coordinate(9.0, -1.0, 3.0, 1.0),
                Coordinate(11.0, -1.0, 3.0, 1.0),
                Coordinate(11.0, -1.0, 1.0, 1.0)
        )

        assertArrayEquals(global1Expected1, obj.global)
        assertArrayEquals(global2Expected, obj2.global)

        assertArrayEquals(model1Expected, obj.model)
        assertArrayEquals(model2Expected, obj2.model)

        val expectedJoint = Coordinate(10.0, 0.0, 1.0, 1.0)

        assertEquals(expectedJoint, obj.halfLink(key).global)
        assertEquals(expectedJoint, obj2.halfLink(key).global)

        val expectedModelJoint = Coordinate(0.0, 1.0, 0.0, 1.0)
        assertEquals(expectedModelJoint, obj.halfLink(key).model)
        assertEquals(expectedModelJoint, obj2.halfLink(key).model)
    }

    @Test
    fun jointAxesRotation() {
        val halfLink = HalfLink(mockk(), Coordinate(1.0, 0.0, 0.0, 1.0))
        val key = 120
        halfLink.addRotationAxis(key, Coordinate(1.0, 0.5, 0.0, 1.0))

        halfLink.rotateAxisGlobal(90.0, Coordinate(1.0, 0.0, 0.0, 1.0))

        val expectedHalfLink = Coordinate(1.0, 0.0, 0.0, 1.0)
        val expectedModel = Coordinate(1.0, 0.0, 0.5, 1.0)

        assertEquals(expectedHalfLink, halfLink.global)
        assertEquals(expectedModel, halfLink.rotationAxis(key).global)
    }

    @Test
    fun jointAxesRotationAfterTranslation() {
        val halfLink = HalfLink(mockk(), Coordinate(1.0, 0.0, 0.0, 1.0))
        val key = 120
        halfLink.addRotationAxis(key, Coordinate(1.0, 0.5, 0.0, 1.0))

        halfLink.translateGlobal(0.0, 1.0, 0.0)
        halfLink.rotateAxisGlobal(90.0, Coordinate(1.0, 0.0, 0.0, 1.0))

        val expectedHalfLink = Coordinate(1.0, 0.0, 1.0, 1.0)
        val expectedModel = Coordinate(1.0, 0.0, 0.5, 1.0)

        assertEquals(expectedHalfLink, halfLink.global)
        assertEquals(expectedModel, halfLink.rotationAxis(key).global)
    }

    @Test
    fun relativeJointRotation() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))

        // now let's rotate the second part
        obj.halfLink(key).rotateOtherAroundAxisGlobal(key, theta = 90.0)

        val expectedGlobal1 = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )
        val expectedGlobal2 = arrayOf(
                Coordinate(0.0, 1.0, 2.0, 1.0),
                Coordinate(0.0, 3.0, 2.0, 1.0),
                Coordinate(0.0, 3.0, 0.0, 1.0),
                Coordinate(0.0, 1.0, 0.0, 1.0)
        )

        assertArrayEquals(expectedGlobal1, obj.global)
        assertArrayEquals(expectedGlobal2, obj2.global)
    }

    @Test
    fun relativeJointRotationScalingModel() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))

        obj.scaleModel(100.0, 100.0, 100.0, TransformationInfo.empty())
        // now let's rotate the second part
        obj.halfLink(key).rotateOtherAroundAxisModel(key, theta = 90.0)

        val expectedModel1 = arrayOf(
                Coordinate(-100.0, -100.0, 100.0, 1.0),
                Coordinate(-100.0, 100.0, 100.0, 1.0),
                Coordinate(100.0, 100.0, 100.0, 1.0),
                Coordinate(100.0, -100.0, 100.0, 1.0)
        )
        val expectedModel2 = arrayOf(
                Coordinate(0.0, 100.0, 200.0, 1.0),
                Coordinate(0.0, 300.0, 200.0, 1.0),
                Coordinate(0.0, 300.0, 0.0, 1.0),
                Coordinate(0.0, 100.0, 0.0, 1.0)
        )

        assertArrayEquals(expectedModel1, obj.model)
        assertArrayEquals(expectedModel2, obj2.model)
    }

    @Test
    fun relativeRotationAfterRotation() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))

        // rotating whole system
        obj.rotateAxisModel(90.0, Coordinate(1.0, 0.0, 0.0, 0.0), TransformationInfo.empty())
        obj.halfLink(key).rotateOtherAroundAxisModel(key, 90.0)
/*
        val expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )*/

        val expected1 = arrayOf(
                Coordinate(-1.0, -1.0, -1.0, 1.0),
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, -1.0, 1.0)
        )

        val expected2 = arrayOf(
                Coordinate(0.0, -2.0, 1.0, 1.0),
                Coordinate(0.0, -2.0, 3.0, 1.0),
                Coordinate(0.0, 0.0, 3.0, 1.0),
                Coordinate(0.0, 0.0, 1.0, 1.0)
        )

        assertArrayEquals(expected1, obj.model)
        assertArrayEquals(expected2, obj2.model)
    }

    @Test
    fun relativeRotationAfterRotation3Objects() {
        val obj = square()
        val obj2 = square()
        val obj3 = square()
        val key = 20
        val key2 = 25
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))

        obj2.addHalfLink(key2, Coordinate(1.0, 0.0, 0.0, 1.0))
        obj3.addHalfLink(key2, Coordinate(-1.0, 0.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj2.halfLink(key2).connectTo(obj3.halfLink(key2))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.halfLink(key2).addRotationAxis(key2, Coordinate(1.0, 0.0, 0.0, 1.0))

        // rotating whole system
        obj.rotateAxisModel(90.0, Coordinate(1.0, 0.0, 0.0, 0.0), TransformationInfo.empty())
        obj.halfLink(key).rotateOtherAroundAxisModel(key, 90.0)
/*
        val expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )*/

        val expected1 = arrayOf(
                Coordinate(-1.0, -1.0, -1.0, 1.0),
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, -1.0, 1.0)
        )

        val expected2 = arrayOf(
                Coordinate(0.0, -2.0, 1.0, 1.0),
                Coordinate(0.0, -2.0, 3.0, 1.0),
                Coordinate(0.0, 0.0, 3.0, 1.0),
                Coordinate(0.0, 0.0, 1.0, 1.0)
        )

        val expected3 = arrayOf(
                Coordinate(0.0, 0.0, 1.0, 1.0),
                Coordinate(0.0, 0.0, 3.0, 1.0),
                Coordinate(0.0, 2.0, 3.0, 1.0),
                Coordinate(0.0, 2.0, 1.0, 1.0)
        )

        assertArrayEquals(expected1, obj.model)
        assertArrayEquals(expected2, obj2.model)
        assertArrayEquals(expected3, obj3.model)
    }

    @Test
    fun relativeRotationAfterRotation3ObjectsComplex() {
        val obj = square()
        val obj2 = square()
        val obj3 = square()
        val key = 20
        val key2 = 25
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))

        obj2.addHalfLink(key2, Coordinate(1.0, 0.0, 1.0, 1.0))
        obj3.addHalfLink(key2, Coordinate(-1.0, 0.0, 1.0, 1.0))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj2.halfLink(key2).addRotationAxis(key2, Coordinate(1.0, 0.0, 0.0, 1.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))
        obj2.halfLink(key2).connectTo(obj3.halfLink(key2))


        val currentModel3 = arrayOf(
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(3.0, 3.0, 1.0, 1.0),
                Coordinate(3.0, 1.0, 1.0, 1.0)
        )
        assertArrayEquals(currentModel3, obj3.model)

        val initialHalflink = Coordinate(0.0, 1.0, 1.0, 1.0)
        assertEquals(initialHalflink, obj.halfLink(key).model)
        assertEquals(initialHalflink, obj2.halfLink(key).model)
        val initialHalflink2 = Coordinate(1.0, 2.0, 1.0, 1.0)
        assertEquals(initialHalflink2, obj2.halfLink(key2).model)
        assertEquals(initialHalflink2, obj3.halfLink(key2).model)

        // rotating whole system
        obj.rotateAxisModel(90.0, Coordinate(1.0, 0.0, 0.0, 1.0), TransformationInfo.empty())

        val afterRotationModel3 = arrayOf(
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 3.0, 1.0),
                Coordinate(3.0, -1.0, 3.0, 1.0),
                Coordinate(3.0, -1.0, 1.0, 1.0)
        )
        assertArrayEquals(afterRotationModel3, obj3.model)

        val afterRotationHalflink = Coordinate(0.0, -1.0, 1.0, 1.0)
        assertEquals(afterRotationHalflink, obj.halfLink(key).model)
        assertEquals(afterRotationHalflink, obj2.halfLink(key).model)
        val afterRotationHalflink2 = Coordinate(1.0, -1.0, 2.0, 1.0)
        assertEquals(afterRotationHalflink2, obj2.halfLink(key2).model)
        assertEquals(afterRotationHalflink2, obj3.halfLink(key2).model)

        obj.halfLink(key).rotateOtherAroundAxisModel(key, 90.0)

        val afterRelativeRotationModel3 = arrayOf(
                Coordinate(0.0, 0.0, 1.0, 1.0),
                Coordinate(0.0, 0.0, 3.0, 1.0),
                Coordinate(0.0, 2.0, 3.0, 1.0),
                Coordinate(0.0, 2.0, 1.0, 1.0)
        )
        assertArrayEquals(afterRelativeRotationModel3, obj3.model)

        val afterRotation2Halflink = Coordinate(0.0, -1.0, 1.0, 1.0)
        assertEquals(afterRotation2Halflink, obj.halfLink(key).model)
        assertEquals(afterRotation2Halflink, obj2.halfLink(key).model)
        val afterRotation2Halflink2 = Coordinate(0.0, 0.0, 2.0, 1.0)
        assertEquals(afterRotation2Halflink2, obj2.halfLink(key2).model)
        assertEquals(afterRotation2Halflink2, obj3.halfLink(key2).model)

        obj2.halfLink(key2).rotateOtherAroundAxisModel(key2, 90.0)
/*
        val expected = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )*/

        val expected1 = arrayOf(
                Coordinate(-1.0, -1.0, -1.0, 1.0),
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, -1.0, 1.0)
        )

        val expected2 = arrayOf(
                Coordinate(0.0, -2.0, 1.0, 1.0),
                Coordinate(0.0, -2.0, 3.0, 1.0),
                Coordinate(0.0, 0.0, 3.0, 1.0),
                Coordinate(0.0, 0.0, 1.0, 1.0)
        )

        val expected3 = arrayOf(
                Coordinate(-1.0, 0.0, 2.0, 1.0),
                Coordinate(1.0, 0.0, 2.0, 1.0),
                Coordinate(1.0, 2.0, 2.0, 1.0),
                Coordinate(-1.0, 2.0, 2.0, 1.0)
        )

        assertArrayEquals(expected1, obj.model)

        assertArrayEquals(expected2, obj.halfLink(key).getOtherParent()!!.model)
        assertArrayEquals(expected2, obj2.model)

        assertArrayEquals(expected3, obj3.model)
        assertArrayEquals(expected3, obj2.halfLink(key2).getOtherParent()!!.model)
    }

    @Test
    fun rotationInY() {
        val obj = square()
        val obj2 = square()
        val key = 20
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))
        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 1.0))
        obj.halfLink(key).rotateOtherAroundAxisModel(key, 90.0)

        val model2Expected = arrayOf(
                Coordinate(0.0, 1.0, 2.0, 1.0),
                Coordinate(0.0, 3.0, 2.0, 1.0),
                Coordinate(0.0, 3.0, 0.0, 1.0),
                Coordinate(0.0, 1.0, 0.0, 1.0)
        )

        assertArrayEquals(model2Expected, obj2.model)
    }

    @Test
    fun rotateWithTranslationOnly() {
        val obj = square()
        val obj2 = square()
        val obj3 = square()
        val key = 20
        val key2 = 21
        obj.addHalfLink(key, Coordinate(0.0, 1.0, 1.0, 1.0))
        obj2.addHalfLink(key, Coordinate(0.0, -1.0, 1.0, 1.0))
        obj2.addHalfLink(key2, Coordinate(1.0, 1.0, 1.0, 1.0))

        obj.halfLink(key).addRotationAxis(key, Coordinate(0.0, 1.0, 0.0, 0.0))

        obj.halfLink(key).connectTo(obj2.halfLink(key))

        obj3.addHalfLink(key2, Coordinate(0.0, -1.0, 1.0, 1.0))
        obj2.halfLink(key2).connectTo(obj3.halfLink(key2))

        val modelExpected1 = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )
        val model2Expected = arrayOf(
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(-1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 3.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0)
        )

        val modelExpected3 = arrayOf(
                Coordinate(0.0, 3.0, 1.0, 1.0),
                Coordinate(0.0, 5.0, 1.0, 1.0),
                Coordinate(2.0, 5.0, 1.0, 1.0),
                Coordinate(2.0, 3.0, 1.0, 1.0)
        )
        assertArrayEquals(modelExpected1, obj.model)
        assertArrayEquals(model2Expected, obj2.model)
        assertArrayEquals(modelExpected3, obj3.model)

        // 0
        //0
        //0
        obj.halfLink(key).rotateOtherSilentlyModel(key, 90.0)
        //   0
        //   |
        //   0

        val expected1 = arrayOf(
                Coordinate(-1.0, -1.0, 1.0, 1.0),
                Coordinate(-1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, 1.0, 1.0, 1.0),
                Coordinate(1.0, -1.0, 1.0, 1.0)
        )
        val expected2 = arrayOf(
                Coordinate(0.0, 1.0, 2.0, 1.0),
                Coordinate(0.0, 3.0, 2.0, 1.0),
                Coordinate(0.0, 3.0, 0.0, 1.0),
                Coordinate(0.0, 1.0, 0.0, 1.0)
        )

        val expected3 = arrayOf(
                Coordinate(-1.0, 3.0, 0.0, 1.0),
                Coordinate(-1.0, 5.0, 0.0, 1.0),
                Coordinate(1.0, 5.0, 0.0, 1.0),
                Coordinate(1.0, 3.0, 0.0, 1.0)
        )

        assertArrayEquals(expected1, obj.model)
        assertArrayEquals(expected2, obj2.model)
        assertArrayEquals(expected3, obj3.model)
    }
}