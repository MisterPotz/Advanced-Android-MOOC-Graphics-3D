package com.bennyplo.android_mooc_graphics_3d.robot

import com.bennyplo.android_mooc_graphics_3d.Coordinate
import com.bennyplo.android_mooc_graphics_3d.HalfLink

enum class AxesType(val coordinate: Coordinate) {
    X(Coordinate(1.0, 0.0, 0.0, 1.0)),
    Y(Coordinate(0.0, 1.0, 0.0, 1.0)),
    Z(Coordinate(0.0, 0.0, 1.0, 1.0))
}

class AxisSaver(xKey: Int, yKey: Int, zKey: Int) {
    // Contains relation between axis type and key to access that axis on some half link
    val axis = mutableMapOf<AxesType, Int>(AxesType.X to xKey, AxesType.Y to yKey, AxesType.Z to zKey)

    val x
        get() = axis[AxesType.X]!!

    val y
        get() = axis[AxesType.Y]!!

    val z
        get() = axis[AxesType.Y]!!

    fun addXTo(halfLink: HalfLink) {
        halfLink.addX(x)
    }

    fun addYTo(halfLink: HalfLink) {
        halfLink.addY(y)
    }

    fun addZTo(halfLink: HalfLink) {
        halfLink.addZ(z)
    }
}

fun HalfLink.addX(key: Int) {
    addRotationAxis(key, AxesType.X.coordinate)
}

fun HalfLink.addY(key: Int) {
    addRotationAxis(key, AxesType.Y.coordinate)
}

fun HalfLink.addZ(key: Int) {
    addRotationAxis(key, AxesType.Z.coordinate)
}
