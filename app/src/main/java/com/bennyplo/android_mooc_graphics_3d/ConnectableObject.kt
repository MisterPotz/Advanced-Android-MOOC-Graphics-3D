package com.bennyplo.android_mooc_graphics_3d

import android.graphics.Canvas

abstract class ConnectableObject(vertices: Array<Coordinate?>) : DrawableObject(vertices) {
    // to connect more drawables to this node, may use additional structure.
    val links = mutableMapOf<Int, HalfLink>()
    // subclasses must implement how this is drawn

    fun addHalfLink(key: Int, coordinate: Coordinate) {
        // adding new half link connected to this node
        links[key] = HalfLink(this, coordinate)
    }

    fun halfLink(key: Int): HalfLink {
        return links[key]!!
    }
}

/**
 * [drawableObject] - object that this link is currently attached to
 * [coordinate] - coordinate where the half link is attached
 */
class HalfLink(val drawableObject: DrawableObject, val coordinate: Coordinate) {
    var anotherLink: HalfLink? = null

    // currenct coordinate
    var drawingCoordinate: Coordinate = coordinate.copy()

    // may add here rotational axis and other shit
    fun connectTo(halfLink: HalfLink) {
        // two-sided linked connection, also objects must be translated so two half links are coincided
        anotherLink = halfLink
        halfLink.anotherLink = this
        // TODO сделать дефолтную координату у другого линка равной дефолтному координату этого линка

        halfLink.performConnection(drawingCoordinate)
    }

    // rotate daughter element around given axis
    fun rotate(theta: Double, axis: Coordinate) {
        // only if have other link doesnt equal to zero
        if (anotherLink != null) {
            anotherLink!!.rotateParentElement(theta, axis)
        }
    }

    private fun rotateParentElement(theta: Double, axis: Coordinate) {
        coordinate.run {
            toLocalCoordinateSystem()
            // rotate element
            drawableObject.rotateAxis(theta, axis)
            // translating element back
            toGlobalCoordinateSystem()
        }
    }

    private fun toLocalCoordinateSystem() {
        coordinate.run {
            drawableObject.translate(-x, -y, -z)
        }
    }

    private fun toGlobalCoordinateSystem() {
        coordinate.run {
            drawableObject.translate(x, y, z)
        }
    }

    /**
     * Accepts coordinates of joint, that is connecting this joint,
     * so parent of connecting joint goes as the base and object of this current joint
     * is being translated to the place of connection
     */
    private fun performConnection(coordinate: Coordinate) {
        val x = coordinate.x + this.coordinate.x
        val y = coordinate.y + this.coordinate.y
        val z = coordinate.z + this.coordinate.z
        translate(x, y, z)
    }

    fun translate(dx: Double, dy: Double, dz: Double) {
        drawingCoordinate = drawingCoordinate?.run { Coordinate(x, y, z, 0.0) }
                ?: Coordinate(dx, dy, dz, 0.0)
    }
}