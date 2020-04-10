package com.bennyplo.android_mooc_graphics_3d

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

interface Transformatable {
    fun shear(hx: Double, hy: Double)

    fun rotateX(xTheta: Double)

    fun rotateY(yTheta: Double)

    fun rotateZ(zTheta: Double)

    fun rotateAxis(theta: Double, axis: Coordinate)

    fun scale(times: Double)

    fun translate(dx: Double, dy: Double, dz: Double)

    fun restore()
}

abstract class DrawableObject(val vertices: Array<Coordinate?>) : Transformatable {
    abstract fun drawOnCanvas(canvas: Canvas)

    var verticesToDraw: Array<Coordinate?> = vertices.copyOf()

    open val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE //Stroke
        color = Color.RED
        strokeWidth = 3f
    }

    fun drawLinePairs(canvas: Canvas, vertices: Array<Coordinate?>, start: Int, end: Int, paint: Paint) { //draw a line connecting 2 points
//canvas - canvas of the view
//points - array of points
//start - index of the starting point
//end - index of the ending point
//paint - the paint of the line
        canvas.drawLine(vertices[start]!!.x.toFloat(), vertices[start]!!.y.toFloat(), vertices[end]!!.x.toFloat(), vertices[end]!!.y.toFloat(), paint)
    }

    override fun shear(hx: Double, hy: Double) {
        verticesToDraw = shear(verticesToDraw, hx, hy)
    }

    override fun rotateX(xTheta: Double) {
        verticesToDraw = rotateX(verticesToDraw, xTheta)
    }

    override fun rotateY(yTheta: Double) {
        verticesToDraw = rotateY(verticesToDraw, yTheta)
    }

    override fun rotateZ(zTheta: Double) {
        verticesToDraw = rotateZ(verticesToDraw, zTheta)
    }

    override fun restore() {
        verticesToDraw = vertices.copyOf()
    }

    override fun rotateAxis(theta: Double, axis: Coordinate) {
        verticesToDraw = rotateAxis(verticesToDraw, theta, axis)
    }

    override fun scale(times: Double) {
        verticesToDraw = scale(verticesToDraw, times, times, times)
    }

    override fun translate(dx: Double, dy: Double, dz: Double) {
        verticesToDraw = translate(verticesToDraw, dx, dy, dz)

    }
}