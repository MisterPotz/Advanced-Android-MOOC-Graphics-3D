package com.bennyplo.android_mooc_graphics_3d


fun Array<DoubleArray>.getAt(place: Int): Double {
    return this[place / size][place % this[0].size]
}

inline fun Array<DoubleArray>.setAt(place: Int, value: Double): Array<DoubleArray> {
    this[place / size][place % this[0].size] = value
    return this
}

inline fun Array<DoubleArray>.multiplyAt(place: Int, value: Double): Array<DoubleArray> {
    setAt(place, if (getAt(place) == 0.0) {
        value
    } else {
        getAt(place) * value
    })
    return this
}

fun Array<DoubleArray>.multiplyRow(vertex: Coordinate, row: Int): Double {
    return this[row][0] * vertex.x + this[row][1] * vertex.y + this[row][2] * vertex.z +
            this[row][3] * vertex.w
}