package com.bennyplo.android_mooc_graphics_3d

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bennyplo.android_mooc_graphics_3d.robot.TestingHalfLinksView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var drawView: DrawView? = null //a custom view for drawing
    private var drawView2 : TestingHalfLinksView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);
        supportActionBar!!.hide() //hide the title bar
//        drawView = DrawView(this)
        drawView2 = TestingHalfLinksView(this)
        var angleX: Double = 90.0
        var angleY: Double = 25.0
        var angleZ: Double = 0.0
        var angle: Double = 25.0

        drawView2!!.setup = {
            println("In loop, angleX: $angleX")
            println("In loop, angleY: $angleY")
            println("In loop, angleZ: $angleZ")

            rotateAxis(angle,axis(1,0,0))
            translate(200.0, 0.0, 100.0)
//            rotateAxis(angle, axis(0, 0, 1))
            placeInCenter()

            angle += 1.0
            if (angleX >= 360) angleX = 0.0;
            if (angleY >= 360) angleY = 0.0;
            if (angleZ >= 360) angleZ = 0.0;
            if (angle >= 360) angle = 0.0;
        }
        setContentView(drawView2)
    }

    override fun onStart() {
        super.onStart()
        launch {
            while (isActive) {
                drawView2!!.invalidate()
                delay(10)
            }
        }
    }
}