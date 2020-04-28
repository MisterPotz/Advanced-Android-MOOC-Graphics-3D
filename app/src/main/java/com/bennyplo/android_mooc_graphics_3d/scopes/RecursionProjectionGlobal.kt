package com.bennyplo.android_mooc_graphics_3d.scopes

import android.util.Log
import com.bennyplo.android_mooc_graphics_3d.*

data class PrjectionScope(val projectionBesicParameters: ProjectionBesicParameters) : ParameterScope

class RecursionProjectionGlobal(connectableObject: ConnectableObject) :
        RecursionSafeDelegate<PrjectionScope>(connectableObject, { obj, scope, info ->
            obj.apply {
                scope.projectionBesicParameters.apply {
                    // rotate
                    global = project(global, left, right, top, bottom, near, far)
                }
            }
        }, { obj, scope, record ->
            obj.apply {
                scope.projectionBesicParameters.apply {
                    obj.projectToGlobal(scope.projectionBesicParameters, record)
                }
            }
        })