package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.RecursionSafeDelegate
import com.bennyplo.android_mooc_graphics_3d.TransformationInfo
import com.bennyplo.android_mooc_graphics_3d.translate

class RecursionTranslationModel(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<CoordinatesScope>(connectableObject, { obj, scope, _ ->
            obj.apply {
                scope.apply {
                    model = translate(model, dx, dy, dz)
                    global = model.copyOf()
                    for (i in links) {
                        i.value.translateModel(dx, dy, dz)
                    }
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.apply {
                    obj.translateModel(dx, dy, dz, record)
                }
            }
        })