package com.bennyplo.android_mooc_graphics_3d

class RotationAxis(val local : Coordinate) {
    var model: Coordinate = local.copy()
    var global : Coordinate = model.copy()

    fun rotateModel(theta : Double, axis: Coordinate) {
        model = rotateAxis(arrayOf(model), theta, axis)[0]!!
        global = model.copy()
    }

    fun rotateGlobal(theta: Double, axis:Coordinate) {
        global = rotateAxis(arrayOf(global), theta, axis)[0]!!
    }
}