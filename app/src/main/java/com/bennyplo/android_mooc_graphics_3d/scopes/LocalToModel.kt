package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.RecursionSafeDelegate
import com.bennyplo.android_mooc_graphics_3d.rotateAxis


class RecursionLocalToModel(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<ScopeMtG>(connectableObject, { obj, scope, info ->
            obj.apply {
                scope.apply {
                    model = local.copyOf()
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.apply {
                    localToModel(record)
                }
            }
        })