package com.bennyplo.android_mooc_graphics_3d

fun axis(x: Double, y: Double, z: Double) = Coordinate(x, y, z, 0.0)

fun axis(x: Int, y: Int, z: Int) = Coordinate(x.toDouble(), y.toDouble(), z.toDouble(), 0.0)