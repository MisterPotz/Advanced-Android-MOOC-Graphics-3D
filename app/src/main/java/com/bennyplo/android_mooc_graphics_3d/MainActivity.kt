package com.bennyplo.android_mooc_graphics_3d

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bennyplo.android_mooc_graphics_3d.robot.AcceptingDrawView
import com.bennyplo.android_mooc_graphics_3d.robot.twoConnectedeCubes
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    val key = 20
    val key2 = 25
    val obj = twoConnectedeCubes()
    val obj2 = obj.halfLink(key).getOtherParent()!!
    val obj3 = obj2.halfLink(key2).getOtherParent()!!
    val link1 = obj.halfLink(key)
    val link2 = obj2.halfLink(key2)
    var view : AcceptingDrawView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = AcceptingDrawView(this)
        view!!.setWillNotDraw(false)
        view!!.sourceSet.add(obj)
        /*link1.rotateOtherAroundAxisModel(key, 90.0)
        link2.rotateOtherAroundAxisModel(key2, 40.0)*/
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Default).launch {
            val relative1 = 0.4
            val relative2 = 1.0
            var global = 30.0
            val ascendGlobal = 0.4
            while (isActive) {
                link1.rotateOtherAroundAxisModel(key, relative1)
                link2.rotateOtherAroundAxisModel(key2, relative2)
                view!!.globalSetup = {
                    Log.i("rotation", "Executing global rotation")
                    obj.rotateAxisGlobal(global, Coordinate(1.0, 0.0, 0.0, 1.0), TransformationInfo.empty())
                }
                global += ascendGlobal; if (global >= 360) global = 0.0
                view!!.invalidate()
                delay(16)
            }
        }
    }
/*
    val key = 20
    val keySecond = 25
    val obj = twoConnectedeCubes()
    val rotatingHalfLink1_2 = obj.halfLink(key)
    val rotatingHalfLink2_3 = run {
        val thing = obj.halfLink(key).run {
            getOtherParent()!!.halfLink(keySecond)
        }
        thing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);
        supportActionBar!!.hide() //hide the title bar
        acceptingDrawView = AcceptingDrawView(this)

        acceptingDrawView.sourceSet.add(obj)

        setContentView(acceptingDrawView)
    }

    override fun onStart() {
        super.onStart()
        launch {
            val relative1_2 = 0.4
            val relative2_3 = 1.0
            var global = 30.0
            val ascendGlobal = 0.4
            while (isActive) {
                //rotatingHalfLink1_2.rotateOtherAroundAxisModel(key, relative1_2)
                rotatingHalfLink2_3.rotateOtherAroundAxisModel(keySecond, relative2_3)
                acceptingDrawView.globalSetup = {
                    Log.i("rotation", "Executing global rotation")
                    obj.rotateAxisGlobal(global, Coordinate(1.0, 0.0, 0.0, 1.0), TransformationInfo.empty())
                }
                global += ascendGlobal; if (global >= 360) global = 0.0
                delay(16)
                acceptingDrawView.invalidate()
            }
        }
    }*/
}


// TODO сделать анализ очереди отрисовки по z
// TODO сделать вращение в джоинтах - чтобы это сделать адекватно, необходимо задать в локальных координатах джоинта доступные для
//  вращения оси, то есть настроить заранее массив доступных для вращения осей, и так же по какому-то ключу можно было бы их получать