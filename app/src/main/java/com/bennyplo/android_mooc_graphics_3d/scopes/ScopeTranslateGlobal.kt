package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.*

data class CoordinatesScope(val dx : Double, val dy: Double, val dz: Double) : ParameterScope

class RecursionTranslationGlobal(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<CoordinatesScope>(connectableObject, { obj, scope ->
    obj.apply {
        scope.apply {
            global = translate(global,  dx, dy, dz)
            for (i in links) {
                i.value.translateGlobal(dx, dy, dz)
            }
        }
    }
}, { obj, scope, record ->
    obj.apply {
        scope.apply {
            obj.translateGlobal(dx, dy, dz, record)
        }
    }
})