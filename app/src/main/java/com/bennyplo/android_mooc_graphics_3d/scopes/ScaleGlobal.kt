package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.RecursionSafeDelegate
import com.bennyplo.android_mooc_graphics_3d.scale

class RecursionScaleGlobal(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<CoordinatesScope>(connectableObject, { obj, scope ->
            obj.apply {
                scope.apply {
                    // scale the vertices
                    global = scale(global, dx, dy, dz)
                    // scale joints coordinate positions
                    for (i in links) {
                        i.value.scaleGlobal(dx, dy, dz)
                    }
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.apply {
                    scaleGlobal(dx, dy, dz, record)
                }
            }
        })