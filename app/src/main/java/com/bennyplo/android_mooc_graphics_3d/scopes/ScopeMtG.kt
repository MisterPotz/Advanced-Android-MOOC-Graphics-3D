package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.*

class ScopeMtG() : ParameterScope

class RecursionMtG(connectableObject: ConnectableObject) : RecursionSafeDelegate<ScopeMtG>(connectableObject, { obj, mtg, _ ->
    obj.apply { global = model.copyOf() }
}, { obj, scope, record ->
    obj.apply { modelToGlobal(record) }
})