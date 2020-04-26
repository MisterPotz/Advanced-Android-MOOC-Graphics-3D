package com.bennyplo.android_mooc_graphics_3d

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

interface TransformatableGlobal {
    fun shearGlobal(hx: Double, hy: Double)

    fun rotateAxisGlobal(theta: Double, axis: Coordinate)

    fun scaleGlobal(times: Double, dy : Double, dz: Double)

    fun translateGlobal(dx: Double, dy: Double, dz: Double)

    fun projectGlobal(left: Double, right: Double, top: Double, bottom: Double, near: Double, far: Double)

    fun projectGlobal(near: Double, far : Double, fov : Double)
    fun localToGlobal()
}

abstract class DrawableObject(val local: Array<Coordinate?>, setupPaint: Paint.() -> Unit = {
    style = Paint.Style.STROKE //Stroke
    color = Color.RED
    strokeWidth = 3f
}) : TransformatableGlobal {
    abstract fun drawOnCanvas(canvas: Canvas)

    open val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        setupPaint()
    }

    var global: Array<Coordinate?> = local.copyOf()

    fun drawLinePairs(canvas: Canvas, vertices: Array<Coordinate?>, start: Int, end: Int, paint: Paint) { //draw a line connecting 2 points
//canvas - canvas of the view
//points - array of points
//start - index of the starting point
//end - index of the ending point
//paint - the paint of the line
        canvas.drawLine(vertices[start]!!.x.toFloat(), vertices[start]!!.y.toFloat(), vertices[end]!!.x.toFloat(), vertices[end]!!.y.toFloat(), paint)
    }

    override fun shearGlobal(hx: Double, hy: Double) {
        global = shear(global, hx, hy)
    }

    override fun localToGlobal() {
        global = local.copyOf()
    }

    override fun rotateAxisGlobal(theta: Double, axis: Coordinate) {
        global = rotateAxis(global, theta, axis)
    }

    override fun scaleGlobal(times: Double, dy : Double, dz : Double) {
        global = scale(global, times, dy, dz)
    }

    override fun translateGlobal(dx: Double, dy: Double, dz: Double) {
        global = translate(global, dx, dy, dz)
    }

    override fun projectGlobal(left: Double, right: Double, top: Double, bottom: Double, near: Double, far: Double) {
        global = project(global, left, right, top, bottom, near, far)
    }

    override fun projectGlobal(near: Double, far: Double, fov: Double) {
        global = project(global, near, far, fov)
    }
}