package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.RecursionSafeDelegate
import com.bennyplo.android_mooc_graphics_3d.scale
import com.bennyplo.android_mooc_graphics_3d.translate


class RecursionScaleModel(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<CoordinatesScope>(connectableObject, { obj, scope,info ->
            obj.apply {
                scope.apply {
                    // scale the vertices
                    model = scale(model, dx, dy, dz)
                    global=  model.copyOf()
                    // scale joints coordinate positions
                    for (i in links) {
                        i.value.scaleModel(dx, dy, dz)
                    }
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.apply {
                    obj.scaleModel(dx, dy, dz, record)
                }
            }
        })