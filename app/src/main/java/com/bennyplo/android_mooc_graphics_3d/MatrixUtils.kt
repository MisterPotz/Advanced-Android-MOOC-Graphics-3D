package com.bennyplo.android_mooc_graphics_3d

import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

fun transformation(vertex: Coordinate?, matrix: Array<DoubleArray>): Coordinate { //affine transformation with homogeneous coordinates
//i.e. a vector (vertex) multiply with the transformation matrix
// vertex - vector in 3D
// matrix - transformation matrix
    val result = Coordinate()
    result.x = matrix.multiplyRow(vertex!!, 0)
    result.y = matrix.multiplyRow(vertex, 1)
    result.z = matrix.multiplyRow(vertex, 2)
    result.w = matrix.multiplyRow(vertex, 3)
    return result
}

fun transformation(vertices: Array<Coordinate?>, matrix: Array<DoubleArray>): Array<Coordinate?> { //Affine transform a 3D object with vertices
// vertices - vertices of the 3D object.
// matrix - transformation matrix
    val result = arrayOfNulls<Coordinate>(vertices.size)
    for (i in vertices.indices) {
        result[i] = transformation(vertices[i], matrix)
        //result[i]!!.Normalise()
    }
    return result
}

//***********************************************************
//Affine transformation
fun translate(vertices: Array<Coordinate?>, tx: Double, ty: Double, tz: Double): Array<Coordinate?> {
    val matrix = DrawView.getIdentityMatrix()
    matrix.setAt(3, tx)
    matrix.setAt(7, ty)
    matrix.setAt(11, tz)

    return transformation(vertices, matrix)
}

fun scale(vertices: Array<Coordinate?>, sx: Double, sy: Double, sz: Double): Array<Coordinate?> {
    val matrix = DrawView.getIdentityMatrix()
    matrix.setAt(0, sx)
    matrix.setAt(5, sy)
    matrix.setAt(10, sz)
    return transformation(vertices, matrix)
}

fun shear(vertices: Array<Coordinate?>, hx: Double, hy: Double): Array<Coordinate?> {
    val matrix = DrawView.getIdentityMatrix()
    matrix.setAt(2, hx)
    matrix.setAt(6, hy)
    return transformation(vertices, matrix)
}

/**
 *
 */
@Deprecated("please use rotation functions around axes separately")
fun rotate(vertices: Array<Coordinate?>, xTheta: Double, yTheta: Double, zTheta: Double): Array<Coordinate?> {
    var verticesPipe: Array<Coordinate?>? = null
    if (xTheta != 0.0) {
        verticesPipe = rotateX(verticesPipe ?: vertices, xTheta)
    }
    if (yTheta != 0.0) {
        verticesPipe = rotateY(verticesPipe ?: vertices, yTheta)
    }
    if (zTheta != 0.0) {
        verticesPipe = rotateZ(verticesPipe ?: vertices, zTheta)
    }
    return verticesPipe ?: vertices
}

fun rotateX(vertices: Array<Coordinate?>, xTheta: Double, degrees: Boolean = true): Array<Coordinate?> {
    val identityMatrix = DrawView.getIdentityMatrix()
    val xThetaActual = if (degrees) Math.toRadians(xTheta) else xTheta
    identityMatrix.apply {
        multiplyAt(5, cos(xThetaActual))
        multiplyAt(6, -sin(xThetaActual))
        multiplyAt(9, sin(xThetaActual))
        multiplyAt(10, cos(xThetaActual))
    }
    return transformation(vertices, identityMatrix)
}

fun rotateY(vertices: Array<Coordinate?>, yTheta: Double, degrees: Boolean = true): Array<Coordinate?> {
    val identityMatrix = DrawView.getIdentityMatrix()
    val yThetaActual = if (degrees) Math.toRadians(yTheta) else yTheta
    identityMatrix.apply {
        multiplyAt(0, cos(yThetaActual))
        multiplyAt(2, sin(yThetaActual))
        multiplyAt(8, -sin(yThetaActual))
        multiplyAt(10, cos(yThetaActual))
    }
    return transformation(vertices, identityMatrix)
}

fun rotateZ(vertices: Array<Coordinate?>, zTheta: Double, degrees: Boolean = true): Array<Coordinate?> {
    val identityMatrix = DrawView.getIdentityMatrix()
    val zThetaActual = if (degrees) Math.toRadians(zTheta) else zTheta
    identityMatrix.apply {
        multiplyAt(0, cos(zThetaActual))
        multiplyAt(1, -sin(zThetaActual))
        multiplyAt(4, sin(zThetaActual))
        multiplyAt(5, cos(zThetaActual))
    }
    return transformation(vertices, identityMatrix)
}

fun rotateAxis(vertices: Array<Coordinate?>, theta: Double, vectorInitial: Coordinate): Array<Coordinate?> {
    val identityMatrix = DrawView.getIdentityMatrix()
    val vector = Coordinate(vectorInitial.x, vectorInitial.y, vectorInitial.z, vectorInitial.w)
    // full vector normalization
    vector.normalizeAdeq(theta)
    identityMatrix.apply {
        setAt(0, vector.run { w.pow(2) + x.pow(2) - y.pow(2) - z.pow(2) })
        setAt(1, vector.run { 2 * x * y - 2 * w * z })
        setAt(2, vector.run { 2 * x * z + 2 * w * y })

        setAt(4, vector.run { 2 * x * y + 2 * w * z })
        setAt(5, vector.run { w.pow(2) - x.pow(2) + y.pow(2) - z.pow(2) })
        setAt(6, vector.run { 2 * y * z - 2 * w * x })

        setAt(8, vector.run { 2 * x * z - 2 * w * y })
        setAt(9, vector.run { 2 * y * z + 2 * w * x })
        setAt(10, vector.run { w.pow(2) - x.pow(2) - y.pow(2) + z.pow(2) })
    }
    return transformation(vertices, identityMatrix)
}

fun Coordinate.normalizeAdeq(theta: Double) {
    // firstly normalizing au-vector
    normalizeVec()

    val halfAngle = Math.toRadians(theta) / 2
    w = cos(halfAngle)
    val sinHalf = sin(halfAngle)
    x *= sinHalf
    y *= sinHalf
    z *= sinHalf
}