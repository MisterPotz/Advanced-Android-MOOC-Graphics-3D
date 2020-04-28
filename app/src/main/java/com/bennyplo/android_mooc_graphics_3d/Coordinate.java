package com.bennyplo.android_mooc_graphics_3d;
//*********************************************
//* Homogeneous coordinate in 3D space

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

import static java.lang.Math.abs;

public class Coordinate {
    public double x, y, z, w;

    public Coordinate() {//create a coordinate with 0,0,0
        x = 0;
        y = 0;
        z = 0;
        w = 0;
    }

    public Coordinate(double x, double y, double z, double w) {//create a Coordinate object
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void Normalise() {//to keep it as a homogeneous coordinate -> divide the coordinate with w and set w=1
        if (w != 0 && w != 1) {//ensure that w!=0
            x /= w;
            y /= w;
            z /= w;
            w = 1;
        } else w = 1;
    }

    public void normalizeVec() {
/*        if (abs(x) <= 1 && abs(y) <= 1 && abs(z) <= 1) {
            return;
        }*/

        double len = Math.sqrt(x * x + y * y + z * z);
        x = x / len;
        y = y / len;
        z = z / len;
    }

    public Coordinate copy() {
        return new Coordinate(x, y, z, w);
    }

    private boolean almostEqual(double expected, double target) {
        if (target == 0.0 || expected == 0.0) {
            return abs(expected - target) <= 0.0000001;
        }
        double relation = (target / expected);
        return relation <= 1.0000001 && relation >= 0.9999999;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return almostEqual(this.x, that.x) && almostEqual(this.y, that.y) &&
                almostEqual(this.z, that.z) && almostEqual(this.w, that.w);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String toString() {
        return ("Coordinate "+ this.hashCode() + " x:" + x + " y:" + y + " z:" + z + " w:" + w);
    }
}
