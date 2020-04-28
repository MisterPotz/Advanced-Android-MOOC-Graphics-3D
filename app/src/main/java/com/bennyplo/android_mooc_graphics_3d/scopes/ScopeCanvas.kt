package com.bennyplo.android_mooc_graphics_3d.scopes

import android.graphics.Canvas
import com.bennyplo.android_mooc_graphics_3d.*

data class ScopeCanvas(val canvas: Canvas) : ParameterScope

class RecursionCanvas(connectableObject: ConnectableObject) : RecursionSafeDelegate<ScopeCanvas>(connectableObject, { obj, a, _ ->
    obj.rawDraw(a.canvas)
}, { obj, scope, record ->
    obj.fullDraw(scope.canvas, record)
})