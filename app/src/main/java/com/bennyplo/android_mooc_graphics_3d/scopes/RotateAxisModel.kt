package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.*

data class RotationScope(val theta: Double, val axis: Coordinate) : ParameterScope

class RecursionRotateAxisModel(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<RotationScope>(connectableObject, { obj, scope, info ->
            obj.apply {
                scope.apply {
                    // rotate
                    model = rotateAxis(model, theta, axis)
                    global = model.copyOf()
                    // scale joints coordinate positions
                    for (i in links) {
                        i.value.rotateAxisModel(theta, axis, info?.rotateAxes ?: true)
                    }
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.apply {
                    rotateAxisModel(theta, axis, record)
                }
            }
        })