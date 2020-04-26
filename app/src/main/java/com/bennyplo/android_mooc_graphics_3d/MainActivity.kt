package com.bennyplo.android_mooc_graphics_3d

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bennyplo.android_mooc_graphics_3d.robot.AcceptingDrawView
import com.bennyplo.android_mooc_graphics_3d.robot.twoConnectedeCubes
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
/*    val key = 20
    val key2 = 25
    val obj = twoConnectedeCubes()
    val obj2 = obj.halfLink(key).getOtherParent()!!
    val obj3 = obj2.halfLink(key2).getOtherParent()!!
    val link1 = obj.halfLink(key)
    val link2 = obj2.halfLink(key2)*/
    var view : AcceptingDrawView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = AcceptingDrawView(this)
        view!!.setWillNotDraw(false)

        val drawView = DrawView(this)
        drawView.setWillNotDraw(false)

        /*link1.rotateOtherAroundAxisModel(key, 90.0)
        link2.rotateOtherAroundAxisModel(key2, 40.0)*/
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                view!!.invalidate()
                delay(16)
            }
        }
    }
}