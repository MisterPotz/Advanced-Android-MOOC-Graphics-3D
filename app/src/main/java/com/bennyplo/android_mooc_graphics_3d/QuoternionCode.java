//package com.bennyplo.android_mooc_graphics_3d;
//
///*Title:      mjbWorld
//Copyright (c) 1998-2007 Martin John BakerThis program is free software; you can redistribute it and/or
//   modify it under the terms of the GNU General Public License
//   as published by the Free Software Foundation; either version 2
//   of the License, or (at your option) any later version.This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//   GNU General Public License for more details.For information about the GNU General Public License see http://www.gnu.org/To discuss this program http://sourceforge.net/forum/forum.php?forum_id=122133
//   also see website https://www.euclideanspace.com/
//   */
//import java.io.*; // for steamtokenizer
//import java.lang.ref.*;
//import java.util.*; // for StringTokenizer
///* x3d definition<!ENTITY % SFRotation "CDATA"> <!-- Rotation -->
// *//** a class to represent a rotation, internally the class may code the rotation    as an
// /// axis angle:
// /// https://www.euclideanspace.com/maths/geometry/rotations/axisAngle/index.htm
// /// or a quaternion:
// /// https://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
// /// or as euler angles
// /// https://www.euclideanspace.com/maths/geometry/rotations/euler/index.htm
// */
//public class sfrotation extends property {/** defines the resolution at which the rotation will be saved to file */
//static boolean saveAsDouble = false ;/** x element of axis angle or quaternion*/
//public double x;/** y element of axis angle or quaternion*/
//public double y;/** z element of axis angle or quaternion*/
//public double z;/** angle element of axis angle or w element of quaternion*/
//public double angle;/** VRML always uses axis-angle to represent rotations
// *but quaternions are more efficient for some applications
// * */
//public int coding=CODING_AXISANGLE;/** possible values for coding variable*/
//public static final int CODING_AXISANGLE = 0;
//    public static final int CODING_QUATERNION = 1;
//    public static final int CODING_EULER = 2;
//    public static final int CODING_AXISANGLE_SAVEASQUAT = 3;
//    public static final int CODING_QUATERNION_SAVEASQUAT = 4;
//    public static final int CODING_EULER_SAVEASQUAT = 5;/**constructor which allows initial value to be suplied as axis angle
//     * @param x1 x dimention of normalised axis
//     * @param y1 y dimention of normalised axis
//     * @param z1 z dimention of normalised axis
//     * @param a1 angle
//     */
//    public sfrotation(double x1,double y1,double z1,double a1) {
//        x=x1;
//        y=y1;
//        z=z1;
//        angle=a1;
//    }/** constructor which allows initial value to be suplied as axis angle,quaternion
//     * or axis angle as defined by c1 whoes possible values are given by enum cde
//     * @param x1 if quaternion or axis angle holds x dimention of normalised axis
//     * @param y1 if quaternion or axis angle holds y dimention of normalised axis
//     * @param z1 if quaternion or axis angle holds z dimention of normalised axis
//     * @param a1 if quaternion holds w, if axis angle holds angle
//     * @param c1 possible values are given by enum cde
//     * */
//    public sfrotation(double x1,double y1,double z1,double a1,int c1) {
//        x=x1;
//        y=y1;
//        z=z1;
//        angle=a1;
//        coding=c1;
//    }/** constructor to create sfrotation from euler angles.
//     * @param heading rotation about z axis
//     * @param attitude rotation about y axis
//     * @param bank rotation about x axis
//     */
//    public sfrotation(double heading,double attitude,double bank){
//        double c1 = Math.cos(heading/2);
//        double s1 = Math.sin(heading/2);
//        double c2 = Math.cos(attitude/2);
//        double s2 = Math.sin(attitude/2);
//        double c3 = Math.cos(bank/2);
//        double s3 = Math.sin(bank/2);
//        double c1c2 = c1*c2;
//        double s1s2 = s1*s2;
//        angle =c1c2*c3 + s1s2*s3;
//        x =c1c2*s3 - s1s2*c3;
//        y =c1*s2*c3 + s1*c2*s3;
//        z =s1*c2*c3 - c1*s2*s3;
//        coding=CODING_QUATERNION;
//    }/** copy constructor
//     * @param in1 class to copy
//     * */
//    public sfrotation(sfrotation in1) {
//        x=(in1!=null) ? in1.x : 0;
//        y= (in1!=null) ? in1.y : 0;
//        z= (in1!=null) ? in1.z : 1;
//        angle= (in1!=null) ? in1.angle : 0;
//        coding = (in1!=null) ? in1.coding : CODING_AXISANGLE;
//    }/** constructor
//     * */
//    public sfrotation() {
//    }/** calculates the effect of this rotation on a point
//     * the new point is given by=q * P1 * q'
//     * this version does not alter P1 but returns the result.
//     *
//     * for theory see:
//     * https://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
//     * @param point">point to be transformed</param>
//     * @return translated point</returns>
//     */
//    public sfvec3f getTransform(sfvec3f p1){
//        double wh = angle;
//        double xh = x;
//        double yh = y;
//        double zh = z;
//        if (coding==CODING_AXISANGLE) {
//            double s = Math.sin(angle/2);
//            xh = x * s;
//            yh = y * s;
//            zh = z * s;
//            wh = Math.cos(angle/2);
//        }
//        sfvec3f p2 = new sfvec3f();
//        p2.x = wh*wh*p1.x + 2*yh*wh*p1.z - 2*zh*wh*p1.y + xh*xh*p1.x + 2*yh*xh*p1.y    + 2*zh*xh*p1.z - zh*zh*p1.x - yh*yh*p1.x;
//        p2.y = 2*xh*yh*p1.x + yh*yh*p1.y + 2*zh*yh*p1.z + 2*wh*zh*p1.x - zh*zh*p1.y    + wh*wh*p1.y - 2*xh*wh*p1.z - xh*xh*p1.y;
//        p2.z = 2*xh*zh*p1.x + 2*yh*zh*p1.y + zh*zh*p1.z - 2*wh*yh*p1.x - yh*yh*p1.z    + 2*wh*xh*p1.y - xh*xh*p1.z + wh*wh*p1.z;
//        return p2;
//    }/** calculates the effect of this rotation on a point
//     * the new point is given by=q * P1 * q'
//     * this version returns the result in p1
//     *
//     * for theory see:
//     * https://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
//     * @param point point to be transformed</param>
//     */
//    public void transform(sfvec3f p1){
//        double wh = angle;
//        double xh = x;
//        double yh = y;
//        double zh = z;
//        if (coding==CODING_AXISANGLE) {
//            double s = Math.sin(angle/2);
//            xh = x * s;
//            yh = y * s;
//            zh = z * s;
//            wh = Math.cos(angle/2);
//        }
//        double resultx = wh*wh*p1.x + 2*yh*wh*p1.z - 2*zh*wh*p1.y + xh*xh*p1.x + 2*yh*xh*p1.y    + 2*zh*xh*p1.z - zh*zh*p1.x - yh*yh*p1.x;
//        double resulty = 2*xh*yh*p1.x + yh*yh*p1.y + 2*zh*yh*p1.z + 2*wh*zh*p1.x - zh*zh*p1.y    + wh*wh*p1.y - 2*xh*wh*p1.z - xh*xh*p1.y;
//        double resultz = 2*xh*zh*p1.x + 2*yh*zh*p1.y + zh*zh*p1.z - 2*wh*yh*p1.x - yh*yh*p1.z    + 2*wh*xh*p1.y - xh*xh*p1.z + wh*wh*p1.z;
//        p1.x = resultx;
//        p1.y = resultx;
//        p1.z = resultx;
//    }/** static method to return type of parameter as used in VRML
//     * @return type of parameter as used in VRML
//     */
//    public static String vrmlType_s(){
//        return "SFRotation";
//    }/** method to return type of parameter as used in VRML, need non static method    so
//     * that it can be overridden
//     * @return type of parameter as used in VRML
//     */
//    public String vrmlType(){
//        return "SFRotation";
//    }/** get a class that can edit this
//     * @return class that can edit this
//     */
//    static public Class getEditClass(){
//        return sfrotationEditor.class;
//    }/** override of clone method for this class
//     * @return clone of this
//     */
//    public Object clone() {
//        return new sfrotation(this);
//    }/** create an array of rotations type with a size given by the parameter
//     * @param size size of array to be created
//     * @return array of this type
//     */
//    public property[] createArray(int size){
//        return new sfrotation[size];
//    }/** invert direction of rotation
//     *
//     */
//    public void minus() {
//        if (coding==CODING_AXISANGLE) {
//            angle = -angle;
//            return;
//        }
//        x=-x;
//        y=-y;
//        z=-z;
//    }/** get a clone of the rotation
//     * @return a new array with value of minus this
//     */
//    public sfrotation getMinus() {
//        if (coding==CODING_AXISANGLE) return new sfrotation(x,y,z,-angle,coding);
//        return new sfrotation(-x,-y,-z,angle,coding);
//    }/** set the axis of rotation
//     * @param tx
//     * @param ty
//     * @param tz
//     * */
//    public void set(double tx,double ty,double tz) {
//        angle = Math.sqrt(tx*tx + ty*ty + tz*tz);
//        if (angle == 0) {x=1;y=z=0;return;}
//        x = tx/angle;
//        y = ty/angle;
//        z = tz/angle;
//    }/** set the values of this rotation
//     * @param tx
//     * @param ty
//     * @param tz
//     * @param tangle
//     * */
//    public void set(double tx,double ty,double tz,double tangle){
//        x = tx;
//        y = ty;
//        z = tz;
//        angle = tangle;
//    }/** returns axis in x dimention
//     * @return axis in x dimention
//     * */
//    public double getTx() {
//        return x*angle;
//    }/** returns axis in y dimention
//     * @return returns axis in y dimention
//     * */
//    public double getTy() {
//        return y*angle;
//    }/** returns axis in z dimention
//     * @return returns axis in z dimention
//     * */
//    public double getTz() {
//        return z*angle;
//    }/** calculate total rotation by taking current rotation and then
//     * apply rotation r
//     *
//     * if both angles are quaternions then this is a multiplication
//     * @param r
//     * */
//    public void combine(sfrotation r) {
//        toQuaternion();
//        if (r==null) return;
//        double qax = x;
//        double qay = y;
//        double qaz = z;
//        double qaw = angle;
//        double qbx;
//        double qby;
//        double qbz;
//        double qbw; if (r.coding==CODING_QUATERNION) {
//            qbx = r.x;
//            qby = r.y;
//            qbz = r.z;
//            qbw = r.angle;
//        } else {
//            double s = Math.sin(r.angle/2);
//            qbx = r.x * s;
//            qby = r.y * s;
//            qbz = r.z * s;
//            qbw = Math.cos(r.angle/2);
//        }
//        // now multiply the quaternions
//        angle =qaw*qbw - qax*qbx - qay*qby - qaz*qbz ;
//        x=qax*qbw + qaw*qbx + qay*qbz - qaz*qby;
//        y=qaw*qby - qax*qbz + qay*qbw + qaz*qbx;
//        z=qaw*qbz + qax*qby - qay*qbx + qaz*qbw;
//        coding=CODING_QUATERNION;
//    }/** combine a rotation expressed as euler angle with current rotation.
//     * first convert both values to quaternoins then combine and convert back to
//     * axis angle. Theory about these conversions shown here:
//     * https://www.euclideanspace.com/maths/geometry/rotations/conversions/index.htm
//     * @param heading angle about x axis
//     * @param attitude angle about y axis
//     * @param bank angle about z axis
//     * */
//    public void combine(double heading,double attitude,double bank){
//        // first calculate quaternion qb from heading, attitude and bank
//        double c1 = Math.cos(heading/2);
//        double s1 = Math.sin(heading/2);
//        double c2 = Math.cos(attitude/2);
//        double s2 = Math.sin(attitude/2);
//        double c3 = Math.cos(bank/2);
//        double s3 = Math.sin(bank/2);
//        double c1c2 = c1*c2;
//        double s1s2 = s1*s2;
//        double qbw =c1c2*c3 + s1s2*s3;
//        double qbx =c1c2*s3 - s1s2*c3;
//        double qby =c1*s2*c3 + s1*c2*s3;
//        double qbz =s1*c2*c3 - c1*s2*s3;
//        // then convert axis-angle to quaternion if required
//        toQuaternion();
//        double qax = x;
//        double qay = y;
//        double qaz = z;
//        double qaw = angle;
//        // now multiply the quaternions
//        angle =qaw*qbw - qax*qbx - qay*qby - qaz*qbz ;
//        x=qax*qbw + qaw*qbx + qay*qbz - qaz*qby;
//        y=qaw*qby - qax*qbz + qay*qbw + qaz*qbx;
//        z=qaw*qbz + qax*qby - qay*qbx + qaz*qbw;
//        coding=CODING_QUATERNION;
//    }/** if this rotation is not already coded as axis angle then convert it to    axis angle */
//    public void toAxisAngle(){
//        if (coding==CODING_AXISANGLE) return;
//        double s = Math.sqrt(1-angle*angle);
//        if (Math.abs(s) < 0.001) s=1;
//        angle = 2 * Math.acos(angle);
//        x = x / s;
//        y = y / s;
//        z = z / s;
//    }
//    /** if this rotation is not already coded as quaternion then convert it to quaternion    */
//    public void toQuaternion(){
//        if (coding==CODING_QUATERNION) return;
//        double s = Math.sin(angle/2);
//        x = x * s;
//        y = y * s;
//        z = z * s;
//        angle = Math.cos(angle/2);
//    }/** used when reading XML
//     * called by sfparam which is called by mfparam which is called by filter_x3d
//     *
//     * expects val to be in following format (1.0 2.0 3.0 0.1)
//     */
//    public void setAttribute(String val,String type){
//        try {
//            int v=0; //value being read
//            StringTokenizer st = new StringTokenizer(val,"() \t\n\r\f"); // skip    these tokens
//            while (st.hasMoreElements()) {
//                try {
//                    switch (v) {
//                        case 0: x = Double.parseDouble(st.nextToken());break;
//                        case 1: y = Double.parseDouble(st.nextToken());break;
//                        case 2: z = Double.parseDouble(st.nextToken());break;
//                        case 3: angle = Double.parseDouble(st.nextToken());break;
//                        default: st.nextToken();break; // skip token
//                    }
//                    v++; // valid number was read so goto next
//                } catch (NumberFormatException e) {
//                    // if it is not a valid number continue while loop
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("sfrotation.setAttribute("+val+","+type+")    " + e.toString());
//        }
//    }/** convert x,y,z,angle to string between brackets */
//    public String toString() {
//        return "("+x+","+
//                y+","+
//                z+","+
//                angle+")";
//    }/// call openGL mglRotated
//    /// <param name="axo"></param>
//   /*public void render3d(AxmjboglCtl axo){
//   if (coding==(int)cde.CODING_AXISANGLE) {
//   axo.mglRotated(angle * 180 / Math.PI,x,y,z);
//   return;
//   }
//   double s = Math.Sqrt(1-angle*angle);
//   if (Math.Abs(s) < 0.001) s=1;
//   axo.mglRotated(Math.Acos(angle) * 360 / Math.PI,x / s,y / s,z / s);
//   }*//** output as a string
//     * @param format mode values
//     * 0 - output modified values
//     * 1 - output original values
//     * 2 - output attribute
//     * 3 - output attribute in brackets
//     * 4 - output with f prefix
//     * @return string representation of this class
//     */
//    public String outstring(int format) {
//        if (format == 3) {
//            if (saveAsDouble)
//                return "(" + x + " " + y + " " + z + " "    + angle + ")";
//            else
//                return "(" + new Float(x).toString() + " " +
//                        new Float(y).toString() + " " +
//                        new Float(z).toString() + " " +
//                        new Float(angle).toString() + ")";
//        } else if (format == 4) { // output to C
//            return new Float(angle).toString() + "f *90/1.57," + // convert to    degrees
//                    new Float(x).toString() + "f," +
//                    new Float(y).toString() + "f," +
//                    new Float(z).toString() + "f";
//        } else {
//            if (saveAsDouble)
//                return "" + x + " " + y + " " + z + " "    + angle;
//            else
//                return new Float(x).toString() + " " +
//                        new Float(y).toString() + " " +
//                        new Float(z).toString() + " " +
//                        new Float(angle).toString();
//        }
//    }/** write to file
//     * @param f information about output
//     * @param mode mode values
//     * 0 - output VRML97 modified values
//     * 1 - output VRML97 original values
//     * 2 - output xml (x3d)
//     * 3 - output attribute in brackets
//     * 4 - output with f prefix
//     * @param indent
//     */
//    public void write(filter f,int mode,int indent){
//        toAxisAngle();
//        f.write(outstring(mode));
//    }/** used by mfparam.vrml2par
//     * @param f
//     * @param sfp
//     * @param n
//     * @param mode
//     * @return
//     */
//    public boolean instring(filter f,sfparam sfp,nodeBean n,int mode) {
//        String s;
//        try {
//            s=f.nextToken();
//            if (s!=null) if (s.equals("IS")) {
//                s=f.nextToken();
//                if (sfp!=null) sfp.setIs(s);
//                return true;
//            }
//            x = Double.parseDouble(s);
//            s=f.nextToken();
//            y = Double.parseDouble(s);
//            s=f.nextToken();
//            z = Double.parseDouble(s);
//            s=f.nextToken();
//            angle = Double.parseDouble(s);
//        } catch (Exception e) {
//            System.out.println("sfrotation.instring " + e.toString());
//        }
//        return true;
//    }/** parse string which contains rotation
//     * @param f
//     * @param s1
//     * @return
//     */
//    public boolean instring(filter f,String s1) {
//        String s;
//        try {
//            x = Double.parseDouble(s1);
//            s=f.nextToken();
//            y = Double.parseDouble(s);
//            s=f.nextToken();
//            z = Double.parseDouble(s);
//            s=f.nextToken();
//            angle = Double.parseDouble(s);
//        } catch (Exception e) {
//            System.out.println("sfrotation.instring " + e.toString());
//        }
//        return true;
//    }
//}