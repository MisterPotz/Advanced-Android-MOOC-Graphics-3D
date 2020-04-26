package com.bennyplo.android_mooc_graphics_3d.scopes

import com.bennyplo.android_mooc_graphics_3d.ConnectableObject
import com.bennyplo.android_mooc_graphics_3d.RecursionSafeDelegate
import com.bennyplo.android_mooc_graphics_3d.rotateAxis

class RecursionRotateAxisGlobal(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<RotationScope>(connectableObject, { obj, scope ->
            obj.apply {
                scope.apply {
                    // rotate
                    global = rotateAxis(global, theta, axis)
                    // scale joints coordinate positions
                    for (i in links) {
                        i.value.rotateAxisGlobal(theta, axis)
                    }
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.apply {
                    obj.rotateAxisGlobal(theta, axis, record)
                }
            }
        })